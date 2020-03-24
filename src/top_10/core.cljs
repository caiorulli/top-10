(ns ^:figwheel-hooks top-10.core
  (:require
   [goog.dom :as gdom]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(println "This text is printed from src/top_10/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

(def initial-value 1000)
(def k 32)

(defn initial-rating
  [games]
  (reduce (fn [ratings game]
            (assoc ratings game initial-value))
          {}
          games))

(defn random-games
  [games]
  (let [limit  (count games)
        rand-1 (rand-int limit)
        rand-2 (loop [new-rand (rand-int limit)]
                 (if-not (= rand-1 new-rand)
                   new-rand
                   (recur (rand-int limit))))]
    [(nth games rand-1)
     (nth games rand-2)]))

(defn game-ratings
  [[ga gb] ratings]
  [(get ratings ga)
   (get ratings gb)])

(defn probabilities
  "Given ratings A and B,
   returns probability A and B"
  [[ra rb]]
  (let [qa (Math/pow 10 (/ ra 400))
        qb (Math/pow 10 (/ rb 400))]
    [(/ qa (+ qa qb))
     (/ qb (+ qa qb))]))

(defn result-points
  "Given probabilities A and B,
   results A and B and a k-factor,
   returns point difference that
   should be applied to new ratings"
  [[ea eb] [sa sb] k-factor]
  [(* k-factor (- sa ea))
   (* k-factor (- sb eb))])

(defn new-ratings
  "Given games A and B and result points A and B,
   calculates new ratings"
  [[ga gb] [fa fb] ratings]
  (-> ratings
      (update ga + fa)
      (update gb + fb)))

(defn calculate-ratings
  [choice question-games ratings]
  (let [question-probabilities (probabilities (game-ratings question-games ratings))
        question-results       (result-points question-probabilities choice k)]
    (new-ratings question-games question-results ratings)))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 "Hello conquista!"]
   [:h3 "Edit this in src/top_10/core.cljs and watch it change!"]])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(rf/dispatch-sync [:initialize])

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (rf/clear-subscription-cache!)
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
