(defproject top-10 "0.1.0-SNAPSHOT"
  :description "Choose your top 10 something!"
  :url "http://github.com/caiorulli/top-10"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "0.1.4"]]
  :main ^:skip-aot top-10.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
