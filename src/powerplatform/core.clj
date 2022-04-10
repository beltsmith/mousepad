(ns powerplatform.core
  (:require [powerplatform.build :refer [build]]))

(defn -main []
  (build :debug true))
