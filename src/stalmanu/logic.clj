(ns stalmanu.logic
  (:require [clojure.string :as s]))

(defn interject?
  "Returns true if the string definitely warrants an interjection."
  [string]
  (let [lower  (s/lower-case (or string ""))
        linux  (.indexOf lower "linux")
        gahnoo (.indexOf lower "gnu")]
    (and (> linux -1)
         (= gahnoo -1))))
