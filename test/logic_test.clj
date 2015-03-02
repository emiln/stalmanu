(ns logic-test
	(:require [expectations :refer [expect]]
						[stalmanu.logic :refer [interject?]]))

;; We don't want Stalmanu spamming the channel, so we require every one of the
;; following to fail.

(expect false
	(interject? "You should be allowed to say GNU/Linux"))
(expect false
	(interject? "And definitely also GNU+Linux"))
(expect false
	(interject? "Hell, even GNU and Linux should be fine."))
(expect false
	(interject? "More creative phrases like 'gnu-infested linux' should work."))
(expect false
	(interject? "Mentioning the Linux kernel should also be dandy."))
(expect false
	(interject? "As far as kernels go, Linux is pretty good."))
