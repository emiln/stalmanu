(ns stalmanu.run
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.actions :refer [interject!]]
    [stalmanu.client :as client]
    [stalmanu.logic :refer [interject?]]))

(defn predicate-action
  "Takes a publication, a topic, a predicate, and an action.
  Subscribes to the given topic on the given publication and does the following
  for every event on the topic:

  * When (predicate event): perform (action event)."
  [publication topic predicate action]
  (let [subscription (async/chan)]
    (async/sub publication topic subscription)
    (async/go-loop []
      (when-let [msg (async/<! subscription)]
        (when (predicate msg)
          (action msg))
        (recur)))))

(defn start!
  "Launches Stalmanu with the given token."
  [token]
  (let [{:keys [receive send]} (client/login! token)]
    (predicate-action
      receive "message" #(interject? (:text %))
      (fn [msg] (interject! send msg)))))
