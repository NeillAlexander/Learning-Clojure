(ns practical_clojure.chpt13)

;; datatypes and protocols are roughlty analagous to classes and interfaces in Java
;; enable defining new abstractions without having to drop into Java
;; which paves the way for Clojure in Clojure (as seen in Clojurescript)

;; defines a set of methods, where each method has a name, one or more vectors
;; and an optional documentation string
(defprotocol MyProtocol
  "This is a new protocol"
  (method-one [x] "This is the first method")
  (method-two ([x] [x y] "This is the second method")))

;; defrecord
(defrecord Employee [name room])

(def emp (Employee. "John Smith" 304))

;; behaves like a map - faster than a struct map
(:name emp)
(seq emp)
(assoc emp :name "Neill Alexander")

;; if you dissoc, it turns back to a map
(dissoc emp :name)

(defrecord Pair [x y]
  java.lang.Comparable
  (compareTo [this other]
    (let [result (compare x (:x other))]
      (if (zero? result)
        (compare y (:y other))
        result))))

(compare (Pair. 1 2) (Pair. 1 2))


;; best practices
;; prefer reify to proxy unless you need to override base class methods
;; prefer defrecord to gen-class unless you need gen-class features for Java inter-op
;; specify your abstractions as protocols, not interfaces
;; prefer protocols to multi-methods for the case of single-argument type dispatch
;; add type hints only where necessary

(defprotocol Payroll
  (paycheck [emp hrs]))

(defrecord HourlyEmployee [name rate]
  Payroll
  (paycheck [emp hrs] (* rate hrs)))

(defrecord SalariedEmployee [name salary]
  Payroll
  (paycheck [emp hrs] (/ salary 12.0)))

(def emp1 (HourlyEmployee. "Devin" 12))
(def emp2 (SalariedEmployee. "Casey" 30000))

(paycheck emp1 105)
(paycheck emp2 120)

(defn contract [amount]
  (reify Payroll (paycheck [emp hrs] amount)))

(def con1 (contract 50000))

(paycheck con1 80)
(paycheck con1 800)
