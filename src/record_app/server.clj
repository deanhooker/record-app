(ns record-app.server
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [record-app.routes :refer [app]])
  (:gen-class))

(defn -main
  "Starts the HTTP server on port 3000."
  [& _]
  (run-jetty app {:port 3000 :join? false})) ; TODO: join? true for prod
