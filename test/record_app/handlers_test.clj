(ns record-app.handlers-test
  (:require
   [clojure.test :refer :all]
   [record-app.handlers :as handlers]
   [record-app.records]
   [record-app.state :refer [clear-records! records]])
  (:import
   [java.io ByteArrayInputStream]))

;; Initialize
;; ==================================================================

;; Records to initialize app state
(def andy
  (#'record-app.records/->record
   "America" "Andy" "andy.america@yopmail.com" "blue" "2/2/1981"))
(def ruth
  (#'record-app.records/->record
   "Tirekicker" "Ruth" "ruth.tirekicker@yopmail.com" "black" "2/7/1984"))

;; Set state to include 2 records for each test
(use-fixtures :each (fn [test-fn]
                      (clear-records!)
                      (swap! records conj andy)
                      (swap! records conj ruth)
                      (test-fn)))

(defn string->stream [s]
  (ByteArrayInputStream. (.getBytes s)))

;; Tests
;; ==================================================================
(deftest post-record-test
  (testing "posting a good record adds a record to the app state"
    (let [record "Homeowner John john.homeowner@yopmail.com white 1/1/1980"
          request {:body (string->stream record)}
          response (handlers/post-record request)]
      (is (= "ok" (:body response)))
      (is (= 3 (count @records)))
      (is (every? map? @records)))))

(deftest post-record-bad-test
  (testing "posting a bad record returns 400 and doesn't alter app state"
    (let [record "bad record"
          request {:body (string->stream record)}
          response (handlers/post-record request)]
      (is (= "Unable to parse record: bad record"
             (:body response)))
      (is (= 2 (count @records))))))

(deftest get-records-by-color-test
  (testing "get records returns two records and the first is ruth"
    (let [response (:body (handlers/get-records-by-color nil))]
             (is (= 2 (count response)))
             (is (= ruth (first response))))))

(deftest get-records-by-date-of-birth-test
  (testing "get records returns two records and the first is andy"
    (let [response (:body (handlers/get-records-by-date-of-birth nil))]
      (is (= 2 (count response)))
      (is (= andy (first response))))))

(deftest get-records-by-last-name-desc-test
  (testing "get records returns two records and the first is ruth"
    (let [response (:body (handlers/get-records-by-last-name-desc nil))]
      (is (= 2 (count response)))
      (is (= ruth (first response))))))
