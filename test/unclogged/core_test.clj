(ns unclogged.core-test
  (:require
   [clojure.test :as t :refer [deftest testing is are]]
   [unclogged.core :as c])
  (:import
   [com.cloudbees.syslog Facility]))

(deftest facility-tests
  (testing "numerical codes"
    (are [code facility] (= (c/facility code) facility)
      0 Facility/KERN)))
