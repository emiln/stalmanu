(ns stalmanu.client
  "A simple Stalmanu bot, which lurks in channels and interjects."
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [org.httpkit.client :as http]
    [gniazdo.core :as ws]))

(defn client!
  "Connects to the given websocket URL and returns a map of
  :socket - the open websocket
  :pub    - a publication onto which events on Slack are pushed. The topic
            type is String and the most popular topic will probably be
            'message'."
  [url]
  (let [publisher   (async/chan)
        publication (async/pub publisher :type)
        socket (ws/connect
                 url
                 :on-receive (fn [raw]
                               (async/go
                                 (->> (json/read-str raw :key-fn keyword)
                                   (async/>! publisher)))))]
    {:socket socket
     :pub publication}))

(def ^:private counter (atom 0))

(defn send!
  "Sends a message to the Slack server, generating id as necessary. Currently
  just sends to the 'General' channel."
  [websocket message]
  (let [msg {:id (swap! counter inc)
             :type "message"
             :channel "C03RGK7FC"
             :text message}]
    (ws/send-msg websocket (json/write-str msg))))

(defn login!
  "Connects to Slack given a token and returns a map of
  :receive - A publication by topic. See (client!).
  :send    - A channel to push message text to."
  [token]
  (let [send (async/chan)
        {:keys [socket pub]}
        (-> (str "https://slack.com/api/rtm.start?token=" token)
              (http/get)
              (deref)
              :body
              (json/read-str :key-fn keyword)
              :url
              (client!))]
    (async/go-loop []
      (when-let [msg (async/<! send)]
        (send! socket msg)
        (recur)))
    {:receive pub
     :send send}))
