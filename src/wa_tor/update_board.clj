(ns wa-tor.update-board
  (:require [wa-tor.create :as c]
            [wa-tor.update-creature :as uc]))

(defn- filter-eaten-fish [old-list result]
  (let [updated-creature (uc/update-creature (first old-list) (concat old-list result))
        {:keys [position]} updated-creature]
    (if (= position (:position (first old-list)))
      old-list
      (remove #(= position (:position %)) old-list))))

(defn- in-labor? [current-creature updated-creature]
  (let [{:keys [breeding position]} current-creature]
    (and (zero? breeding) (not= position (:position updated-creature)))))

(defn- make-baby [current-creature current-list]
  (let [updated-creature (uc/update-creature current-creature current-list)
        {:keys [species position]} current-creature]
    [updated-creature (c/create species position)]))

(defn- dead? [current-creature]
  (= 0 (:energy current-creature)))

(defn- handle-life-cycle [old-list result]
  (let [current-creature (first old-list) current-list (concat old-list result)
        updated-creature (uc/update-creature current-creature current-list)]
    (cond
      (dead? current-creature) result
      (in-labor? current-creature updated-creature) (concat result (make-baby current-creature current-list))
      :else (conj (vec result) updated-creature))))

(defn update-board
  ([creature-list] (update-board creature-list []))

  ([old-list result]
   (if (empty? old-list)
     result
     (recur (rest (filter-eaten-fish old-list result))
            (handle-life-cycle old-list result)))))