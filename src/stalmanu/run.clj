(ns stalmanu.run
  (:gen-class)
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.actions :refer [interject!]]
    [stalmanu.client :as client]
    [stalmanu.logic :refer [interject?]]))

(defn start!
  "Launches Stalmanu with the given token."
  [token]
  (let [socket (client/login! token)]
    (client/handle "message"
      (fn [msg]
        (when (interject? msg)
          (interject! socket))))))

(defn -main [& args]
  (start! (first args)))
