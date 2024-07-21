(ns wa-tor.move.core)

(defmulti get-move :species)


(defmethod get-move :default [{:keys [position]}]
  position)

