(ns top-10.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :list
 (fn [db _]
   db))

(rf/reg-sub
 :current-item
 (fn [db _]
   (:current-item db)))

(rf/reg-sub
 :items
 (fn [db _]
   (:items db)))
