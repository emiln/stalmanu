(ns stalmanu.client
  "A simple Stalmanu bot, which lurks in channels and interjects."
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [org.httpkit.client :as http]
    [gniazdo.core :as ws]))

(defn client!
  "Connects to the given websocket URL and pushes events received onto it."
  [url channel]
  (ws/connect
    url
    :on-receive #(async/go (async/>! channel %))))

(def counter (atom 0))

(defn send!
  "Sends a message to the server, generating id as necessary. Currently just
  sends to the 'General' channel."
  [websocket message]
  (let [msg {:id (swap! counter inc)
             :type "message"
             :channel "C03RGK7FC"
             :text message}]
    (ws/send-msg websocket (json/write-str msg))))

(defn login!
  "Creates channels for receiving and sending messages, connects to Slack, and
  returns the channels."
  [token]
  (let [receive (async/chan)
        send    (async/chan)
        socket (-> (str "https://slack.com/api/rtm.start?token=" token)
                 http/get
                 deref
                 :body
                 (json/read-str :key-fn keyword)
                 :url
                 (client! receive))]
    (async/go-loop []
      (when-let [msg (async/<! send)]
        (send! socket msg)
        (recur)))
    {:receive receive
     :send send}))
