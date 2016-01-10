(ns unclogged.core-test
  (:require
   [clojure.test :as t :refer [deftest testing is are]]
   [unclogged.core :as c])
  (:import
   [com.cloudbees.syslog Facility Severity]))

(defn facility-bit-flag
  "In syslog, facilities don't use the lowest 2 bits; they're ints
  shifted left by 3."
  [n]
  (bit-shift-left n 3))

(deftest facility-tests
  (testing "from numerical codes"
    (are [code facility] (let [flag (facility-bit-flag code)]
                           (= facility (@#'unclogged.core/facility flag)))
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
  (testing "from facility labels"
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
      "NTP" Facility/NTP
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
      "LOCAL7" Facility/LOCAL7))
  (testing "from lower case"
    (are [s facility] (= facility (@#'unclogged.core/facility s))
      "kern" Facility/KERN
      "user" Facility/USER
      "mail" Facility/MAIL
      "daemon" Facility/DAEMON
      "auth" Facility/AUTH
      "syslog" Facility/SYSLOG
      "lpr" Facility/LPR
      "news" Facility/NEWS
      "uucp" Facility/UUCP
      "cron" Facility/CRON
      "authpriv" Facility/AUTHPRIV
      "ftp" Facility/FTP
      "ntp" Facility/NTP
      "audit" Facility/AUDIT
      "alert" Facility/ALERT
      "clock" Facility/CLOCK
      "locaL0" Facility/LOCAL0
      "locaL1" Facility/LOCAL1
      "locaL2" Facility/LOCAL2
      "locaL3" Facility/LOCAL3
      "locaL4" Facility/LOCAL4
      "locaL5" Facility/LOCAL5
      "locaL6" Facility/LOCAL6
      "locaL7" Facility/LOCAL7))
  (testing "from keyword"
    (are [s facility] (= facility (@#'unclogged.core/facility s))
      :kern Facility/KERN
      :user Facility/USER
      :mail Facility/MAIL
      :daemon Facility/DAEMON
      :auth Facility/AUTH
      :syslog Facility/SYSLOG
      :lpr Facility/LPR
      :news Facility/NEWS
      :uucp Facility/UUCP
      :cron Facility/CRON
      :authpriv Facility/AUTHPRIV
      :ftp Facility/FTP
      :ntp Facility/NTP
      :audit Facility/AUDIT
      :alert Facility/ALERT
      :clock Facility/CLOCK
      :locaL0 Facility/LOCAL0
      :locaL1 Facility/LOCAL1
      :locaL2 Facility/LOCAL2
      :locaL3 Facility/LOCAL3
      :locaL4 Facility/LOCAL4
      :locaL5 Facility/LOCAL5
      :locaL6 Facility/LOCAL6
      :locaL7 Facility/LOCAL7)))

(deftest severity-tests
  (are [code severity] (= severity
                          (@#'unclogged.core/severity code))
    1 Severity/ALERT
    2 Severity/CRITICAL
    3 Severity/ERROR
    4 Severity/WARNING
    5 Severity/NOTICE
    6 Severity/INFORMATIONAL
    7 Severity/DEBUG))
