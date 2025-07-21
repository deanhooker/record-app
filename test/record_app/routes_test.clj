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
