(ns practical_clojure.chpt9)

;; Clojure mult-methods provide runtime polymorphic dispatch
;; can be based on any or all arguments to the function
;; Much more powerful than Java

;; created with defmulti and implemented with defmethod

;; when you call a multi-method, the dispatch function is called to
;; generate the dispatch value. Clojure then searches for a method
;; with a matching dispatch value

(def a {:name "Arthur, :species ::human, :strength 8"})
(def b {:name "Balfor, :species ::elf, :strength 7"})
(def c {:name "Callis, :species ::elf, :strength 5"})
(def d {:name "Drung, :species ::orc, :strength 6"})

(defmulti move :species)

(defmethod move ::elf [creature]
  (str (:name creature) " runs swiftly"))
