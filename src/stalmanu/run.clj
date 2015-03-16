(ns stalmanu.run
  (:gen-class)
  (:require
    [slacker.client :refer [handle emit!]]
    [stalmanu.logic :refer [interject?]]
    [stalmanu.rants :refer [full light]]))

(def interval 1800000)

(def next-message (atom (+ interval (System/currentTimeMillis))))

(handle :message
  (fn [{:keys [text]}]
    (when (interject? text)
      (let [now (System/currentTimeMillis)]
        (if (> now @next-message)
          (do
            (emit! :slacker.client/send-message (full))
            (reset! next-message (+ now interval)))
          (emit! :slacker.client/send-message (light)))))))

(defn -main
  [& [token]]
  (println "Connecting with token" token)
  (emit! :slacker.client/connect-bot token))
