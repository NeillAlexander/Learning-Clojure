(ns practical-clojure.chpt4)

;; although Clojure is dynamically typed, there are still types:
;; lists, numbers, vectors, strings etc

;; clojure types

;; Number     23
;; String     "hello"
;; Boolean    true or false
;; Character  \a
;; Keyword    :keyword
;; List       '(1 2 3)  ; escaped because 1 is not a function
;; Vector     [1 2 3]
;; Map        {:key val}
;; Set        #{1 2 3}

;; 2 remainder 1
(quot 5 2)
(rem 5 2)

(subs "Hello world" 0 5)

\u00A3 ;; £

(def say-hello (list println "Hello"))

(eval say-hello)

(vector 1 2 3)
(vec '(1 2 3))

(= (vector 1 2 3) (vec '(1 2 3))) ;; true


;; different types of map
(array-map :a 1, :b 2)
(hash-map :a 1, :b 2, :c 3)
(sorted-map :a 1, :b 2, :c 3)


;; ooh, I like this
;; this is the knowledge I was missing when I was writing Sponge
(defstruct person :first-name :last-name)
(def neill (struct-map person :first-name "Neill" :last-name "Alexander"))
(def emmal (struct-map person :first-name "Emma" :last-name "Alexander"))

;; the key structure is shared, and so the storage is more efficient

;; can then use the accessor function to create very efficient accessor methods
(def get-first-name (accessor person :first-name))
(def get-last-name (accessor person :last-name))

(get-first-name neill)
(get-last-name neill)

;; sweet!

;; "In general you shouldn't worry about using struct-maps except for performance reasons.
;; Normal maps are fast enough for most applications..."

(assoc {:a 1, :b 2} :a 3)
(merge {:a 1, :b 2} {:c 3, :d 4})

(merge-with + {:a 1, :b 2} {:a 3, :b 2})
