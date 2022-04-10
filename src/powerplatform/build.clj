(ns powerplatform.build
  (:require [scad-clj.model :refer [difference]]
            [scad-clj.scad :refer [write-scad]]
            [powerplatform.platform :refer [platform keytray-cutout mount hole-cutout]]
            [clojure.string :as string]))

(defn print-sep []
  (println (string/join "" (take 40 (repeat "#")))))

(defn spit-scad [model file & {:keys [debug]}]
  (let [filename (format "things/%s.scad" file)
        data (write-scad model)]
    (when debug
      (println (format "DEBUG %s" filename))
      (print-sep)
      (println data)
      (print-sep))
    (spit filename data)))

(defn build [& {:keys [debug] :or {debug false}}]
  (print-sep)
  (spit-scad platform "powerplatform" :debug debug))
