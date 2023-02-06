(ns blaclojack.core-test
  (:require
   [blaclojack.core :as sut]
   [blaclojack.test-helper :as h]
   [clojure.test :as t]))

(t/deftest game-test
  (t/testing "Bust"
    (t/is (true? (sut/bust? 22)))
    (t/is (false? (sut/bust? 21))))

  (t/testing "Hide Deeler's Cards"
    (let [open     (h/make-player :cards [1] :open-only 1)
          secret   (h/make-player :cards [1 2] :open-only 1)
          whatever (h/make-player :cards [1] :open-only nil)]
      (t/is (sut/displayable? open))
      (t/is (not (sut/displayable? secret)))
      (t/is (sut/displayable? whatever))))

  (t/testing "Point Summary"
    (let [blackjack  (h/make-player :cards [1 13])
          ace-as-one (h/make-player :cards [1 1 10])]
      (t/is (= 21 (sut/sum-point blackjack)))
      (t/is (= 12 (sut/sum-point ace-as-one)))))

  (t/testing "Winner"
    (let [p1 (h/make-player :cards [1 13])
          p2 (h/make-player :cards [1 1])]
      (t/is (= p1 (sut/winner p1 p2)))
      (t/is (= p1 (sut/winner p1 p2))))))

(t/deftest display-test
  (t/testing "card->str"
    (t/is (= "スペードのエース" (sut/card->str {:index 1 :suit :spade})))
    (t/is (= "ハートのクイーン" (sut/card->str {:index 12 :suit :heart})))))

(t/deftest state-test
  (t/testing "Draw"
    (let [player  (ref (h/make-player))
          indices [1 2 3 4]
          deck    (ref (h/coll->cards indices))]
      (doseq [idx indices]
        (sut/draw-card player deck)
        (t/is (= idx
                 (get-in @player [:cards (dec idx) :index]))))
      (t/is (empty? @deck)))))
