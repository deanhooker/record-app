(ns record-app.records-test
  (:require
   [clojure.test :refer :all]
   [record-app.records :as records]
   [record-app.util :refer [display-date parse-date]]))

(deftest non-record-filter-test
  (let [non-record-filter #'record-app.records/non-record-filter]
    (testing "Comments are removed"
      (let [line "#This is a comment"]
        (is (false? (non-record-filter line)))))
    (testing "Empty lines are removed"
      (let [empty-line ""]
        (is (false? (non-record-filter empty-line)))))
    (testing "Non-empty and non-comment lines are not filtered"
      (let [line "This is not a comment or empty"]
        (is (non-record-filter line))))))

(deftest find-delimiter-test
  (let [find-delimiter #'record-app.records/find-delimiter]
    (testing "Pipe delimiter is found"
      (let [line "john|smith"]
        (is (= "\\|" (find-delimiter line)))))
    (testing "Comma delimiter is found"
      (let [line "john,smith"]
        (is (= "," (find-delimiter line)))))
    (testing "Pipe delimiter is found"
      (let [line "john smith"]
        (is (= "\\s+" (find-delimiter line)))))))

(deftest parse-line-test
  (let [test-record (#'record-app.records/->record
                     "America" "Andy" "andy.america@yopmail.com" "blue" "2/2/1981")]
    (testing "A good, space-delimited line is correctly parsed"
      (let [line "America Andy andy.america@yopmail.com blue 2/2/1981"]
        (is (= (records/parse-line line)
               test-record))))
    (testing "A good, comma-delimited line is correctly parsed"
      (let [line "America,Andy,andy.america@yopmail.com,blue,2/2/1981"]
        (is (= (records/parse-line line)
               test-record))))
    (testing "A good, pipe-delimited line is correctly parsed"
      (let [line "America|Andy|andy.america@yopmail.com|blue|2/2/1981"]
        (is (= (records/parse-line line)
               test-record))))
    (testing "A bad line, with too many columns, throws an exception"
      (let [line "America|Andy|andy.america@yopmail.com|blue|2/2/1981|some_additional_data"]
        (is (thrown? Exception (records/parse-line line)))))
    (testing "A bad line, with too few columns, throws an exception"
      (let [line "America|Andy|andy.america@yopmail.com|blue"]
        (is (thrown? Exception (records/parse-line line)))))))

(deftest parse-lines-test
  (testing "Commented and empty lines are filtered"
    (is (= 0 (count (records/parse-lines '("#comment" ""))))))
  (testing "Other lines are parsed into maps"
    (let [lines '("America|Andy|andy.america@yopmail.com|blue|2/2/1981"
                  "Tirekicker|Ruth|ruth.tirekicker@yopmail.com|black|2/7/1984")]
      (is (every? map? (records/parse-lines lines))))))

(deftest sort-by-color-then-last-test
  (testing "Records are sorted by color"
    (let [records '({:favorite-color "blue"}
                    {:favorite-color "black"}
                    {:favorite-color "red"})
          sorted (records/sort-by-color-then-last records)]
      (is (= (map :favorite-color sorted)
             '("black" "blue" "red")))))
  (testing "Records with matching color are sorted by last-name"
    (let [records '({:favorite-color "blue"
                     :last-name "Tirekicker"}
                    {:favorite-color "blue"
                     :last-name "America"}
                    {:favorite-color "blue"
                     :last-name "Purchaser"})
          sorted (records/sort-by-color-then-last records)]
      (is (= (map :last-name sorted)
             '("America" "Purchaser" "Tirekicker"))))))

(deftest sort-by-date-of-birth-test
  (let [records (list {:date-of-birth (parse-date "7/31/2012")}
                      {:date-of-birth (parse-date "11/19/1941")}
                      {:date-of-birth (parse-date "1/1/2000")})
        sorted (records/sort-by-date-of-birth records)]
    (is (= (map (comp display-date :date-of-birth) sorted)
           '("11/19/1941" "1/1/2000" "7/31/2012")))))

(deftest sort-by-last-name-desc-test
  (let [records '({:last-name "Tirekicker"}
                  {:last-name "America"}
                  {:last-name "Purchaser"})
        sorted (records/sort-by-last-name-desc records)]
    (is (= (map :last-name sorted)
           '("Tirekicker" "Purchaser" "America")))))

(deftest print-record-test
  (let [print-record #'record-app.records/print-record
        test-record (#'record-app.records/->record
                     "America" "Andy" "andy.america@yopmail.com" "blue" "2/2/1981")]
    (is (= (with-out-str (print-record test-record))
           "America | Andy | andy.america@yopmail.com | blue | 2/2/1981\n"))))
