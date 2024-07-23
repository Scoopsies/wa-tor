(ns wa-tor.move.fish-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move.core :as core]
            [wa-tor.move.fish :as sut]))

;;Rules:
;; Picks a random, unoccupied, Adjacent space.
;; If no vacant unoccupied spaces, return original position.

(describe "move.fish"
  (with-stubs)
  (context "get-adjacent-spaces"
    (it "returns adjacent spaces of [1 1]"
      (should= [[0 0] [1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2]]
               (sut/get-adjacent-spaces [1 1])))

    (it "returns adjacent spaces of [2 2]"
      (should= [[1 1] [2 1] [3 1] [1 2] [3 2] [1 3] [2 3] [3 3]]
               (sut/get-adjacent-spaces [2 2])))

    (it "translates any cells that go below bounds to be across the map"
      (should= [[19 19] [0 19] [1 19] [19 0] [1 0] [19 1] [0 1] [1 1]]
               (sut/get-adjacent-spaces [0 0])))

    (it "translates any cells that go above bounds to be across the map"
      (should= [[18 18] [19 18] [0 18] [18 19] [0 19] [18 0] [19 0] [0 0]]
               (sut/get-adjacent-spaces [19 19])))
    )

  (context "get-vacant-adjacent"
    (it "returns vacant adjacent spaces"
      (should= (set [[1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2]])
               (set (sut/get-vacant-adjacent [1 1] [(c/create :fish [0 0])]))))
    )

  (context "get-move"
    (redefs-around [rand-nth (stub :rand-nth {:invoke first})])
    (it "returns a random un-occupied space"
      (should= (first (sut/get-vacant-adjacent [1 1] [])) (core/get-move (c/create :fish [1 1]) []))
      (should= (first (sut/get-vacant-adjacent [1 1] [(c/create :fish [2 2])]))
               (core/get-move (c/create :fish [1 1]) [(c/create :fish [2 2])])))

    (it "returns current position if surrounded"
      (let [fish-list (map #(c/create :fish %) (sut/get-adjacent-spaces [1 1]))
            shark-list (map #(c/create :shark %) (sut/get-adjacent-spaces [1 1]))]
        (should= [1 1] (core/get-move (c/create :fish [1 1]) shark-list))
        (should= [1 1] (core/get-move (c/create :fish [1 1]) fish-list))))
    )
  )
