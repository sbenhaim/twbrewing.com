(ns twbrewing_com.core
  (:use compojure.core
        [ring.adapter.jetty :only [run-jetty]])
  (:require [ring.util.response :as resp]
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

(defn sub-maybe [transform snip]
  (if snip (sub transform snip)
      (sub t/substitute nil)))


(t/deftemplate layout "twbrewing_com/views/layout.html" [d]
  [:#banner] (sub t/content (d :banner))
  [:#banner] (t/add-class (d :class))
  [:#content] (sub-maybe t/content (d :content))
  [:#lower] (sub-maybe t/content (d :lower))
  [(d :selected-beer)] (t/add-class "selected")
  [(d :selected-nav)] (t/add-class "selected")
  )

(t/defsnippet banner "twbrewing_com/views/content.html" [:#banner :> :*] [])
(t/defsnippet lower "twbrewing_com/views/content.html" [:#lower :> :*] [])

(t/defsnippet beer-banner "twbrewing_com/views/content.html" [:#banner-beers :> :*] [])

(defn beer-content [beer]
  (let [beer-class (keyword (str "." beer))
        beer-id (keyword (str "#" beer))]
    ((t/snippet "twbrewing_com/views/content.html" [:#content :> beer-class] []))))

(defroutes main-routes
  (GET "/" [] (render (layout {:banner (banner)
                               :lower (lower)})))

  (GET "/beers" [] (resp/redirect "/beers/short-circuit"))

  (GET "/beers/:beer" [beer] (render (layout {:banner (beer-banner)
                                              :content (beer-content beer)
                                              :class "beers"
                                              :selected-beer (keyword (str "#" beer))})))

  ;; (GET "/beers/:beer" [beer] (render-snippet (beer-content beer)))

  (GET "/news" [] (render (layout {:banner (beer-banner)})))

  (GET "/who" [] (render (layout {:banner (beer-banner)})))

  (GET "/where" [] (render (layout {:banner (beer-banner)})))

  ;; (GET "/beer-content" [] (render-snippet (content)))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))

;; (defonce server (run-jetty #'app {:port 3000 :join? false}))

