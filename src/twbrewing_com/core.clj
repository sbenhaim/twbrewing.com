(ns twbrewing_com.core
  (:use compojure.core)
  (:require [ring.util.response :as resp]
            [ring.adapter.jetty]
            [compojure.route :as route]
            [net.cgrand.enlive-html :as t]
            [hiccup.core :as h]
            [compojure.handler :as handler]))

(defn render [ss]
  (apply str ss))

(defn render-snippet [ss]
  (-> ss t/emit* render))

(defn sub [transform snip]
  (-> snip
      t/emit*
      render
      t/html-snippet
      transform))


(t/deftemplate index "twbrewing_com/views/index.html" [d]
  [:#banner] (sub t/substitute (d :banner))
  [:#lower] (sub t/substitute (d :lower))
  )

(t/defsnippet banner "twbrewing_com/views/index.html" [:#banner] [])
(t/defsnippet lower "twbrewing_com/views/index.html" [:#lower] [])

(t/defsnippet beer-banner "twbrewing_com/views/beer.html" [:#banner] [])
(t/defsnippet content "twbrewing_com/views/beer.html" [:#content] [])

(defroutes main-routes
  (GET "/" [] (render (index {:banner (banner)
                              :lower (lower)})))

  (GET "/beer" [] (render (index {:banner (beer-banner)
                                  :lower (content)})))

  (GET "/news" [] (render (index {:banner (beer-banner)
                                  :lower (content)})))

  (GET "/who" [] (render (index {:banner (beer-banner)
                                  :lower (content)})))

  (GET "/where" [] (render (index {:banner (beer-banner)
                                  :lower (content)})))

  (GET "/beer-content" [] (render-snippet (content)))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))
