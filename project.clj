(defproject twbrewing_com "0.1.0-SNAPSHOT"
  :description "twbrewing.com website"
  :url "http://twbrewing.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[compojure "1.1.0"]
                 [org.clojure/clojure "1.4.0"]
                 [enlive "1.0.1"]
                 [hiccup "1.0.0"]
                 [ring "1.1.1"]]
  ;; :profiles {:dev {:dependencies [[ring-serve "0.1.1"]]}}
  :plugins [[lein-ring "0.7.1"]]
  :ring {:handler twbrewing_com.core/app})
