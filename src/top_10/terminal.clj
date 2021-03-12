(ns top-10.terminal
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [top-10.core :refer [ratings
                                 next-entries
                                 apply-match
                                 top-list]])
  (:gen-class))

(defn- save-ratings-to-csv [ratings]
  (with-open [writer (io/writer "results.csv")]
    (csv/write-csv writer
                   ratings)))

(defn- entries-from-csv [filename]
  (with-open [reader (io/reader filename)]
    (doall (->> (csv/read-csv reader)
                (map first)))))

(defn- load-ratings []
  (with-open [reader (io/reader "results.csv")]
    (->> (doall (csv/read-csv reader))
         (map (fn [[k v]]
                [k (BigDecimal. v)]))
         (into {}))))

(defn- loop-message
  [entry-a entry-b]
  (str "\nWhich of these do you choose?\n"
       "1) " entry-a "\n"
       "2) " entry-b "\n"
       "3) Save and print top 10"))

(defn- top-10
  [ratings]
  (let [top-entries (take 10 (top-list ratings))]

    (->> (for [[position entry] (zipmap (range 1 11) top-entries)]
           (str position ". " entry))
         (s/join "\n")
         (str "\nOrdered top 10 entries:\n\n"))))

(defn- try-integer [input]
  (try
    (Integer. input)
    (catch Exception _ nil)))

(defn- main-loop
  [ratings]
  (let [[entry-a entry-b] (next-entries ratings)]
    (println (loop-message entry-a entry-b))
    (let [answer          (try-integer (read-line))]

      (case answer
        1 (recur (apply-match ratings entry-a entry-b))
        2 (recur (apply-match ratings entry-a entry-b))
        3 (do
            (save-ratings-to-csv ratings)
            (println "Saved ratings to results.csv file.\n")
            (println (top-10 ratings)))
        (do
          (save-ratings-to-csv ratings)
          (println "Saved ratings to results.csv file.\n"))))))

(defn -main
  [& [filename]]
  (let [ratings (if (seq filename)
                  (do
                    (println (str "Loading files from "
                                  filename "..."))
                    (ratings (entries-from-csv filename)))
                  (do
                    (println (str "Loading existing ratings..."))
                    (load-ratings)))]
    (println "Welcome to the Top 10 Chooser!")
    (main-loop ratings)))
