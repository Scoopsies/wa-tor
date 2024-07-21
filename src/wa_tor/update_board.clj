(ns wa-tor.update-board
  (:require [wa-tor.create :as c]
            [wa-tor.move :as move]
            [wa-tor.settings :as settings]))

(defn remove-eaten [old-board updated-creature]
  (remove #(= (:position updated-creature) (:position %)) old-board))

(defn dead? [old-creature]
  (= 0 (:energy old-creature)))

(defn- pregnant? [old-creature]
  (= 0 (:breeding old-creature)))

(defn- make-baby [old-creature updated-creature]
  (let [baby (c/create (:species old-creature) (:position old-creature))
        moved-creature (assoc updated-creature :breeding (:breeding baby))]
    [baby moved-creature]))

(defn sharks-first [old-board]
  (sort-by #(= (:species %) :fish) old-board))

(defmulti update-creature (fn [creature _] (:species creature)))

(defmethod update-creature :fish [creature board]
  (assoc (move/move creature board) :breeding (dec (:breeding creature))))

(defmethod update-creature :shark [creature board]
  (let [move (move/move creature board)
        fish-eaten? (some #(= (:position move) (:position %)) board)]
    (if fish-eaten?
      (assoc move :breeding (dec (:breeding creature)) :energy settings/shark-energy)
      (assoc move :breeding (dec (:breeding creature)) :energy (dec (:energy creature))))))

(defn update-board [all-creatures]
  (loop [old-board all-creatures result []]
    (let [old-board (sharks-first old-board)]
      (if (empty? old-board)
        result
        (let [both-boards (concat old-board result)
              old-creature (first old-board)
              updated-creature (update-creature old-creature both-boards)]
          (recur
            (rest (remove-eaten old-board updated-creature))
            (cond
              (dead? old-creature) result
              (pregnant? old-creature) (concat result (make-baby old-creature updated-creature))
              :else (conj result updated-creature))))))))
