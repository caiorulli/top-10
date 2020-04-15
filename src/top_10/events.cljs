(ns top-10.events
  (:require [top-10.domain :refer [initial-value]]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:current-item ""
    :items {}}))

(rf/reg-event-db
 :change-current-item
 (fn [db [_ item]]
   (assoc db :current-item item)))

(rf/reg-event-db
 :add-item
 (fn [db]
   (-> db
       (update :items assoc (:current-item db) initial-value)
       (assoc :current-item ""))))

(rf/reg-event-db
 :remove-item
 (fn [db [_ item]]
   (update db :items dissoc item)))
