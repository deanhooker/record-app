(ns record-app.middleware
  ;; (:require [clojure.java.io :as io])
  )

(defn wrap-slurp-body
  "Reads request body into a string and associates it at :string-body."
  [handler]
  (fn [req]
    (let [string-body (slurp (:body req))]
      (handler (assoc req :string-body string-body)))))
