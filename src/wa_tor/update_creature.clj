(ns wa-tor.update-creature
  (:require [wa-tor.move.core :as move]))

(defn- dec-breeding [creature]
  (let [{:keys [breeding]} creature]
    (assoc creature :breeding (dec breeding))))

(defn- dec-energy [creature]
  (let [{:keys [energy]} creature]
    (assoc creature :energy (dec energy))))

(defn- update-position [creature]
  (let [position (move/get-move creature)]
    (assoc creature :position position)))

(defmulti update-creature :species)

(defmethod update-creature :fish [creature]
  (->> (dec-breeding creature)
       (update-position)))

(defmethod update-creature :shark [creature]
  (->> (dec-breeding creature)
       (dec-energy)
       (update-position)))