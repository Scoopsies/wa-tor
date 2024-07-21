(ns wa-tor.core)

(def window-size 500)
(def row-size 20)
(def pixel-size (/ window-size row-size))
(def shark-breeding 8)
(def shark-energy 6)
(def fish-breeding 3)

(defmulti create (fn [key & _] key))