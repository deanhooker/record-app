(ns record-app.routes
  (:require
   [record-app.handlers :as handlers]
   [record-app.middleware :refer [wrap-slurp-body]]
   [reitit.ring :as ring]))

(def app
  (ring/ring-handler
   (ring/router
    [["/ping" {:get (fn [_] {:status 200 :body "pong\n"})}]
     ["/record" {:post {:handler handlers/post-record
                        :middleware [wrap-slurp-body]}}]])
   (ring/create-default-handler)))
