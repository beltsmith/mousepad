(defproject powerplatform "0.1.0-SNAPSHOT"
  :description "Platform for Logitech PowerPlay"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot powerplatform.core
  :plugins [[lein-auto "0.1.3"]
            [lein-exec "0.3.7"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [unicode-math "0.2.0"]
                 [scad-clj "0.5.3"]])
