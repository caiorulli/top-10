(ns top-10.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [for-all]]
            [top-10.core :as top-10]))

(deftest core-test
  (let [games #{"Doom Eternal"
                "Hades"
                "Dark Souls 3"
                "PUBG"}]

    (testing "ratings initialization"

      (is (= {} (top-10/ratings #{})))

      (is (= {"Doom Eternal" 1000.0M
              "Hades"        1000.0M
              "Dark Souls 3" 1000.0M
              "PUBG"         1000.0M}
             (top-10/ratings games))))

    (let [ratings (top-10/ratings games)]

      (testing "getting two random entries"
        (with-redefs [shuffle reverse]

          (is (= ["Doom Eternal" "Hades"]
                 (top-10/next-entries ratings)))))

      (let [new-ratings (-> ratings
                            (top-10/apply-match "Doom Eternal" "PUBG")
                            (top-10/apply-match "Hades" "PUBG")
                            (top-10/apply-match "Dark Souls 3" "PUBG")
                            (top-10/apply-match "Doom Eternal" "Hades"))]

        (testing "applying ratings"

          (is (= {"Doom Eternal" 1031.9660918698307
                  "Hades"        999.2976013366472
                  "Dark Souls 3" 1014.5641271217237
                  "PUBG"         954.1721796717983}
                 new-ratings)))

        (testing "getting the top 2 games"

          (is (= ["Doom Eternal" "Dark Souls 3"]
                 (take 2 (top-10/top-list new-ratings)))))))))

(defn- next-random-match
  [ratings]
  (let [entries (keys ratings)]
    (if (seq entries)
      (gen/let [winner (gen/elements entries)
                loser  (gen/elements entries)]

        (top-10/apply-match ratings winner loser))

      (gen/return ratings))))

(def gen-ratings
  (-> (gen/fmap top-10/ratings gen/string-alphanumeric)
      (gen/bind next-random-match)
      (gen/bind next-random-match)
      (gen/bind next-random-match)))

(defn- ratings-average
  [ratings]
  (let [entry-amount (count (keys ratings))
        sum          (reduce + (vals ratings))]
    (bigdec (/ sum entry-amount))))

(defspec ratings-spec 100
  (for-all [ratings gen-ratings
            :when (pos? (count ratings))]

           (is (= (ratings-average ratings) top-10/initial-value))

           (let [winners (top-10/top-list ratings)]
             (is (apply >= (map #(get ratings %) winners))))))
