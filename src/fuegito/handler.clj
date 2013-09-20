(ns fuegito.handler
  (:use compojure.core)
  (:require [fuegito.views :as v]
            [compojure.handler :as handler]
            [compojure.route :as route]))


(defroutes app-routes
  (GET "/" [] (v/home)) 
  (GET "/create" [] (v/create [] {})) 
  (POST "/create" [] "recieves the burn down form and redirects")
  (GET "/:id" [id] "returns the main burn down page for the id")
  (GET "/:id/chart" [id] "returns the image of the chart")
  (POST "/:id/add-data" [] "adds data then redirects back to page")
  (route/resources "/")
  (route/not-found "No hay nada"))

(def app
  (handler/site app-routes))
