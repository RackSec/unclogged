(ns unclogged.core
  (:import
   [com.cloudbees.syslog Facility Severity MessageFormat SyslogMessage])
  (:gen-class))

(defn ^:private parse-facility
  [x]
  (cond
    (keyword? x) (facility (name x))
    (string? x) (Facility/fromLabel (.toUpperCase ^String x))
    (number? x) (Facility/fromNumericalCode x)))

(def ^:private severity-aliases
  {"INFO" Severity/INFORMATIONAL
   "WARN" Severity/WARNING
   "ERR" Severity/ERROR})

(defn ^:private severity
  [x]
  (if (number? x)
    (Severity/fromNumericalCode x)
    (let [x (.toUpperCase ^String (name x))]
      (or (get severity-aliases x)
          (Severity/fromLabel x)))))

(defn ^:private message-format
  [s]
  (case (re-find #"\d+" (name s))
    "3164" MessageFormat/RFC_3164
    "5424" MessageFormat/RFC_5424))

(defn ^:private ->syslog-msg
  "Turns defaults + message map into a SyslogMessage."
  [defaults message]
  (let [{:keys [message
                message-id
                app-name
                hostname
                process-id
                facility
                severity]}
        (merge defaults message)]
    (doto (SyslogMessage.)
      (.withMsg (str message))
      (.withMsgId message-id)
      (.withAppName app-name)
      (.withHostname hostname)
      (.withProcId (str process-id))
      (.withFacility facility)
      (.withSeverity severity))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
