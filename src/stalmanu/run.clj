(ns stalmanu.run
  (:gen-class)
  (:require
    [clojure.core.async :refer [<!! timeout]]
    [clojure.tools.logging :refer [info]]
    [slacker.client :refer [handle emit!]]
    [stalmanu.logic :refer [interject?]]
    [stalmanu.rants :refer [full light]]))

(def interval 1800000)

(def next-message (atom (+ interval (System/currentTimeMillis))))

(defn interject!
  [{:keys [user text]}]
  (when (interject? text)
    (let [now (System/currentTimeMillis)]
      (if (> now @next-message)
        (do
          (emit! :slacker.client/send-message (full user))
          (reset! next-message (+ now interval)))
        (emit! :slacker.client/send-message (light user))))))

(defn -main
  [& [token]]
  (handle :message interject!)
  (println "Connecting with token" token)
  (emit! :slacker.client/connect-bot token)
  (loop []
    (<!! (timeout 1000))
    (recur)))
