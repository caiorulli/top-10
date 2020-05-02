(ns ^:figwheel-hooks top-10.core
  (:require [goog.dom :as gdom]
            [re-frame.core :as rf]
            [reagent.dom :as rdom]
            top-10.events
            top-10.subs
            [top-10.views :as views]))

(defn multiply [a b] (* a b))

(defn get-app-element []
  (gdom/getElement "app"))

(defn app []
  [:div.main
   [views/add-item-section]
   [views/list-items-section]
   [views/choose-between-two-section]
   [views/current-top-10]])

(defn mount [el]
  (rdom/render [app] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(rf/dispatch-sync [:initialize])

(mount-app-element)

(defn ^:after-load on-reload []
  (rf/clear-subscription-cache!)
  (mount-app-element))
