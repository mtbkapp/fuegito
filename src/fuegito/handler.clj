(ns fuegito.handler
  (:use compojure.core)
  (:require [fuegito.views :as v]
            [fuegito.storage :as s]
            [ring.util.response :as resp]
            [compojure.handler :as handler]
            [compojure.route :as route])
  (:import [java.text SimpleDateFormat]))

;; used to connect to the db from vim-fireplace
#_(s/setup-db! "mongodb://localhost/fuegito")

(def formatter (SimpleDateFormat. "yyyy-MM-dd"))

(defn is-num?
  [x]
  (not (nil? (re-seq #"^([1-9]\d*|0)$" (.trim x)))))

(defn is-date?
  [x]
  (try (.parse formatter (.trim x))
       true
       (catch Exception _ false)))

(defn valid-chart?
  [{:keys [title start-value goal-value units start-date goal-date]}]
  (println start-value)
  (println goal-value)
  (and (< (count title) 100)
       (> (count title) 0)
       (< (count units) 100)
       (> (count title) 0)
       (is-num? start-value)
       (is-num? goal-value)
       (is-date? start-date)
       (is-date? goal-date)))

(defroutes app-routes
  (GET "/" [] (v/home))
  (GET "/create" [] (v/create [] {}))
  (POST "/create" {params :params} (str (valid-chart? params)))
  #_(GET "/:id" [id] "returns the main burn down page for the id")
  #_(GET "/:id/chart" [id] "returns the image of the chart")
  #_(POST "/:id/add-data" [] "adds data then redirects back to page")
  (route/resources "/")
  (route/not-found "no hay nada"))

(def app
  (handler/site app-routes))


