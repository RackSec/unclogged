(ns unclogged.core
  (:import
   [com.cloudbees.syslog Facility])
  (:gen-class))

(defn ^:private facility
  [x]
  Facility/KERN)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
