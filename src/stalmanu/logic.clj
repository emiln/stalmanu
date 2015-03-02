(ns stalmanu.logic
  (:require [clojure.string :as s]))

(defn interject?
  "Returns true if the string definitely warrants an interjection."
  [string]
  (let [lower  (s/lower-case (or string ""))
        gahnoo (> (.indexOf lower "gnu") -1)
        kernel (> (.indexOf lower "kernel") -1)
        linux  (> (.indexOf lower "linux") -1)]
    (and linux
         (not gahnoo)
         (not kernel))))
