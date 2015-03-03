(ns stalmanu.run
  (:require
    [clojure.core.async :as async]
    [clojure.data.json :as json]
    [stalmanu.client :as client]
    [stalmanu.logic :refer [interject?]]))

(def interjection
  (str "I'd just like to interject for a moment. What you're referring to as "
    "Linux is in fact GNU/Linux, or as I've recently taken to calling it, GNU "
    "plus Linux. Linux is not an operating system unto itself, but rather "
    "another free component of a fully functioning GNU system made useful by "
    "the GNU corelibs, shell utilities and vital system components comprising "
    "a full OS as defined by POSIX.\n\n"

    "Many computer users run a modified version of the GNU system every day, "
    "without realizing it. Through a peculiar turn of events, the version of "
    "GNU which is widely used today is often called Linux, and many of its "
    "users are not aware that it is basically the GNU system, developed by "
    "the GNU Project.\n\n"

    "There really is a Linux, and these people are using it, but it is just a "
    "part of the system they use. Linux is the kernel: the program in the "
    "system that allocates the machine's resources to the other programs that "
    "you run. The kernel is an essential part of an operating system, but "
    "useless by itself; it can only function in the context of a complete "
    "operating system. Linux is normally used in combination with the GNU "
    "operating system: the whole system is basically GNU with Linux added, or "
    "GNU/Linux. All the so-called Linux distributions are really "
    "distributions of GNU/Linux."))

(defn predicate-action
  "Takes a publication, a topic, a predicate, and an action.
  Subscribes to the given topic on the given publication and does the following
  for every event on the topic:

  * When (predicate event): perform (action)."
  [publication topic predicate action]
  (let [subscription (async/chan)]
    (async/sub publication topic subscription)
    (async/go-loop []
      (when-let [msg (async/<! subscription)]
        (when (predicate msg)
          (action))
        (recur)))))

(defn start!
  "Launches Stalmanu with the given token."
  [token]
  (let [{:keys [receive send]} (client/login! token)]
    (predicate-action
      receive "message" #(interject? (:text %))
      (fn [] (async/go (async/>! send interjection))))))
