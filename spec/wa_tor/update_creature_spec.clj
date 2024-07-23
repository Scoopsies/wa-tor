(ns wa-tor.update-creature-spec
  (:require [speclj.core :refer :all]
            [wa-tor.create :as c]
            [wa-tor.settings :as settings]
            [wa-tor.update-creature :as sut]))

(describe "update-creature"
  (with-stubs)
  (context "update-stats"
    (let [fish (c/create :fish) fish-breeding (:breeding fish)
          shark (c/create :shark) shark-breeding (:breeding shark)]
      (it "decreases creatures breeding by 1"
        (should= (dec fish-breeding) (:breeding (sut/update-creature fish [])))
        (should= (dec shark-breeding) (:breeding (sut/update-creature shark [])))))

    (it "decreases a shark's energy"
      (with-redefs [rand-nth (stub :rand-nth {:return [0 0]})]
        (let [shark (c/create :shark [0 0]) {:keys [energy breeding]} shark]
          (should= (sut/update-creature shark [])
                   (assoc shark :breeding (dec breeding) :energy (dec energy))))))

    (it "keeps a fish's energy as nil"
      (should-not (:energy (sut/update-creature (c/create :fish) []))))

    (it "puts sharks energy back to full if adjacent fish (eats fish)."
      (let [shark (c/create :shark [0 0]) all-creatures [shark (c/create :fish [1 1])]]
        (should= c/shark-energy (:energy (sut/update-creature shark all-creatures)))))

    (it "resets a shark's breeding back to full if at 0"
      (let [shark (assoc (c/create :shark) :breeding 0)]
        (should= c/shark-breeding (:breeding (sut/update-creature shark [])))))

    (it "resets a fish's breeding back to full if at 0"
      (let [fish (assoc (c/create :fish) :breeding 0)]
        (should= c/fish-breeding (:breeding (sut/update-creature fish [])))))
    )
  )