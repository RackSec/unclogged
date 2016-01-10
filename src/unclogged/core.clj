(ns unclogged.core
  (:import
   [com.cloudbees.syslog Facility Severity])
  (:gen-class))

(defn ^:private facility
  [x]
  (cond
    (keyword? x) (facility (name x))
    (string? x) (Facility/fromLabel (.toUpperCase ^String x))
    (number? x) (Facility/fromNumericalCode x)))

(defn ^:private severity
  [x]
  (cond
    (string? x) (Severity/fromLabel x)
    (number? x) (Severity/fromNumericalCode x)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
