(ns top-10.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as s]))

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

(defn save-ratings-to-csv [ratings]
  (with-open [writer (io/writer "results.csv")]
    (csv/write-csv writer
                   ratings)))

(defn games-from-csv [filename]
  (with-open [reader (io/reader filename)]
    (doall (->> (csv/read-csv reader)
                (map first)))))

(defn load-ratings []
  (with-open [reader (io/reader "results.csv")]
    (into {} (doall (csv/read-csv reader)))))

(defn loop-message
  [[ga gb]]
  (str "\nWhich of these games is better?\n"
       "1) " ga "\n"
       "2) " gb "\n"
       "3) Save current ratings\n"
       "4) Print top 10 and finish"))

(defn top-10
  [ratings]
  (let [sorted (sort-by val > ratings)]
    (->> sorted
         (take 10)
         (map first)
         (s/join "\n")
         (str "\nOrdered top 10 games:\n\n"))))

(defn calculate-ratings
  [choice question-games ratings]
  (let [question-ratings       (game-ratings question-games
                                             ratings)
        question-probabilities (probabilities question-ratings)
        question-results       (result-points question-probabilities
                                              choice
                                              k)]
    (new-ratings question-games
                 question-results
                 ratings)))

(defn main-loop
  [ratings]
  (let [question-games (random-games (keys ratings))]
    (println (loop-message question-games))
    (let [answer (Integer. (read-line))]
      (case answer
        1 (recur (calculate-ratings [1 0]
                                    question-games
                                    ratings))
        2 (recur (calculate-ratings [0 1]
                                    question-games
                                    ratings))
        3 (do
            (save-ratings-to-csv ratings)
            (println "Saved ratings to results.csv file.")
            (recur ratings))
        4 (println (top-10 ratings))))))

(defn -main
  [& [filename]]
  (let [ratings (if (seq filename)
                  (do
                    (println (str "Loading files from "
                                  filename "..."))
                    (initial-rating (games-from-csv filename)))
                  (do
                    (println (str "Loading existing ratings..."))
                    (load-ratings)))]
    (println "Welcome to the Top 10 Chooser!")
    (main-loop ratings)))
