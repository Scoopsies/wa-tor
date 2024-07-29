(ns wa-tor.move.fish-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move.core :as move-core]
            [wa-tor.move.fish :as sut]))

(describe "move -fish"
  (context "get-adjacent"
    (it "returns all adjacent spaces of [1 1]"
      (should-contain [0 0] (sut/get-adjacent [1 1]))
      (should-contain [1 0] (sut/get-adjacent [1 1]))
      (should-contain [2 0] (sut/get-adjacent [1 1]))
      (should-contain [0 1] (sut/get-adjacent [1 1]))
      (should-contain [2 1] (sut/get-adjacent [1 1]))
      (should-contain [0 2] (sut/get-adjacent [1 1]))
      (should-contain [1 2] (sut/get-adjacent [1 1]))
      (should-contain [2 2] (sut/get-adjacent [1 1]))
      )

    (it "wraps value around the grid if below bounds"
      (should-contain [19 19] (sut/get-adjacent [0 0])))

    (it "wraps value around the grid if below bounds"
      (should-contain [0 0] (sut/get-adjacent [19 19])))
    )

  (context "get-vacant-adjacent"
    (it "returns all unoccupied adjacent spaces"
      (let [position [1 1]
            adjacent (sut/get-adjacent [1 1])
            fish-list [(c/create :fish (first adjacent))]]
        (should= (set (rest adjacent)) (set (sut/get-vacant-adjacent position fish-list))))

      (let [position [1 1]
            adjacent (sut/get-adjacent [1 1])
            fish-list [(c/create :fish (first adjacent))
                       (c/create :fish (second adjacent))]]
        (should= (set (drop 2 adjacent)) (set (sut/get-vacant-adjacent position fish-list)))))
    )

  (context "get-move"
    (with-stubs)
    (redefs-around [rand-nth (stub :rand-nth {:invoke first})])
    (it "returns a random, unoccupied, adjacent space"
      (let [position [1 1]
            adjacent (sut/get-adjacent [1 1])
            fish (c/create :fish position)
            fish-list [(c/create :fish (first adjacent))]]
        (should= (first (sut/get-vacant-adjacent position fish-list))
                 (move-core/get-move fish fish-list))))

    (it "returns original position if fish is surrounded"
      (let [position [1 1]
            adjacent (sut/get-adjacent [1 1])
            fish (c/create :fish position)
            fish-list (map #(c/create :fish %) adjacent)]
        (should= position
                 (move-core/get-move fish fish-list))))
    )
  )