(ns twbrewing_com.core
  (:use compojure.core
        [ring.adapter.jetty :only [run-jetty]])
  (:require [ring.util.response :as resp]
            [compojure.route :as route]
            [net.cgrand.enlive-html :as t]
            [hiccup.core :as h]
            [compojure.handler :as handler]))

(def *content-template* "twbrewing_com/views/content.html")

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


(defn select [selector]
  (t/select (t/html-resource *content-template*) selector))


(defn sub-or-keep [transform stuff]
  (if stuff (sub-maybe transform stuff) identity))


(t/deftemplate layout "twbrewing_com/views/layout.html" [d]
  [:#banner] (sub t/substitute (d :banner))
  [:#banner] (t/add-class (d :class))
  [:#content] (sub-maybe t/content (d :content))
  [:#lower] (sub-maybe t/content (d :lower))
  [(d :selected-beer)] (t/add-class "selected")
  [(d :selected-nav)] (t/add-class "selected")
  [:#banner :div.h1] (t/html-content (d :banner-text))
  [:#right] (sub-or-keep t/content (d :right))
  [:.twitter-widget] (t/substitute (select [:#connect-twitter]))
  )

(defn banner [page]
  (select [(keyword (str "#banner." page))]))


(defn content [page]
  (select [(keyword (str "#content." page)) :> :*]))

(t/defsnippet lower "twbrewing_com/views/content.html" [:#lower :> :*] [])

(t/defsnippet beer-banner "twbrewing_com/views/content.html" [:#banner-beers :> :*] [])

(defn beer-content [beer]
  (let [beer-class (keyword (str "." beer))
        beer-id (keyword (str "#" beer))]
    ((t/snippet "twbrewing_com/views/content.html" [:#content :> beer-class] []))))

(defroutes main-routes
  (GET "/" [] (render (layout {:banner (banner "home")
                               :lower (lower)})))

  (GET "/beers" [] (resp/redirect "/beers/short-circuit"))

  (GET "/beers/:beer" [beer] (render (layout {:banner (banner "beers")
                                              :content (beer-content beer)
                                              :class "beers"
                                              :selected-nav :#nav-beers
                                              :selected-beer (keyword (str "#" beer))})))

  ;; (GET "/beers/:beer" [beer] (render-snippet (beer-content beer)))

  (GET "/news" [] (render (layout {:banner (banner "general")
                                   :banner-text "The Brew's News"
                                   :content (content "news")
                                   :right (select [#{:#connect-twitter :#connect-other}])
                                   :selected-nav :#nav-news})))

  (GET "/who" [] (render (layout {:banner (banner "who")
                                  :content (content "who")
                                  :selected-nav :#nav-who})))

  (GET "/where" [] (render (layout {:banner (banner "where")
                                  :content (content "where")
                                  :selected-nav :#nav-where})))

  (GET "/blw" [] (resp/redirect "/coming-soon"))

  (GET "/coming-soon" [] (render (layout {:banner (banner "general")
                                          :banner-text "Coming Soon"
                                          :content (content "coming-soon")
                                          :selected-nav :#blw-tab})))

  ;; (GET "/beer-content" [] (render-snippet (content)))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))

;; (defonce server (run-jetty #'app {:port 3000 :join? false}))

