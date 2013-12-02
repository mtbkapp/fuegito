(ns fuegito.views
  (:require [net.cgrand.enlive-html :refer :all]))

(deftemplate page "page.html" 
  [body]
  [:#content] (content body))

(defsnippet home-body "bits.html" [:#home]
  [])

;;could probably make this cleaner with some kind of macro or function
;;with a cgrand at form
(defsnippet create-form "bits.html" [:#create-form]
  [messages {:keys [title units start goal end-by] :as form}]
  [:#messages :li] (clone-for [m messages] (content m))
  [:#title] (set-attr :value title)
  [:#units] (set-attr :value units)
  [:#start] (set-attr :value start)
  [:#goal] (set-attr :value goal)
  [:#end-by] (set-attr :value end-by))

(defn home
  []
  (page (home-body)))

(defn create
  [messages form]
  (page (create-form messages form)))


