(ns unclogged.core
  (:require
   [manifold.stream :as s])
  (:import
   [com.cloudbees.syslog.sender TcpSyslogMessageSender UdpSyslogMessageSender]
   [com.cloudbees.syslog Facility Severity MessageFormat SyslogMessage])
  (:gen-class))

(defn ^:private parse-facility
  [x]
  (cond
    (instance? Facility x) x
    (keyword? x) (parse-facility (name x))
    (string? x) (Facility/fromLabel (.toUpperCase ^String x))
    (number? x) (Facility/fromNumericalCode x)))

(def ^:private severity-aliases
  {"INFO" Severity/INFORMATIONAL
   "WARN" Severity/WARNING
   "ERR" Severity/ERROR})

(defn ^:private parse-severity
  [x]
  (cond
    (instance? Severity x) x
    (number? x) (Severity/fromNumericalCode x)
    :else (let [x (.toUpperCase ^String (name x))]
            (or (get severity-aliases x)
                (Severity/fromLabel x)))))

(defn ^:private parse-message-format
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
      (.withFacility (parse-facility facility))
      (.withSeverity (parse-severity severity)))))

(defn ^:private make-syslog
  "Creates an instance.

  This fn exists because it's way easier to redef a Clojure fn than it
  is to mock a Java class."
  [transport]
  (case transport
    :udp (UdpSyslogMessageSender.)
    :tcp (TcpSyslogMessageSender.)
    ;; TLS is enabled in configured-syslog.
    :tls (TcpSyslogMessageSender.)
    :ssl (TcpSyslogMessageSender.)))

(defn ^:private configured-syslog
  "Creates a configured syslog instance.

  Please note that this currently does not set any message defaults;
  all values are always set on all syslog messages; this may be a
  small performance hit, but helps inspectability. Ideally, our syslog
  implementation wouldn't check for that at all, and decomplect the
  concerns of providing defaults with the actual send-syslog-messages
  bits."
  [conn-opts]
  (let [{:keys [host port message-format transport]
         :or {transport :tls}} conn-opts
        syslog (make-syslog transport)
        port (if (string? port)
               (Integer/parseInt port)
               port)]
    (.setSyslogServerHostname syslog host)
    (when port
      (.setSyslogServerPort syslog port))
    (when message-format
      (.setMessageFormat syslog (parse-message-format message-format)))
    (when (#{:tls :ssl} transport)
      (.setSsl syslog true))
    syslog))

(def system-defaults
  "Unclogged's default chocies for syslog messages."
  {:severity Severity/INFORMATIONAL
   :facility Facility/USER})

(defn ->syslog!
  "Consumes elems on source and sends them to syslog as specified by
  the connection map & defaults.

  Values in message maps override values in the defaults; values in
  the defaults override unclogged's default-defaults. This prevents
  NullPointerExceptions when you forget to provide a facility or
  severity, for example.

  Returns a map containing the syslog client object under the :syslog
  key. This is only provided for inspection; mutating that object is
  not guaranteed to have desired effects. It also contains the input
  stream under the :stream key."
  [source conn-opts defaults]
  (let [actual-defaults (merge system-defaults defaults)
        syslog (configured-syslog conn-opts)
        send! (fn [message-details]
                (->> message-details
                     (->syslog-msg actual-defaults)
                     (.sendMessage syslog)))]
    (s/consume send! source)
    {:syslog syslog
     :stream source}))

(defn syslog-sink
  "Builds a Manifold sink (stream) where you can dump information
  you'd like to send to syslog. Takes a connection map and some
  message defaults.

  Returns a map that contains at least the built stream under
  the :stream key.

  If you already have a manifold stream, see ->syslog!."
  [conn-opts defaults]
  (let [sink (s/stream)]
    (->syslog! sink conn-opts defaults)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
