(ns fuegito.handler
  (:use compojure.core)
  (:require [fuegito.views :as v]
            [fuegito.storage :as s]
            [ring.util.response :as resp]
            [compojure.handler :as handler]
            [compojure.route :as route]))

#_(defn to-keyword-keys
  [m]
  (apply hash-map (flatten (map (fn [[k v]] [(keyword k) v]) m))))

#_(s/setup-db! "mongodb://localhost/fuego")

(defroutes app-routes
  (GET "/" [] (v/home))
  (GET "/create" [] (v/create [] {}))
  (POST "/create" [] "do create")
  #_(GET "/:id" [id] "returns the main burn down page for the id")
  #_(GET "/:id/chart" [id] "returns the image of the chart")
  #_(POST "/:id/add-data" [] "adds data then redirects back to page")
  (route/resources "/")
  (route/not-found "no hay nada"))

(def app
  (handler/site app-routes))


