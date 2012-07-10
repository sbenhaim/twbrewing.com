(ns twbrewing_com.core
  (:use compojure.core)
  (:require [ring.util.response :as resp]
            [compojure.route :as route]
            [compojure.handler :as handler]))


(defroutes main-routes
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))
