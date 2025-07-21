(ns record-app.handlers
  "HTTP request handlers"
  (:require
   [record-app.records :as r]
   [record-app.state :refer [records]]
   [ring.util.response :as response]))

(defn post-record
  "Parses a post request containing a record, and adds it to the app's
  records."
  [req]
  (try
    (let [new-record (-> req
                         :string-body
                         r/parse-line)]
      (swap! records conj new-record)
      (println "Added new record. There are now" (count @records) "records.")
      (response/response "ok"))
    (catch Exception e
      (response/bad-request req))))
