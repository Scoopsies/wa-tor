(ns wa-tor.update-board-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move.shark :as shark]
            [wa-tor.update-board :as sut]))

(describe "update-board"
  (with-stubs)
  (it "updates all stats of all creatures"
    (let [shark (c/create :shark [0 0]) {:keys [energy breeding]} shark]
      (should= [(dec energy)] (map :energy (sut/update-board [shark])))
      (should= [(dec energy) (dec energy)]
               (map :energy (sut/update-board [shark shark])))

      (should= [(dec breeding)] (map :breeding (sut/update-board [shark])))
      (should= [(dec breeding) (dec breeding)]
               (map :breeding (sut/update-board [shark shark])))

      (with-redefs [rand-nth (stub :rand-nth {:return [15 15]})]
        (should= [[15 15] [15 15]] (map :position (sut/update-board [shark shark]))))))

  (it "filters out eaten fish"
    (should= [:shark] (map :species (sut/update-board [(c/create :shark [0 0]) (c/create :fish [0 1])]))))

  (it "doesn't filter out non-eaten fish"
    (let [shark (c/create :shark [0 0]) adj-fish (c/create :fish [0 1]) far-fish (c/create :fish [15 15])]
      (should= [:shark :fish] (map :species (sut/update-board [shark far-fish])))
      (should= [:shark :fish] (map :species (sut/update-board [shark far-fish adj-fish])))))

  (it "adds another creature if creatures breeding is 0"
    (let [pregnant-shark (assoc (c/create :shark [1 1]) :breeding 0)]
      (should= [:shark :shark] (map :species (sut/update-board [pregnant-shark])))
      (should= [:shark :shark] (map :species (sut/update-board [pregnant-shark (c/create :fish [0 1])])))
      (should= [:shark :shark :fish] (map :species (sut/update-board [pregnant-shark (c/create :fish [10 10])])))))

  (it "does not add another creature if it can't move"
    (let [pregnant-shark (assoc (c/create :shark [1 1])  :breeding 0)
          adjacent-spaces (shark/get-adjacent pregnant-shark)
          surrounded-list (concat [pregnant-shark] (map #(c/create :shark %) adjacent-spaces))]
      (should= 9 (count (sut/update-board surrounded-list)))))

  (it "filters out dead sharks"
    (let [shark (c/create :shark [1 1]) dead-shark (assoc (c/create :shark [1 2]) :energy 0)]
      (should= [:shark] (map :species (sut/update-board [shark dead-shark])))))
  )