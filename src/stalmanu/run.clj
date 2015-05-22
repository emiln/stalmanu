(ns stalmanu.run
  (:gen-class)
  (:require
    [clojure.core.async :refer [<!! timeout]]
    [slacker.client :refer [handle emit!]]
    [stalmanu.logic :refer [interject?]]
    [stalmanu.rants :refer [full light]]))

(def interval 1800000)

(def next-message (atom (+ interval (System/currentTimeMillis))))

(defn interject!
  [{:keys [channel user text]}]
  (when (interject? text)
    (let [now (System/currentTimeMillis)]
      (if (> now @next-message)
        (do
          (emit! :slacker.client/send-message (full user))
          (reset! next-message (+ now interval)))
        (emit! :slacker.client/send-message channel (light user))))))

(defn -main
  [token]
  (handle :message interject!)
  (emit! :slacker.client/connect-bot token)
  (loop []
    (<!! (timeout 10000))
    (recur)))
