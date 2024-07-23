(ns wa-tor.update-creature
  (:require [wa-tor.move.core :as move]
            [wa-tor.move.shark :as shark]
            [wa-tor.move.fish]
            [wa-tor.create :as c]))

(defn- update-breeding [creature]
  (let [{:keys [breeding species]} creature
        refill (if (= :fish species) c/fish-breeding c/shark-breeding)]
    (if (zero? breeding)
      (assoc creature :breeding refill)
      (assoc creature :breeding (dec breeding)))))

(defn- dec-energy [creature]
  (let [{:keys [energy]} creature]
    (assoc creature :energy (dec energy))))

(defn- refill-energy [creature]
  (assoc creature :energy c/shark-energy))

(defn update-energy [creature adjacent-fish]
  (if (empty? adjacent-fish)
    (dec-energy creature)
    (refill-energy creature)))

(defn- update-position [creature all-creatures]
  (let [position (move/get-move creature all-creatures)]
    (assoc creature :position position)))

(defmulti update-creature :species)

(defmethod update-creature :fish [creature all-creatures]
  (update-position (update-breeding creature) all-creatures))

(defmethod update-creature :shark [creature all-creatures]
  (let [adjacent-fish (shark/get-adjacent-fish creature all-creatures)]
    (->
      (update-energy creature adjacent-fish)
      (update-position all-creatures)
      (update-breeding))))