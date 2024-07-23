(ns wa-tor.move.fish
  (:require [clojure.set :as set]
            [wa-tor.move.core :as move]))

(defn translate [xy]
  (cond
    (= -1 xy) 19
    (= 20 xy) 0
    :else xy))

(defn get-adjacent-spaces [position]
  (let [[x y] position]
    (for [y [(dec y) y (inc y)]
          x [(dec x) x (inc x)]
          :when (not= position [x y])]
      (map translate [x y]))))

(defn get-vacant-adjacent [position all-creatures]
  (let [adjacent-spaces (get-adjacent-spaces position)]
    (sort-by second (vec (set/difference (set adjacent-spaces) (set (map :position all-creatures)))))))

(defmethod move/get-move :fish [creature all-creatures]
  (let [vacant-adjacent (get-vacant-adjacent (:position creature) all-creatures)]
    (if (empty? vacant-adjacent)
      (:position creature)
      (rand-nth vacant-adjacent))))