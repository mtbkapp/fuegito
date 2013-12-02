(ns fuegito.storage
  (:require [monger.core :as mg]
            [monger.collection :as coll])
  (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]
           [java.text SimpleDateFormat]
           [java.util Date]))

(defn setup-db!
  [uri]
  (mg/connect-via-uri! uri)
  (mg/set-db! (mg/get-db "fuegito")))

(defn throws?
  [f & args]
  (try (apply f args) false (catch Exception e true)))

(defn parse-int
  [s]
  (Integer/parseInt s))

(defn parse-date 
  [pattern string]
  (.parse (SimpleDateFormat. pattern) string))

(defn match?
  [re]
  (fn [s]
    (not (nil? (re-seq re s)))))

