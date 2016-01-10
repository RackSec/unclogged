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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
