(ns record-app.server
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [record-app.routes :refer [app]])
  (:gen-class))

(defn -main [& _]
  (run-jetty app {:port 3000}))
