(ns practical_clojure.chpt9)

;; Clojure mult-methods provide runtime polymorphic dispatch
;; can be based on any or all arguments to the function
;; Much more powerful than Java

;; created with defmulti and implemented with defmethod

;; when you call a multi-method, the dispatch function is called to
;; generate the dispatch value. Clojure then searches for a method
;; with a matching dispatch value

(def a {:name "Arthur", :species ::human, :strength 8})
(def b {:name "Balfor", :species ::elf, :strength 4})
(def c {:name "Callis", :species ::elf, :strength 5})
(def d {:name "Drung", :species ::orc, :strength 6})

;; :species is the dispatch function
;; a keyword is a function on a map
(defmulti move :species)

(defmethod move ::elf [creature]
  (str (:name creature) " runs swiftly"))

(defmethod move ::human [creature]
  (str (:name creature) " walks steadily"))

(defmethod move ::orc [creature]
  (str (:name creature) " stomps heavily"))


;; note that the big benefit here is the fact that a new creature could
;; be added at any time just by defining another move method
;; nothing else needs to change

(defmulti attack (fn [creature]
                   (if (> (:strength creature) 5)
                     :strong
                     :weak)))

(defmethod attack :strong [creature]
  (str (:name creature) " attacks mightily"))

(defmethod attack :weak [creature]
  (str (:name creature) " attacks feebly"))


;; can dispath on mulitple arguments

(defmulti encounter (fn [x y]
                      [(:species x) (:species y)]))

(defmethod encounter [::elf ::orc] [elf orc]
  (str "Brave elf " (:name elf) " attacks evil orc " (:name orc)))

(defmethod encounter [::orc ::elf] [orc elf]
  (str "Evil orc " (:name orc) " attacks brave elf " (:name elf)))

(defmethod encounter [::elf ::elf] [elf1 elf2]
  (str "Brave elf " (:name elf1) " greets his pal " (:name elf2)))

(defmethod encounter :default [x y]
  (str (:name x) " and " (:name y) " ignore each other"))

;; or

(defmulti talk :species :default "other")

(defmethod talk ::orc [creature]
  (str (:name creature) " grunts"))

(defmethod talk "other" [creature]
  (str (:name creature) " speaks"))

;; hierarchies
(derive ::human ::good)
(derive ::elf ::good)
(derive ::orc ::evil)
(derive ::elf ::magical)
(derive ::orc ::magical)
(derive ::hero ::human)

(isa? ::orc ::good)
(isa? ::human ::good)

;; can now use hierarchies to dispatch
(defmulti cast-spell :species)

(defmethod cast-spell ::magical [creature]
  (str (:name creature) " casts a spell"))

(defmethod cast-spell :default [creature]
  (str "No, " (:name creature) " is not magical"))


;; ha ha
(derive java.util.Date ::evil)
(isa? java.util.Date ::evil)

(defmulti invert class)

(defmethod invert Number [x]
  (- x))

(defmethod invert String [x]
  (apply str (reverse x)))

(parents ::orc)
(parents java.util.Date)


;; use prefer-method to resolve conflicts

;; mult-methods good for high level apis
;; cost means not good for low level
;; for low level use protocols
