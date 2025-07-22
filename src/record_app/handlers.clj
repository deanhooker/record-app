(ns record-app.handlers
  "HTTP request handlers"
  (:require
   [clojure.string :as str]
   [record-app.records :as r]
   [record-app.state :refer [records]]
   [ring.util.response :as response]))

(defn post-record
  "Parses a post request containing a record, and adds it to the app's
  records."
  [req]
  (let [body (-> req :body slurp str/trim)]
    (try
      (let [new-record (r/parse-line body)]
        (swap! records conj new-record)
        (println "Added new record. There are now" (count @records) "records.")
        (response/response "ok"))
      (catch Exception e
       (response/bad-request
        (str "Unable to parse record: " body))))))

(defn get-records-by-color
  [_]
  (response/response
   (r/sort-by-color-then-last @records)))

(defn get-records-by-date-of-birth
  [_]
  (response/response
   (r/sort-by-date-of-birth @records)))

(defn get-records-by-last-name-desc
  [_]
  (response/response
   (r/sort-by-last-name-desc @records)))
