(ns unclogged.core-test
  (:require
   [clojure.test :as t :refer [deftest testing is are]]
   [unclogged.core :as c])
  (:import
   [com.cloudbees.syslog Facility Severity MessageFormat SyslogMessage]
   [java.io CharArrayWriter]))

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
  (testing "from numerical codes"
    (are [code severity] (= severity
                            (@#'unclogged.core/severity code))
      1 Severity/ALERT
      2 Severity/CRITICAL
      3 Severity/ERROR
      4 Severity/WARNING
      5 Severity/NOTICE
      6 Severity/INFORMATIONAL
      7 Severity/DEBUG))
  (testing "from labels"
    (are [label severity] (= severity
                             (@#'unclogged.core/severity label))
      "ALERT" Severity/ALERT
      "CRITICAL" Severity/CRITICAL
      "ERROR" Severity/ERROR
      "WARNING" Severity/WARNING
      "NOTICE" Severity/NOTICE
      "INFORMATIONAL" Severity/INFORMATIONAL
      "DEBUG" Severity/DEBUG))
  (testing "from lower case labels"
    (are [label severity] (= severity
                             (@#'unclogged.core/severity label))
      "alert" Severity/ALERT
      "critical" Severity/CRITICAL
      "error" Severity/ERROR
      "warning" Severity/WARNING
      "notice" Severity/NOTICE
      "informational" Severity/INFORMATIONAL
      "debug" Severity/DEBUG))
  (testing "from keywords"
    (are [kw severity] (= severity
                          (@#'unclogged.core/severity kw))
      :alert Severity/ALERT
      :critical Severity/CRITICAL
      :error Severity/ERROR
      :warning Severity/WARNING
      :notice Severity/NOTICE
      :informational Severity/INFORMATIONAL
      :debug Severity/DEBUG))
  (testing "from aliases"
    (are [sev-alias severity] (let [as-str (name sev-alias)
                                    upper-case (.toUpperCase ^String as-str)]
                                (= severity
                                   (@#'unclogged.core/severity sev-alias)
                                   (@#'unclogged.core/severity as-str)
                                   (@#'unclogged.core/severity upper-case)))
      :info Severity/INFORMATIONAL
      :err Severity/ERROR
      :warn Severity/WARNING)))

(deftest message-format-tests
  (testing "from strings"
    (are [s fmt] (= fmt (@#'unclogged.core/message-format s))
      "RFC 3164" MessageFormat/RFC_3164
      "RFC-3164" MessageFormat/RFC_3164
      "RFC_3164" MessageFormat/RFC_3164
      "RFC3164" MessageFormat/RFC_3164
      "rfc 3164" MessageFormat/RFC_3164
      "rfc-3164" MessageFormat/RFC_3164
      "rfc_3164" MessageFormat/RFC_3164
      "rfc3164" MessageFormat/RFC_3164

      "RFC 5424" MessageFormat/RFC_5424
      "RFC-5424" MessageFormat/RFC_5424
      "RFC_5424" MessageFormat/RFC_5424
      "RFC5424" MessageFormat/RFC_5424
      "rfc 5424" MessageFormat/RFC_5424
      "rfc-5424" MessageFormat/RFC_5424
      "rfc_5424" MessageFormat/RFC_5424
      "rfc5424" MessageFormat/RFC_5424))
  (testing "from keywords"
    (are [kw fmt] (= fmt (@#'unclogged.core/message-format kw))
      :RFC-3164 MessageFormat/RFC_3164
      :RFC_3164 MessageFormat/RFC_3164
      :RFC-3164 MessageFormat/RFC_3164
      :RFC_3164 MessageFormat/RFC_3164
      :RFC3164 MessageFormat/RFC_3164
      :rfc-3164 MessageFormat/RFC_3164
      :rfc_3164 MessageFormat/RFC_3164
      :rfc3164 MessageFormat/RFC_3164

      :RFC-5424 MessageFormat/RFC_5424
      :RFC_5424 MessageFormat/RFC_5424
      :RFC-5424 MessageFormat/RFC_5424
      :RFC_5424 MessageFormat/RFC_5424
      :RFC5424 MessageFormat/RFC_5424
      :rfc-5424 MessageFormat/RFC_5424
      :rfc_5424 MessageFormat/RFC_5424
      :rfc5424 MessageFormat/RFC_5424)))

(deftest ->syslog-msg-tests
  (testing "no defaults, all keys"
    (let [contents {:message "hello"
                    :message-id "xyzzy"
                    :app-name "unclogged"
                    :hostname "ditka"
                    :process-id "1234"}
          syslog-msg (@#'unclogged.core/->syslog-msg {} contents)]
      (is (= "hello" (.toString (.getMsg ^SyslogMessage syslog-msg))))
      (is (= "xyzzy" (.getMsgId ^SyslogMessage syslog-msg)))
      (is (= "unclogged" (.getAppName ^SyslogMessage syslog-msg)))
      (is (= "ditka" (.getHostname ^SyslogMessage syslog-msg)))
      (is (= "1234" (.getProcId ^SyslogMessage syslog-msg)))))
  (testing "some defaults, all keys"
    (let [defaults {:app-name "unclogged"
                    :hostname "ditka"
                    :process-id "1234"}
          contents {:message "hello"
                    :message-id "xyzzy"}
          syslog-msg (@#'unclogged.core/->syslog-msg defaults contents)]
      (is (= "hello" (.toString (.getMsg ^SyslogMessage syslog-msg))))
      (is (= "xyzzy" (.getMsgId ^SyslogMessage syslog-msg)))
      (is (= "unclogged" (.getAppName ^SyslogMessage syslog-msg)))
      (is (= "ditka" (.getHostname ^SyslogMessage syslog-msg)))
      (is (= "1234" (.getProcId ^SyslogMessage syslog-msg)))))
  (testing "type coercions"
    (let [defaults {:app-name "unclogged"
                    :hostname "ditka"
                    :process-id 1234}
          contents {:message [[:a] [[[{:b :c}]]]]
                    :message-id "xyzzy"}
          syslog-msg (@#'unclogged.core/->syslog-msg defaults contents)]
      (is (= "[[:a] [[[{:b :c}]]]]"
             (.toString ^CharArrayWriter (.getMsg ^SyslogMessage syslog-msg))))
      (is (= "xyzzy" (.getMsgId ^SyslogMessage syslog-msg)))
      (is (= "unclogged" (.getAppName ^SyslogMessage syslog-msg)))
      (is (= "ditka" (.getHostname ^SyslogMessage syslog-msg)))
      (is (= "1234" (.getProcId ^SyslogMessage syslog-msg))))))
