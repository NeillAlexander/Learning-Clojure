(ns practical_clojure.chpt8)

;; "meta data is a map of data attached to an object that does not affect the value of the object"
;; modifying meta-data yields a new object with the same value and the same hash code as the
;; original

(set! *print-meta* true)

(def v (with-meta [1 2] {:about "A vector"}))

(meta v)

(def v-new (vary-meta v assoc :help "It's a vector"))

(= v v-new) ;; true
(identical? v v-new) ;;false

;; collection functions (conj, assoc, dissoc etc) preserve meta data
;; sequence functions do not
;; ALWAYS TEST IF YOU ARE RELYING ON IT!! There are exceptions...

;; can attach meta-data with reader macro #^

#^{:comment "hello"} [2 3]

;; only applies to literal forms. this won't work as expected
#^{:m 1} (list 1 2)

;; "The #^ reader macro is normally used to attach metadata to symbols, not data structures"
(meta (var or))

;; ^{:ns #<Namespace practical_clojure.chpt8>, :name *thing*, :file "NO_SOURCE_FILE", :line 1, :doc "My cool thing"} #'practical_clojure.chpt8/*thing*
(def #^{:doc "My cool thing"} *thing*)

;; that is cool!

(doc *thing*)

;; some standard meta-data tags that clojure uses
;; :name
;; :ns
;; :file
;; :line
;; :doc
;; :arglists
;; :macro - true for macro
;; :private - true for private var
;; :tag - used for type hints to compiler

(def #^{:private true} *my-private-var*)

(defn- my-private-fn [x] x)

(meta my-private-fn)

;; meta data fills similar role as Java annotations
