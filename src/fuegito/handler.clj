(ns fuegito.handler
  (:use compojure.core)
  (:require [fuegito.views :as v]
            [fuegito.storage :as s]
            [ring.util.response :as resp]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn to-keyword-keys
  [m]
  (apply hash-map (flatten (map (fn [[k v]] [(keyword k) v]) m))))

(defroutes app-routes
  (GET "/" [] (do (s/setup-db!) (v/home)))
  (GET "/create" [] (v/create [] {})) 
  (POST "/create" {:keys [form-params]}
        (let [messages (s/valid-chart? form-params)]
          (if (empty? messages)
            (resp/redirect-after-post (str (s/create-chart form-params)))
            (v/create messages (to-keyword-keys form-params)))))
  (GET "/:id" [id] "returns the main burn down page for the id")
  (GET "/:id/chart" [id] "returns the image of the chart")
  (POST "/:id/add-data" [] "adds data then redirects back to page")
  (route/resources "/")
  (route/not-found "No hay nada"))

(def app
  (handler/site app-routes))


