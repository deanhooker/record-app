(ns record-app.util-test
  (:require
   [clojure.test :refer :all]
   [record-app.util :as util])
  (:import
   [java.time LocalDate]))

(deftest parse-date-test
  (testing "Can parse a date in the format M/d/yyyy"
    (is (instance? LocalDate (util/parse-date "1/31/1991"))))
  (testing "An unparsable date throws an exception"
    (is (thrown? Exception (util/parse-date "13/13/1991")))))

(deftest display-date-test
  (testing "A LocalDate is converted into a string with the format M/d/yyyy"
    (is (= "1/31/1991"
           (util/display-date (LocalDate/of 1991 1 31))))))
