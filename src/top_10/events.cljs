(ns top-10.events
  (:require [re-frame.core :as rf]
            [top-10.domain :as domain :refer [initial-value]]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:current-item ""
    :items        {}
    :choices      []
    :present      false}))

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

(rf/reg-event-db
 :generate-choices
 (fn [db]
   (assoc db :choices (domain/random-items (keys (:items db))))))

(rf/reg-event-db
 :choose-item
 (fn [db [_ item]]
   (-> db
       (assoc :items (domain/calculate-ratings item
                                               (:choices db)
                                               (:items db)))
       (assoc :choices (domain/random-items (keys (:items db)))))))

(rf/reg-event-db
 :present-top-10
 (fn [db]
   (assoc db :present true)))
