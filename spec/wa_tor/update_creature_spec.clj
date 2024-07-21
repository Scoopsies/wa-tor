(ns wa-tor.update-creature-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.update-creature :as sut]
            [wa-tor.move.core :as move]))

(describe "move"
  (with-stubs)
  (context "update-stats"
    (let [fish (c/create :fish) fish-breeding (:breeding fish)
          shark (c/create :shark) shark-breeding (:breeding shark)]
      (it "decreases creatures breeding by 1"
        (should= (dec fish-breeding) (:breeding (sut/update-creature fish)))
        (should= (dec shark-breeding) (:breeding (sut/update-creature shark)))))

    (it "decreases a shark's energy"
      (let [shark (c/create :shark) {:keys [energy breeding]} shark]
        (should= (sut/update-creature shark)
                 (assoc shark :breeding (dec breeding) :energy (dec energy)))))

    (it "keeps a fish's energy as nil"
      (should-not (:energy (sut/update-creature (c/create :fish)))))

    (it "updates the creatures position"
      (with-redefs [move/get-move (stub :get-move {:return [22 22]})]
        (should= [22 22] (:position (sut/update-creature (c/create :fish [10 10]))))
        (should= [22 22] (:position (sut/update-creature (c/create :shark [10 10]))))))
    )
  )