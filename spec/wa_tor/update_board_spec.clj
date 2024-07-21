(ns wa-tor.update-board-spec
  (:require [speclj.core :refer :all]
            [wa-tor.update-board :as sut]
            [wa-tor.create :as c]
            [wa-tor.move :as m]
            [wa-tor.settings :as s]))

(describe "update-board"
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
