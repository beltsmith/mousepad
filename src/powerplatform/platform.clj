(ns powerplatform.platform
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [unicode-math.core :refer :all]))

(defn half [x] (/ x 2))

(def mount-diameter 30)

; make cutout z = 0, negative z is solid base

(def pad-height 5)
(def base-height 10)

(def pad-width 343)
(def pad-depth 285)

(def connector-depth 35)
(def connector-width 135)
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
(def mount-hole-height (* 1 platform-height))
(def mount-top-y (- 0 connector-depth radius))
(def mount-width mount-diameter)

(defn hole [d h]
  (cylinder (half d) h :center false))

(defn rounded-corner [x y]
  (translate [x y 0] (circle radius)))

(defn rounded-rectangle [width height depth]
  (let [x-right (- (half width) radius)
        x-left (- x-right)
        y-top radius
        y-bottom (+ y-top depth)]
    (extrude-linear {:height height :center false}
                    (hull
                     (rounded-corner x-left y-top)
                     (rounded-corner x-right y-top)
                     (rounded-corner x-right y-bottom)
                     (rounded-corner x-left y-bottom)))))

(defmacro connector-translate [& body]
  (let [x (half (- pad-width connector-width))
        y (- 0 connector-depth 24)
        z 0]
    `(translate [~x ~y ~z] ~@body)))

(defmacro center [& body]
  `(translate [0 0 ~(- base-height)] ~@body))

(def platform-case
  (center
   (hull
    (translate [(- mount-width) mount-top-y 0]
               (hole mount-diameter platform-height))
    (rounded-rectangle
     platform-width
     platform-height
     platform-depth)
    (connector-translate
     (rounded-rectangle
      (+ connector-width perimeter-side)
      connector-height
      (+ connector-depth perimeter-top))))))

(def pad-cutout
  (union
   (translate [0 perimeter-top 0]
              (rounded-rectangle pad-width pad-height pad-depth))
   (connector-translate
    (rounded-rectangle connector-width connector-height pad-depth))))

(def hole-cutout
  (center
   (translate [0 (- mount-distance) 0]
              (hole mount-diameter mount-hole-height))))

(def keytray-cutout
  hole-cutout)

(def platform
  (-> platform-case
      (difference hole-cutout)
      (difference pad-cutout)
      (difference keytray-cutout)))

(spit "things/powerplatform.scad"
      (write-scad platform))

(defn -main [dum] 1)  ; dummy to make it easier to batch
