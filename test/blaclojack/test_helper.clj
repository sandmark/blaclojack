(ns blaclojack.test-helper)

(defn int->card [n]
  {:index n})

(defn coll->cards [coll]
  (map int->card coll))

(defn make-player
  {:arglists '([& opts])}
  [& {:keys [name cards open-only]}]
  {:name      (or name "TestPlayer")
   :cards     (mapv int->card cards)
   :open-only open-only})
