(set-env!
  :source-paths #{"src"}
  :dependencies '[[adzerk/boot-test "1.0.4" :scope "test"]
                  [org.clojure/clojure "1.7.0-RC1"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                  [org.clojure/data.json "0.2.5"]
                  [emiln/slacker "1.2.0"]
                  [stylefruits/gniazdo "0.3.1"]])

(deftask build
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace '#{stalmanu.run})
   (pom :project 'stalmanu
        :version "0.1.0")
   (uber)
   (jar :main 'stalmanu.run)))

(deftask stalmanu-test
  "Run the unit tests for Stalmanu in a pod."
  []
  (merge-env! :source-paths #{"test"})
  (require 'adzerk.boot-test)
  ((resolve 'adzerk.boot-test/test)))
