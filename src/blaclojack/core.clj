(ns blaclojack.core
  (:gen-class))

(def suits [:clover :diamond :spade :heart])
(def indices (range 1 14))

(defn- make-deck []
  (shuffle
   (for [suit suits, i indices]
     {:suit suit :index i})))

(defn- make-player
  {:arglists '([name & opts])}
  [name & {:keys [open-only]}]
  {:name name :cards [] :open-only open-only})

(defn displayable?
  {:arglists '([player])}
  [{:keys [open-only cards]}]
  (or (nil? open-only)
      (<= (count cards) open-only)))

(defn draw-card [player deck]
  (dosync
   (alter player update :cards conj (first @deck))
   (alter deck rest))
  nil)

(def ^{:arglists '([suit])
       :private  true}
  suit->str
  {:clover  "クローバー" :heart "ハート"
   :diamond "ダイヤ"     :spade "スペード"})

(defn- rank->str [idx]
  (let [m {1  "エース"   11 "ジャック"
           12 "クイーン" 13 "キング"}]
    (get m idx idx)))

(defn card->str
  {:arglists '([card])}
  [{:keys [suit index]}]
  (let [rank-or-index (or (rank->str index) index)]
    (format "%sの%s" (suit->str suit) rank-or-index)))

(defn- point
  {:arglists '([card])}
  [{:keys [index]} & ace?]
  (case index
    (11 12 13) 10
    1          (if ace? 11 1)
    index))

(defn bust? [n] (> n 21))

(defn sum-point
  {:arglists '([player])}
  [{:keys [cards]}]
  (let [smaller (->> cards (map point) (reduce + 0))
        bigger  (->> cards (map #(point % :ace)) (reduce + 0))]
    (if (bust? bigger)
      smaller
      bigger)))

(defn- ask-draw? []
  (print "カードを引きますか？ (y/n): ")
  (flush)
  (case (read-line)
    "y" true
    "n" false
    (do (println "y か n を入力してください")
        (recur))))

(defn- print-secret-draw
  {:arglists '([player])}
  [{:keys [name]}]
  (println (format "%sがカードを引きました" name)))

(defn- print-last-card
  {:arglists '([player])}
  [{:keys [name cards] :as player}]
  (let [fmt "%sの%d枚目のカード: %s"
        s   (card->str (last cards))
        cnt (count cards)]
    (if (displayable? player)
      (println (format fmt name cnt s))
      (print-secret-draw player))))

(defn- print-point
  {:arglists '([player])}
  [{:keys [name] :as player}]
  (let [sum (sum-point player)]
    (println (format "%sの現在のスコア: %sポイント" name sum))))

(defn draw-initial-cards [player deeler deck]
  (let [order [player deeler player deeler]]
    (dosync
     (doseq [target order]
       (draw-card target deck)
       (print-last-card @target))))
  nil)

(defn dealer-phase
  {:arglists '([dealer deck])}
  [dealer deck]
  (when-not (>= (sum-point @dealer) 17)
    (draw-card dealer deck)
    (print-last-card @dealer)
    (when-not (bust? (sum-point @dealer))
      (recur dealer deck))))

(defn winner [& players]
  (last (sort-by sum-point players)))

(defn game []
  (println)
  (let [player-name "あなた"
        dealer-name "ディーラー"
        deck        (ref (make-deck))
        player      (ref (make-player player-name))
        dealer      (ref (make-player dealer-name :open-only 1))]
    (draw-initial-cards player dealer deck)
    (print-point @player)

    (loop []
      (if (bust? (sum-point @player))
        (println "残念！ バーストしてしまいました")  ; quit loop
        (if (ask-draw?)
          (do  ; player's draw
            (draw-card player deck)
            (print-last-card @player)
            (print-point @player)
            (recur))

          (do
            (dealer-phase dealer deck)
            (if (bust? (sum-point @dealer))
              (do (print-point @dealer)
                  (println
                   (format
                    "%sがバーストしました。あなたの勝利です！"
                    dealer-name)))
              (let [{:keys [name]} (winner @player @dealer)]
                ;; TODO implement draw-game
                (print-point @player)
                (print-point @dealer)
                (println (str name "の勝利！"))))))))))


(defn -main [& _]
  (game))
