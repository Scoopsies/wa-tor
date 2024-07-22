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
      (prn (count surrounded-list))
      (should= 9 (count (sut/update-board surrounded-list))))))


#_(describe "update-board"
  (context "update-board"
    (it "removes any eaten fish"
      (should= [:shark] (map :species (sut/update-board [(c/create :shark [0 0]) (c/create :fish [0 1])]))))

    (it "doesn't remove surrounded creature"
      (should=
        (repeat 8 :shark)
        (map :species
             (sut/update-board
               (conj (map #(c/create :shark %) (m/->adjacent-cells [1 1])) (c/create :shark [1 1]))))))

    (it "doesn't include dead sharks"
      (should= [] (sut/update-board [(assoc (c/create :shark [0 0]) :energy 0)])))

    (it "adds a creature when breeding is 0"
      (should= [:fish :fish] (map :species (sut/update-board [(assoc (c/create :fish [0 0]) :breeding 0)]))))

    (it "decreases energy stat of shark if no fish eaten"
      (should= [(dec s/shark-energy)] (map :energy (sut/update-board [(c/create :shark [0 0])]))))

    (it "moves energy back to full if fish eaten"
      (should= [s/shark-energy] (map :energy (sut/update-board [(c/create :shark [1 1]) (c/create :fish [0 0])] ))))

    (it "decreases breeding stat of creature"
      (should= [(dec s/fish-breeding)] (map :breeding (sut/update-board [(c/create :fish [0 0])]))))
    )
  )
