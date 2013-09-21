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

(defn match
  [re]
  (fn [s]
    (not (nil? (re-seq re s)))))

(def date (partial parse-date "M-d-y")) 

(defn date?
  [x]
  ((complement throws?) date x))

(defn in-future?
  [d]
  (.after (date d) (Date.)))

(defn validator 
  [k pred mess]
  (fn [data]
    (when-not
     (try (pred (get data k))
       (catch Exception e false))
      mess)))

(defn checker
  [& validators]
  (fn [data]
    (reduce (fn [messages v]
              (if-let [mess (v data)]
                (conj messages mess)
                messages))
            [] validators)))

(defn count-range
  [low high]
  (fn [x]
    (let [c (count x)]
      (and (<= c high)
           (>= c low)))))

(defn only-has
  [& ks]
  (fn [data]
    (if-not
      (= (set (keys data))
         (set ks))
      "You can't included extra data!")))

(def valid-chart? 
  (checker
    (only-has "title" "units" "goal" "start" "end-by")
    (validator "title" (count-range 1 100) "I need a title")
    (validator "units" (count-range 1 100) "I need units")
    (validator "goal" (match #"^\-?\d+$") "I need a goal that is a number")
    (validator "start" (match #"^\-?\d+$") "I need a starting value that is a number")
    (validator "end-by" date? "I need an end date that is . . . a date")
    (validator "end-by" in-future? "The end date needs to be in the future")))

(def chart-parsers
  {"goal" parse-int
   "start" parse-int
   "end-by" date})

(defn parse-values
  [parsers data]
  (reduce (fn [new-data [k parser]]
            (assoc new-data k (parser (get new-data k))))
          data
          parsers))

(defn create-chart
  [chart]
  (let [id (ObjectId.)]
    (coll/insert "charts" (assoc (parse-values chart-parsers
                                             chart)
                               :_id id))
    id))


