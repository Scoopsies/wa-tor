(ns wa-tor.gui
  (:require [wa-tor.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]
            [wa-tor.create :as c]
            [wa-tor.update-board :as b]))

(def species-colors
  {:fish  [0 205 0]
   :shark [205 0 0]})

(defn get-color [{:keys [species] :as _creature}]
  (get species-colors species [0 0 205]))

(defn shape-creature [creature]
  (let [[x y] (:position creature)]
    (q/rect
      (* core/pixel-size x)
      (* core/pixel-size y)
      core/pixel-size
      core/pixel-size)))

(defn color-creature [creature]
  (let [[r g b] (get-color creature)]
    (q/fill r g b)))

(defn draw-creature [creature]
  (color-creature creature)
  (shape-creature creature))

(defn setup []
  (q/frame-rate 2)
  (c/create :board 20 50))

(defn draw-board [state]
  (q/background 0 0 205)
  (run! draw-creature state))

(defn draw [state]
  (draw-board state))

(defn next-state [state]
  (b/update-board state))

(declare wa-tor)

(defn -main []
  (q/defsketch wa-tor
    :title "Wa-tor"
    :settings #(q/smooth 2)
    :setup setup
    :draw draw
    :update next-state
    :middleware [m/fun-mode]
    :size [core/window-size core/window-size]))