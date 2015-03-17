(ns stalmanu.rants
  (:require [clojure.java.io :refer [reader]]
            [clojure.string :refer [join]]))

(defn- read-and-format
  [file-name & args]
  (with-open [file (reader (str "resources/rants/" file-name ".txt"))]
    (apply format (rand-nth (line-seq file)) args)))

(defn full
  "Inserts a reference to user-id into the full rant and returns the rant as a
  string."
  [user-id]
  (with-open [rdr (reader "resources/rants/full.txt")]
    (->> (line-seq rdr)
      (join "\n\n")
      (#(format % (str "<@" user-id ">"))))))

(defn light
  "Generates a random light rant and inserts a reference to user-id in it.
  Returns the rant as a string."
  [user-id]
  (let [part1 (read-and-format "light-01" (str "<@" user-id ">"))
        split (read-and-format "light-02")
        part2 (read-and-format "light-03" split)]
    (join " " [part1 part2])))
