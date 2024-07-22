;(ns wa-tor.move
;  (:require [clojure.set :as set]
;            [wa-tor.settings :as settings]))
;
;(defn translate [n]
;  (cond
;    (= n -1) (dec settings/row-size)
;    (= n settings/row-size) 0
;    :else n))
;
;(defn get-permutations [x y]
;  (for [x [x (inc x) (dec y)]
;        y [y (inc y) (dec y)]]
;    [x y]))
;
;(defn ->adjacent-cells [[x y]]
;  (->>
;    (get-permutations x y)
;    (remove #(= [x y] %))
;    (map #(map translate %))))
;
;(defn ->vacant-adjacent [creature all-creatures]
;  (vec
;    (set/difference
;      (set (->adjacent-cells (:position creature)))
;      (set (map :position all-creatures)))))
;
;(defn default-move [creature all-creatures]
;  (let [vacant-spaces (->vacant-adjacent creature all-creatures)]
;    (if (empty? vacant-spaces)
;      creature
;      (assoc creature :position (rand-nth vacant-spaces)))))
;
;(defmulti move (fn [creature _] :species))
;
;(defmethod move :fish [creature all-creatures]
;  (default-move creature all-creatures))
;
;(defn ->adjacent-fish [creature all-creatures]
;  (vec
;    (set/intersection
;      (set (->adjacent-cells (:position creature)))
;      (set (map :position (filter #(= :fish (:species %)) all-creatures))))))
;
;(defn eat-fish [creature all-creatures adjacent-fish]
;  (assoc
;    (default-move creature all-creatures)
;    :position (rand-nth adjacent-fish)))
;
;(defmethod move :shark [creature all-creatures]
;  (let [adjacent-fish (->adjacent-fish creature all-creatures)]
;    (cond
;      (empty? adjacent-fish) (default-move creature all-creatures)
;      :else (eat-fish creature all-creatures adjacent-fish))))