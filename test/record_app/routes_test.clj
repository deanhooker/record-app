(ns record-app.routes-test
  (:require
   [cheshire.core :as json]
   [clojure.test :refer :all]
   [ring.mock.request :as mock]
   [record-app.records]
   [record-app.routes :refer [app]]
   [record-app.state :refer [clear-records! records]]))

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

;; Tests
;; ==================================================================

(deftest ping-test
  (testing "ping returns pong"
    (let [response (app (mock/request :get "/ping"))]
             (is (= 200 (:status response)))
             (is (= "pong\n" (:body response))))))

(deftest post-record-test
  (testing "posting a valid, comma-delimited record"
    (let [response (app (-> (mock/request :post "/record")
                            (mock/body "America,Andy,andy.america@yopmail.com,blue,2/2/1981")))]
      (is (= 200 (:status response)))))
  (testing "posting a valid, space-delimited record"
    (let [response (app (-> (mock/request :post "/record")
                            (mock/body "America Andy andy.america@yopmail.com blue 2/2/1981")))]
      (is (= 200 (:status response)))))
  (testing "posting a valid, pipe-delimited record"
    (let [response (app (-> (mock/request :post "/record")
                            (mock/body "America|Andy|andy.america@yopmail.com|blue|2/2/1981")))]
      (is (= 200 (:status response)))))

  (testing "posting an invalid record"
    (let [response (app (-> (mock/request :post "/record")
                            (mock/body "this is invalid")))]
      (is (= 400 (:status response))))))

(deftest get-records-by-color-test
  (testing "GET returns 200 and valid json body with 2 records"
    (let [response (app (mock/request :get "/records/color"))
          body (json/parse-string (slurp (:body response)) true)]
      (is (= "application/json; charset=utf-8"
             (get-in response [:headers "Content-Type"])))
      (is (= 200 (:status response)))
      (is (= 2 (count body)))
      (is (every? map? body)))))

(deftest get-records-by-date-of-birth-test
  (testing "GET returns 200 and valid json body with 2 records"
    (let [response (app (mock/request :get "/records/birthdate"))
          body (json/parse-string (slurp (:body response)) true)]
      (is (= "application/json; charset=utf-8"
             (get-in response [:headers "Content-Type"])))
      (is (= 200 (:status response)))
      (is (= 2 (count body)))
      (is (every? map? body)))))

(deftest get-records-by-last-name-desc-test
  (testing "GET returns 200 and valid json body with 2 records"
    (let [response (app (mock/request :get "/records/name"))
          body (json/parse-string (slurp (:body response)) true)]
      (is (= "application/json; charset=utf-8"
             (get-in response [:headers "Content-Type"])))
      (is (= 200 (:status response)))
      (is (= 2 (count body)))
      (is (every? map? body)))))
