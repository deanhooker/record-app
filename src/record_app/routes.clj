(ns record-app.routes
  (:require
   [muuntaja.core :as m]
   [record-app.handlers :as handlers]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as rrc]
   [reitit.ring.middleware.muuntaja :as muuntaja]))

(def routes
  [["/ping"              {:get (fn [_] {:status 200 :body "pong\n"})}]
   ["/record"            {:post handlers/post-record}]
   ["/records/color"     {:get handlers/get-records-by-color}]
   ["/records/birthdate" {:get handlers/get-records-by-date-of-birth}]
   ["/records/name"      {:get handlers/get-records-by-last-name-desc}]])

(def app
  (ring/ring-handler
   (ring/router
    routes
    {:data {:muuntaja   m/instance
            :middleware [muuntaja/format-response-middleware
                         rrc/coerce-response-middleware]}})
   (ring/create-default-handler)))
