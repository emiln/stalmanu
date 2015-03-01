(ns stalmanu.run
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.client :as client]
    [stalmanu.logic :refer [interject?]]))

(def interjection
  (str "I'd just like to interject for a moment. What you're referring"
    " to as linux is in fact GNU/Linux, or as I've recently taken to"
    " calling it, GNU plus Linux."))

(defn start!
  [token]
  (let [{:keys [receive send]} (client/login! token)]
    (async/go-loop []
      (when-let [msg (async/<! receive)]
        (let [text (:text (json/read-str msg :key-fn keyword))]
          (when (interject? text)
            (async/>! send interjection))
          (recur))))))
