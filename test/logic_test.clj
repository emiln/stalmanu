(ns logic-test
  (:require [clojure.test :refer [are deftest is testing]]
            [stalmanu.logic :refer [interject?]]))

(deftest about-interjections
  (testing "Facts about injections"
    (testing "only appropriate and educational notices about GNU plus Linux make
             it onto our channels."
      (are [text] (false? (interject? text))
        "You should be allowed to say GNU/Linux"
        "And definitely also GNU+Linux"
        "Hell, even GNU and Linux should be fine."
        "More creative phrases like 'gnu-infested linux' should work."
        "Mentioning the Linux kernel should also be dandy."
        "As far as kernels go, Linux is pretty good."))))
