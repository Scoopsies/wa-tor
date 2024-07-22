(ns wa-tor.move.shark-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move.core :as sut]
            [wa-tor.move.shark :as shark]))

(describe "move.shark"
  (with-stubs)
  (context "get-adjacent"
    (it "returns a list of adjacent cells"
      (should= (shark/get-adjacent (c/create :shark [1 1]))
               [[0 0] [1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2]])
      (should= (shark/get-adjacent (c/create :shark [2 2]))
               [[1 1] [2 1] [3 1] [1 2] [3 2] [1 3] [2 3] [3 3]]))

    (it "translates those cells if going below bounds"
      (should= (shark/get-adjacent (c/create :shark [0 0]))
               [[19 19] [0 19] [1 19] [19 0] [1 0] [19 1] [0 1] [1 1]]))

    (it "translates those cells if going above bounds"
      (should= (mapv vec (shark/get-adjacent (c/create :shark [19 19])))
               [[18 18] [19 18] [0 18] [18 19] [0 19] [18 0] [19 0] [0 0]]))
    )

  (context "get-adjacent-fish"
    (it "returns a list of adjacent fish"
      (let [creature (c/create :shark [0 0])
            adj-fish (c/create :fish [0 1])
            far-fish (c/create :fish [15 10])]
        (should= [[0 1]] (shark/get-adjacent-fish creature [creature adj-fish]))
        (should= [[0 1]] (shark/get-adjacent-fish creature [creature adj-fish far-fish]))
        (should= [] (shark/get-adjacent-fish creature [creature far-fish]))))
    )

  (it "moves shark to random adjacent space if board is empty"
    (with-redefs [rand-nth (stub :rand-nth {:invoke first})]
      (should= [2 2] (sut/get-move (c/create :shark [1 1]) [(c/create :shark [1 1])]))))

  (it "moves shark to place with fish if one is adjacent"
    (should= [1 2] (sut/get-move (c/create :shark [1 1]) [(c/create :shark [1 1])
                                                          (c/create :fish [1 2])])))

  (it "does not move shark if no adjacent spaces available"
    (let [shark (c/create :shark [1 1])
          adjacent-sharks (map #(c/create :shark %) (shark/get-adjacent shark))]
      (should= [1 1] (sut/get-move shark adjacent-sharks))))

  )
