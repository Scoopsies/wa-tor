(ns wa-tor.create
  (:require [wa-tor.core :as core]
            #_[wa-tor.core :refer [create]]))

(defn get-rand-position []
  [(rand-int core/row-size) (rand-int core/row-size)])

(defmulti create (fn [key & _] key))

(defmethod create :fish
  ([_] (create :fish (get-rand-position)))

  ([_ position]
   {:species  :fish
    :breeding core/fish-breeding
    :position position}))

(defmethod create :shark
  ([species] (create species (get-rand-position)))

  ([_ position]
   {:species  :shark
    :breeding core/shark-breeding
    :position position
    :energy   core/shark-energy}))

(defn- repeat-creature [species amount]
  (take amount (repeatedly #(create species))))

(defmethod create :board [_ num-shark num-fish]
  (let [sharks-list (repeat-creature :shark num-shark)
        fish-list (repeat-creature :fish num-fish)]
    (concat sharks-list fish-list)))