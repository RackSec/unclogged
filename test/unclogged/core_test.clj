(ns unclogged.core-test
  (:require
   [clojure.test :as t :refer [deftest testing is are]]
   [unclogged.core :as c])
  (:import
   [com.cloudbees.syslog Facility]))

(defn facility-bit-flag
  "In syslog, facilities don't use the lowest 2 bits; they're ints
  shifted left by 3."
  [n]
  (bit-shift-left n 3))

(deftest facility-tests
  (testing "from numerical codes"
    (are [code facility] (= (@#'unclogged.core/facility (facility-bit-flag code))
                            facility)
      0 Facility/KERN
      1 Facility/USER
      2 Facility/MAIL
      3 Facility/DAEMON
      4 Facility/AUTH
      5 Facility/SYSLOG
      6 Facility/LPR
      7 Facility/NEWS
      8 Facility/UUCP
      9 Facility/CRON
      10 Facility/AUTHPRIV
      11 Facility/FTP
      12 Facility/NTP
      13 Facility/AUDIT
      14 Facility/ALERT
      15 Facility/CLOCK
      16 Facility/LOCAL0
      17 Facility/LOCAL1
      18 Facility/LOCAL2
      19 Facility/LOCAL3
      20 Facility/LOCAL4
      21 Facility/LOCAL5
      22 Facility/LOCAL6
      23 Facility/LOCAL7))
  (testing "from strings"
    (are [s facility] (= (@#'unclogged.core/facility s) facility)
      "KERN" Facility/KERN
      "USER" Facility/USER
      "MAIL" Facility/MAIL
      "DAEMON" Facility/DAEMON
      "AUTH" Facility/AUTH
      "SYSLOG" Facility/SYSLOG
      "LPR" Facility/LPR
      "NEWS" Facility/NEWS
      "UUCP" Facility/UUCP
      "CRON" Facility/CRON
      "AUTHPRIV" Facility/AUTHPRIV
      "FTP" Facility/FTP
      "NTP "Facility/NTP
      "AUDIT" Facility/AUDIT
      "ALERT" Facility/ALERT
      "CLOCK" Facility/CLOCK
      "LOCAL0" Facility/LOCAL0
      "LOCAL1" Facility/LOCAL1
      "LOCAL2" Facility/LOCAL2
      "LOCAL3" Facility/LOCAL3
      "LOCAL4" Facility/LOCAL4
      "LOCAL5" Facility/LOCAL5
      "LOCAL6" Facility/LOCAL6
      "LOCAL7" Facility/LOCAL7)))
