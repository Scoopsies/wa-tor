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

(defn get-vacant-adjacent [adjacent-spaces all-creatures]
  (let [occupied-positions (map :position all-creatures)]
    (vec (set/difference (set adjacent-spaces) (set occupied-positions)))))

(defn get-adjacent-fish [adjacent-spaces all-creatures]
  (let [fish-list (filter #(= :fish (:species %)) all-creatures)
        fish-positions (map :position fish-list)]
    (vec (set/intersection (set adjacent-spaces) (set fish-positions)))))

(defmethod core/get-move :shark [creature all-creatures]
  (let [adjacent-spaces (get-adjacent creature)
        vacant-adjacent (get-vacant-adjacent adjacent-spaces all-creatures)
        adjacent-fish (get-adjacent-fish adjacent-spaces all-creatures)]
    (cond
      (not-empty adjacent-fish) (rand-nth adjacent-fish)
      (empty? vacant-adjacent) (:position creature)
      :else (rand-nth vacant-adjacent))))
