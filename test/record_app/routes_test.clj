(ns record-app.routes-test
  (:require
   [clojure.test :refer :all]
   [ring.mock.request :as mock]
   [record-app.routes :refer [app]]))

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
