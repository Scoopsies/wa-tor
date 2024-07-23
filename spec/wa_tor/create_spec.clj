(ns wa-tor.create-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as sut]
            [wa-tor.settings :as settings]))

(describe "create"
  (with-stubs)
  (context "get-rand-position"
    (it "returns a vector of two random integers"
      (with-redefs [rand-int (stub :rand-int {:return 21})]
        (should= [21 21] (sut/get-rand-position))))

    (it "returns a vector whose values never exceed 19"
      (let [[x y] (sut/get-rand-position)]
        (should (and (> 20 x) (> 20 y)))))
    )

  (context ":fish"
    (it "creates a fish with position specified"
      (should= (sut/create :fish [0 0])
               {:species :fish :breeding sut/fish-breeding :position [0 0]})
      (should= (sut/create :fish [1 1])
               {:species :fish :breeding sut/fish-breeding :position [1 1]}))

    (it "creates a fish with random position if position not specified"
      (with-redefs [rand-int (stub :rand-int {:return 21})]
        (should= {:species :fish :breeding sut/fish-breeding :position [21 21]}
                 (sut/create :fish))))
    )

  (context ":shark"
    (it "creates a shark with position specified"
      (should= {:species :shark :breeding sut/shark-breeding
                :position [0 0] :energy sut/shark-energy}
               (sut/create :shark [0 0])))

    (it "creates a shark with random position if position not specified"
      (with-redefs [rand-int (stub :rand-int {:return 21})]
        (should= {:species :shark :breeding sut/shark-breeding
                  :position [21 21] :energy sut/shark-energy}
                 (sut/create :shark))))
    )

  (context ":board"
    (it "populates a board with creatures"
      (should= [:shark :fish] (map :species (sut/create :board 1 1)))
      (should= [:shark :shark :fish :fish] (map :species (sut/create :board 2 2))))

    (it "all creatures positions are randomized"
      (with-redefs [rand-int (stub :rand-int {:return 21})]
        (should= [[21 21] [21 21]] (map :position (sut/create :board 1 1)))))
    )
  )