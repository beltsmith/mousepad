(ns powerplatform.platform
  (:refer-clojure :exclude [use import])
  (:require
   [scad-clj.model :refer :all]
   [unicode-math.core :refer :all]))

(defn half [x] (/ x 2))

(def mount-diameter 30)

; make cutout z = 0, negative z is solid base

(def pad-height 5)
(def base-height 8)

(def pad-width 343)
(def pad-depth 285)

(def connector-depth 35)
(def connector-width 130)
(def connector-height (+ pad-height base-height))

(def radius 10)

(def perimeter-left 5)
(def perimeter-right 5)
(def perimeter-side (+ perimeter-left perimeter-right))
(def perimeter-bottom 5)
(def perimeter-top 5)

(def platform-height (+ pad-height base-height))
(def platform-depth (+ pad-depth perimeter-bottom perimeter-top))
(def platform-width (+ pad-width perimeter-side))

(def mount-radius (half mount-diameter))
(def mount-offset 10)
(def mount-distance (+ mount-radius mount-offset))
;; (def mount-hole-height (* 1 platform-height))
(def mount-top-y (- 0 connector-depth radius))
(def mount-width mount-diameter)
(def mount-height 27)
(def mount-thickness 2)

(defn hole [d h]
  (cylinder (half d) h :center false))

(defn rounded-corner [x y]
  (translate [x y 0] (circle radius)))

(defmacro extrude-hull [height & body]
  `(extrude-linear {:height ~height :center false}
                   (hull ~@body)))

(defn rounded-rectangle [width height depth]
  (let [x-right (- (half width) radius)
        x-left (- x-right)
        y-top radius
        y-bottom (- (+ y-top depth) (* 2 radius))]
    (extrude-hull height
                  (rounded-corner x-left y-top)
                  (rounded-corner x-right y-top)
                  (rounded-corner x-right y-bottom)
                  (rounded-corner x-left y-bottom))))

(defmacro connector-translate [& body]
  (let [x (half (- pad-width connector-width))
        y (- 0 connector-depth 1)
        z 0]
    `(translate [~x ~y ~z] ~@body)))

(defmacro mount-translate [& body]
  (let [x (- mount-width)
        y mount-top-y
        z 0]
    `(translate [~x ~y ~z] ~@body)))

(defmacro center [& body]
  `(translate [0 0 ~(- base-height)] ~@body))

;; (def platform (rounded-rectangle platform-width platform-height platform-depth))
(def connector-holder
  (let [connector-holder-width (+ connector-width perimeter-side)
        connector-holder-depth (+ connector-depth perimeter-top)]
    (rounded-rectangle connector-holder-width connector-height connector-holder-depth)))

(def mount-hole (hole mount-diameter platform-height))
(def mount
  (translate [0 (- mount-distance) (- base-height)]
             (difference
              (hole (+ mount-diameter (* 2 mount-thickness)) mount-height)
              (hole mount-diameter mount-height))))

(def platform-case
  (center
   (hull
    (mount-translate mount-hole)
    (rounded-rectangle platform-width platform-height platform-depth)
    (connector-translate connector-holder))))

(def pad (rounded-rectangle pad-width pad-height pad-depth))
(def pad-cutout
  (union
   (translate [0 perimeter-top 0] pad)
   (connector-translate
    (translate [0 -7 0] ;; add cutout for cable
               (rounded-rectangle connector-width connector-height pad-depth)))))

(def hole-cutout
  (center
   (translate [0 (- mount-distance) 0]
              (hole mount-diameter mount-height))))

; width 343 / 2 = 171.5
; depth 285 / 2 = 142.5
(def keytray-cutout
  (translate [100 240 -10]
             (rounded-rectangle 180 platform-height 60)))

(def platform
  (union
   (difference platform-case pad-cutout hole-cutout)
   mount))
