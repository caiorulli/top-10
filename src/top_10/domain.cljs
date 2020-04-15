(ns top-10.domain)

(def initial-value 1000)

(defn initial-rating
  [games]
  (reduce (fn [ratings game]
            (assoc ratings game initial-value))
          {}
          games))

(def k 32)

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

