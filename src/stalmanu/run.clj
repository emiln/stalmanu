(ns stalmanu.run
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.client :as client]))

(defn start!
  [token]
  (let [{:keys [receive send]} (client/login! token)]
    (async/go-loop []
      (when-let [msg (async/<! receive)]
        (println (json/read-str msg :key-fn keyword))
        (recur)))))
