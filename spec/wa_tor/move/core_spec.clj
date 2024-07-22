(ns wa-tor.move.core-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.move.core :as sut]))

(describe "move.core"

  (context "get-move :default"
    (it "returns the original position"
      (let [fish (c/create :fish) {:keys [position]} fish]
        (should= position (sut/get-move fish [])))

      #_(let [shark (c/create :shark [4 4]) {:keys [position]} shark]
        (should= position (sut/get-move shark [])))))
  )