(ns record-app.util
  (:import
   [java.time LocalDate]
   [java.time.format DateTimeFormatter]))

(def ^:private date-formatter
  (DateTimeFormatter/ofPattern "M/d/yyyy"))

(defn parse-date
  [date]
  (LocalDate/parse date date-formatter))

(defn display-date
  [date]
  (.format date date-formatter))
