(ns wa-tor.move-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move :as sut]))

(describe "move"
  (context "get-adjacent-spaces"
    (it "returns a list of adjacent spaces"
      (should= [[1 2] [1 0] [2 1] [2 2] [2 0] [0 1] [0 2] [0 0]] (sut/->adjacent-cells [1 1]))
      (should= [[2 3] [2 1] [3 2] [3 3] [3 1] [1 2] [1 3] [1 1]] (sut/->adjacent-cells [2 2])))

    (it "translates any out of bounds spaces to be across the board"
      (should= [[0 1] [0 19] [1 0] [1 1] [1 19] [19 0] [19 1] [19 19]] (sut/->adjacent-cells [0 0]))
      (should= [[19 0] [19 18] [0 19] [0 0] [0 18] [18 19] [18 0] [18 18]] (sut/->adjacent-cells [19 19])))
    )

  (context "move"
    (it "should update position of creature to adjacent un-occupied space"
      (should (some #(= (:position (sut/move (c/create :fish [0 0]) [])) %) (sut/->adjacent-cells [0 0]))))

    (it "should keep creature in same place if surrounded"
      (should= [0 0] (:position (sut/move (c/create :fish [0 0])
                                          (map #(c/create :fish %) (sut/->adjacent-cells [0 0]))))))
    )
  )
