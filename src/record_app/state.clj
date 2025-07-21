(ns record-app.state)

(def records (atom []))

(defn clear-records! []
  (reset! records []))
