(ns user
  (:require
   [record-app.records :as records]
   [record-app.state :refer [clear-records! records]]
   [record-app.routes :refer [app]]
   [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defn start []
  (reset! server (jetty/run-jetty app {:port 3000 :join? false}))
  (println "Server started on port 3000"))

(defn stop []
  (when @server
    (.stop @server)
    (reset! server nil)
    (println "Server stopped")))

(defn restart []
  (stop)
  (start))

(comment
  (swap! records conj
         (#'record-app.records/->record "Dev" "User" "dev@example.com" "green" "1/1/1990"))

  @records

  (clear-records!)
  )

;; Example post:
;; curl http://localhost:3000/record -d "America,Andy,andy.america@yopmail.com,red,2/2/1981"
