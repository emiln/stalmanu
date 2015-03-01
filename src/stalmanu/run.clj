(ns stalmanu.run
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.client :as client]
    [stalmanu.logic :refer [interject?]]))

(defn start!
  [token]
  (let [{:keys [receive send]} (client/login! token)]
    (async/go-loop []
      (when-let [msg (async/<! receive)]
        (let [text (:text (json/read-str msg :key-fn keyword))]
          (when (interject? text)
            (async/>! send "It's GNU+Linux, faggot."))
          (recur))))))
