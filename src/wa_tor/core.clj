(ns wa-tor.core)

(def window-size 500)
(def row-size 20)
(def pixel-size (/ window-size row-size))

(defmulti create (fn [key & _] key))