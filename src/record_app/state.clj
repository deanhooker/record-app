(ns record-app.state)

(def records (atom []))

(defn clear-records!
  "Clears the app state."
  []
  (reset! records []))
