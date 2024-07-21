(ns wa-tor.move.shark
  (:require [wa-tor.move.core :as core]
            [clojure.set :as set]))

(defn- format-cell [cell]
  (map #(cond (= -1 %) 19 (= 20 %) 0 :else %) cell))

(defn get-adjacent [{:keys [position]}]
  (let [[x y] position]
    (for [y [(dec y) y (inc y)]
          x [(dec x) x (inc x)]
          :when (not= position [x y])]
      (format-cell [x y]))))

(defmethod core/get-move :shark [creature all-creatures]
  (let [adjacent-spaces (get-adjacent creature)
        fish-list (filter #(= :fish (:species %)) all-creatures)
        fish-positions (map :position fish-list)
        adjacent-fish (vec (set/intersection (set adjacent-spaces) (set fish-positions)))]
    (if (empty? adjacent-fish)
      (rand-nth (get-adjacent creature))
      (rand-nth adjacent-fish))))
