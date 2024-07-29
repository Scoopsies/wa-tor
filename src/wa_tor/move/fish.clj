(ns wa-tor.move.fish
  (:require [clojure.set :as set]
            [wa-tor.move.core :as move-core]
            [wa-tor.core :as core]))

(defn- wrap-grid [xy]
  (cond
    (= xy -1) (dec core/row-size)
    (= xy core/row-size) 0
    :else xy))

(defn get-adjacent [[x y]]
  (for [Y [(dec y) y (inc y)]
        X [(dec x) x (inc x)]
        :when (not= [x y] [X Y])]
    (map wrap-grid [X Y])))

(defn get-vacant-adjacent [position fish-list]
  (let [adjacent (get-adjacent position)
        all-occupied (map :position fish-list)]
    (vec (set/difference (set adjacent) (set all-occupied)))))

(defmethod move-core/get-move :fish [{:keys [position]} fish-list]
  (let [vacant-adjacent (get-vacant-adjacent position fish-list)]
    (if (empty? vacant-adjacent)
      position
      (rand-nth vacant-adjacent))))