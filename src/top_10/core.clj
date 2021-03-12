(ns top-10.core)

(def ^:const initial-value 1000.0M)

(def ^{:dynamic true
       :doc     "K factor for ELO ratings"}
  *k*
  32)

(defn ratings
  "Creates ratings map out of entry seq."
  [entry]
  (->> entry
       (map (fn [entry] [entry initial-value]))
       (into {})))

(defn next-entries
  "Takes next 2 entries randomly."
  [ratings]
  (take 2 (shuffle (keys ratings))))

(defn apply-match
  "Applies ELO match points to ratings."
  [ratings winner loser]
  (let [p-win  (Math/pow 10 (/ (get ratings winner) 400))
        p-lose (Math/pow 10 (/ (get ratings loser) 400))

        winner-points (* *k* (- 1 (/ p-win (+ p-win p-lose))))
        loser-points  (* *k* (- (/ p-lose (+ p-win p-lose))))]

    (-> ratings
        (update winner + winner-points)
        (update loser + loser-points))))

(defn top-list
  "Lists entries from highest-rank to lowest-rank."
  [ratings]
  (keys (sort-by val > ratings)))
