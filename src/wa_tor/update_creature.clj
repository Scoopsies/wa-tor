(ns wa-tor.update-creature
  (:require [wa-tor.move.core :as move]
            [wa-tor.move.shark]))

(defn- dec-breeding [creature]
  (let [{:keys [breeding]} creature]
    (assoc creature :breeding (dec breeding))))

(defn- dec-energy [creature]
  (let [{:keys [energy]} creature]
    (assoc creature :energy (dec energy))))

(defn- update-position [creature all-creatures]
  (let [position (move/get-move creature all-creatures)]
    (assoc creature :position position)))

(defmulti update-creature :species)

(defmethod update-creature :fish [creature all-creatures]
  (update-position (dec-breeding creature) all-creatures))

(defmethod update-creature :shark [creature all-creatures]
  (update-position (dec-energy (dec-breeding creature)) all-creatures))