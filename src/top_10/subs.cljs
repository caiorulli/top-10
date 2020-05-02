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

(rf/reg-sub
 :choices
 (fn [db _]
   (:choices db)))

(rf/reg-sub
 :show-top-10
 (fn [db _]
   (:present-top-10 db)))

(rf/reg-sub
 :top-10
 (fn [db _]
   (let [sorted    (sort-by val > (:items db))
         top-games (->> sorted
                       (take 10)
                       (map first))]
    (->> (for [[position game] (map list (range 1 11) top-games)]
           (str position ". " game))
         (s/join "\n")
         (str "\nOrdered top 10 games:\n\n")))))

