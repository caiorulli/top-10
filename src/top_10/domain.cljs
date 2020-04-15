(ns top-10.domain)

(def initial-value 1000)

(def k 32)

(defn random-items
  [items]
  (let [limit  (count items)
        rand-1 (rand-int limit)
        rand-2 (loop [new-rand (rand-int limit)]
                 (if-not (= rand-1 new-rand)
                   new-rand
                   (recur (rand-int limit))))]
    [(nth items rand-1)
     (nth items rand-2)]))

(defn- probabilities
  "Given ratings A and B,
   returns probability A and B"
  [[option-a option-b] items]
  (let [rating-a (get items option-a)
        rating-b (get items option-b)
        qa       (Math/pow 10 (/ rating-a 400))
        qb       (Math/pow 10 (/ rating-b 400))]
    [(/ qa (+ qa qb))
     (/ qb (+ qa qb))]))

(defn- choice-tuple
  [choice options]
  (if (= choice (first options))
    [1 0]
    [0 1]))

(defn- result-points
  "Given probabilities A and B,
   results A and B and a k-factor,
   returns point difference that
   should be applied to new ratings"
  [[expected-a expected-b]
   [actual-a actual-b]
   k-factor]
  [(* k-factor (- actual-a expected-a))
   (* k-factor (- actual-b expected-b))])

(defn- new-items
  "Given items A and B and result points A and B,
   calculates new ratings"
  [[item-a item-b] [result-a result-b] items]
  (-> items
      (update item-a + result-a)
      (update item-b + result-b)))

(defn calculate-ratings
  [choice options items]
  (let [results (result-points (probabilities options items)
                               (choice-tuple choice items)
                               k)]
    (new-items options results items)))

