(ns record-app.routes
  (:require
   [reitit.ring :as ring]))

(def app
  (ring/ring-handler
   (ring/router
    [["/ping" {:get (fn [m] {:status 200 :body "pong\n"})}]])
   (ring/create-default-handler)))
