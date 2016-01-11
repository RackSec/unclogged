(ns unclogged.core
  (:import
   [com.cloudbees.syslog Facility Severity MessageFormat])
  (:gen-class))

(defn ^:private facility
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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
