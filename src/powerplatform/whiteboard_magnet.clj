(ns powerplatform.whiteboard-magnet
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [unicode-math.core :refer :all]
            [clojure.math.combinatorics :refer [cartesian-product]]))

(defn half [x] (/ x 2))
(defn hole [d h]
  (cylinder (half d) h :center false))

;  2 × √(r2 − d2)
;  (w/2)^2 = r^2 - d^2
;  d^2 = r^2 - (w/2)^2
;  d = sqrt(r^2 - (w/2)^2)
(defn rounded-tope [r w]
  (let [h (Math/sqrt (- (Math/pow r 2) (Math/pow (/ w 2) 2)))
        d (* r 2)
        x (- d h)]
    (translate [0 0 (- h)]
               (difference
                (sphere r)
                (translate [0 0 (- h r)]
                           (cube d d d))))))

(def magnet-diameter 13)
(def magnet-radius (half magnet-diameter))
(def magnet-height 5)
(def nob-height 6)
(def nob-bottom-radius 8)
(def nob-top-radius 12)

(def magnet-cutout
  (hole magnet-diameter magnet-height))

(def magnet
  (hole (- magnet-diameter 1) 2))

(def nob
  (cylinder [nob-bottom-radius nob-top-radius] nob-height :center false))

(def nob-cap
  (union
   (rounded-tope (* 2 nob-top-radius) (* 2 (+ 1 nob-top-radius)))
   (translate [0 0 (- 2)]
              magnet)))

(def whiteboard-magnet
  (-> nob
      (difference (translate [0 0 (- nob-height magnet-height)] magnet-cutout))
      (minkowski (sphere 0.25))))

(spit "things/whiteboard-magnet.scad"
      (write-scad whiteboard-magnet))

(spit "things/top.scad"
      (write-scad nob-cap))

(defn -main [dum] 1)  ; dummy to make it easier to batch
