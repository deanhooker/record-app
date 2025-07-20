(ns record-app.records
  (:require
   [clojure.string :as str]
   [record-app.util :refer [display-date parse-date]]))

(defn- non-record-filter
  "Filters comments and empty lines."
  [line]
  (not (or (.startsWith line "#") ; comment
           (empty? line))))       ; empty line

(defn- find-delimiter
  [line]
  (cond
    (str/includes? line "|") "\\|"
    (str/includes? line ",") ","
    :else                    "\\s+"))

(defn- ->record
  [last-name first-name email favorite-color date-of-birth]
  {:last-name last-name
   :first-name first-name
   :email email
   :favorite-color favorite-color
   :date-of-birth (parse-date date-of-birth)})

(defn- parse-line
  [line]
  (let [delimiter (find-delimiter line)
        tokens (->> (str/split line (re-pattern delimiter))
                    (map str/trim))]
    (if (= 5 (count tokens))
      (apply ->record tokens)
      (throw (ex-info "Invalid record format"
                      {:line line :tokens tokens})))))

(defn parse-lines
  "Returns a collection of records."
  [lines]
  (->> lines
       (filter non-record-filter)
       (map parse-line)))

(defn sort-by-color-then-last
  [records]
  (sort-by (juxt :favorite-color :last-name) records))

(defn sort-by-date-of-birth
  [records]
  (sort-by :date-of-birth records))

(defn sort-by-last-name-desc
  [records]
  (sort-by :last-name #(compare %2 %1) records))

(defn- print-record
  [{:keys [last-name first-name email favorite-color date-of-birth]}]
  (println
   (str/join " | "
             [last-name first-name email favorite-color (display-date date-of-birth)])))

(defn print-records
  "Prints the records in a readable format."
  [records]
  (doseq [rec records]
    (print-record rec)))
