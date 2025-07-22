(ns record-app.core
  "CLI application for sorting and viewing records within text files."
  (:require
   [clojure.java.io :as io]
   [record-app.records :as records])
  (:gen-class))

(defn- display-help []
  (println "Usage: clojure -M:cli <filenames>")
  (System/exit 1))

(defn- read-file
  "Reads a file and returns a sequence of records."
  [file-path]
  (let [file (io/file file-path)]
    (if (.exists file)
      (-> file
          io/reader
          line-seq
          records/parse-lines)
      (do (println "Error: file does not exist:" file-path)
        (System/exit 1)))))

(defn -main
  "Entry point for the CLI app. Reads the provided files and displays
  the records."
  [& file-paths]
  (if (empty? file-paths)
    (display-help)
    (let [parsed-records (->> file-paths
                              (map read-file)
                              (apply concat))
          view1 (records/sort-by-color-then-last parsed-records)
          view2 (records/sort-by-date-of-birth parsed-records)
          view3 (records/sort-by-last-name-desc parsed-records)]

      (println "\nOutput 1: Sorted by favorite color, then last name")
      (records/print-records view1)

      (println "\nOutput 2: Sorted by birth date")
      (records/print-records view2)

      (println "\nOutput 3: Sorted by last name descending")
      (records/print-records view3)

      )))
