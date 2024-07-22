(ns wa-tor.gui-spec
  (:require [quil.core :as q]
            [speclj.core :refer :all]
            [wa-tor.gui :as sut]
            [wa-tor.create :as c]
            [wa-tor.update-board :as b]))

(describe "gui"
  (with-stubs)
  (redefs-around [q/defsketch (stub :defsketch)])

  (context "get-color"
    (it "returns to proper color for fish"
      (should= [0 205 0] (sut/get-color (c/create :fish))))

    (it "returns the proper color a shark"
      (should= [205 0 0] (sut/get-color (c/create :shark))))

    (it "defaults to blue"
      (should= [0 0 205] (sut/get-color {:species :something-weird})))
    )

  (context "shape-creature"
    (redefs-around [q/rect (stub :rect)])
    (it "invokes rect with correct position"
      (sut/shape-creature {:position [0 0]})
      (should-have-invoked :rect {:with [0 0 25 25] :times 1})))

  (context "color-creature"
    (redefs-around [q/fill (stub :fill)])
    (it "calls q/fill with color of fish"
      (sut/color-creature (c/create :shark))
      (should-have-invoked :fill {:with (sut/get-color (c/create :shark))}))

    (it "calls q/fill with color of fish"
      (sut/color-creature (c/create :fish))
      (should-have-invoked :fill {:with (sut/get-color (c/create :fish))}))
    )

  (context "draw-creature"
    (redefs-around [sut/color-creature (stub :color-creature) sut/shape-creature (stub :shape-creature)])
    (it "calls color creature 1 time."
      (sut/draw-creature (c/create :shark))
      (should-have-invoked :color-creature))

    (it "calls shape-creature 1 time."
      (sut/draw-creature (c/create :fish))
      (should-have-invoked :shape-creature)))

  (context "setup"
    (redefs-around [q/frame-rate (stub :frame-rate) c/create (stub :create)])
    (it "calls frame-rate 1 time."
      (sut/setup) (should-have-invoked :frame-rate))

    (it "calls create 1 time."
      (sut/setup) (should-have-invoked :create)))

  (context "draw-board"
    (redefs-around [q/background (stub :background)
                    run! (stub :run!)])
    (it "calls background 1 time"
      (sut/draw-board [])
      (should-have-invoked :background))

    (it "calls run! 1 time"
      (sut/draw-board [])
      (should-have-invoked :run!)))

  (context "draw"
    (redefs-around [sut/draw-board (stub :draw-board)])
    (it "calls draw-board 1 time."
      (sut/draw (c/create :shark))
      (should-have-invoked :draw-board)))

  #_(context "next-state"
    (redefs-around [b/update-board (stub :update-board)])
    (it "calls draw-board 1 time."
      (sut/next-state [(c/create :fish)])
      (should-have-invoked :update-board)))
  )