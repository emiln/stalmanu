(set-env!
  :source-paths #{"src"}
  :dependencies '[[expectations "2.0.9"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                  [org.clojure/data.json "0.2.5"]
                  [http-kit "2.1.16"]
                  [stylefruits/gniazdo "0.3.1"]])

(require '[expectations :as exp])

(deftask expectations
  "Run tests"
  []
  (set-env! :source-paths #(conj % "test"))
  (use 'logic-test))
