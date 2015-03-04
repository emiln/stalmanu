(ns stalmanu.client
  "A simple Stalmanu bot, which lurks in channels and interjects."
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [org.httpkit.client :as http]
    [gniazdo.core :as ws]))

(def ^:private publisher (async/chan))
(def ^:private publication (async/pub publisher :type))

(defn emit!
  "Emits a message to handlers. The message is expected to be a map with the
  key :type present as this is used as topic. If no such key is found, a
  horrible exception will be thrown and everyone will be sad."
  [message]
  (when-not (map? message)
    (throw (Exception. (str "emit! should be called with a map containing "
                            ":type. Actual value: " (pr-str message)))))
  (async/put! publisher message))

(defn handle
  "Subscribes an event handler for the given topic. The handler will be called
  whenever an event is emitted for the given topic, and any optional args from
  emit! will be passed as arguments to the handler-fn."
  [topic handler-fn]
  (let [c (async/chan)]
    (async/sub publication topic c)
    (async/go-loop []
      (when-let [msg (async/<! c)]
        (handler-fn msg)
        (recur)))))

(defn client!
  "Connects to the given websocket URL and returns a map of
  :socket - the open websocket
  :pub    - a publication onto which events on Slack are pushed. The topic
            type is String and the most popular topic will probably be
            'message'."
  [url]
  (ws/connect
    url
    :on-receive (fn [raw]
                  (emit! (json/read-str raw :key-fn keyword)))))

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
  (-> (str "https://slack.com/api/rtm.start?token=" token)
        (http/get)
        (deref)
        :body
        (json/read-str :key-fn keyword)
        :url
        (client!)))
