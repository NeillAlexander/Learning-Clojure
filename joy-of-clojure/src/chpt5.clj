(ns chpt5)

;persistent data structures

;array is not persistent
(def ds (into-array [:willie :barnabas :adam]))

(seq ds)

(aset ds 1 :quentin)
(seq ds)

;clojure data structures are
(def ds [:willie :barnabas :adam])
ds

(def ds1 (replace {:barnabas :quentin} ds))
ds1

;clojure classifies each composite data type as either:
; 1. Sequential
; 2. Map
; 3. Set
; Equality only applies within each partition ie. a map can never be equal to a set of sequential

;if 2 sequentials have the sames values in the same order, they are equal
(= [1 2 3] '(1 2 3)) ;true

;not same type
(= [1 2 3] #{1 2 3})  ;sequential can never be equal to a set

;sequentials include:
;clojure list, vector, and anything that implements java.util.List
;"generally things that faill into the other partitions include set or map in their name and so are easy to identify"

;the sequence abstraction plays the same role in Clojure that the cons-cell plays in Lisp
;all that is required is to support the 2 core functions first and rest

(rseq [1 2 3 4])
(class (hash-map :a 1))                ;clojure.lang.PersistentHashMap
(class (seq (hash-map :a 1)))          ;clojure.lang.PersistentHashMap$NodeSeq
(class (seq (keys (hash-map :a 1))))   ;clojure.lang.APersistentMap$KeySeq

;vectors
(vec (range 10))
(class (vec (range 10))) ;clojure.lang.PersistentVector

(let [my-vector [:a :b :c]]
  (into my-vector (range 10)))  ;[:a :b :c 0 1 2 3 4 5 6 7 8 9]

(into (vector-of :int) [Math/PI 2 1.3])      ;[3 2 1]
(into (vector-of :double) [Math/PI 2 1.3])   ;[3.141592653589793 2.0 1.3]

(count (into (vector-of :double) [Math/PI 2 1.3]))   ;3

;accessing elements in vector
(def a-to-j (vec (map char (range 65 75))))
a-to-j

;all these do the same
(nth a-to-j 4)  ;\E
(get a-to-j 4)  ;\E
(a-to-j 4)      ;\E

;there are subtle differences between these around:
; 1. vector is nil
(def my-vec nil)
(nth my-vec 4) ;nil
(get my-vec 4) ;nil
(my-vec 4)     ;exception

; 2. index is out of range
(def my-vec [:a :b :c])
(nth my-vec 10) ;exception
(get my-vec 10) ;nil
(my-vec 10)     ;exception

; 3.support for not found
(nth my-vec 10 :whoops) ;:whoops
(get my-vec 10 :whoops) ;:whoops
(my-vec 10 :whoops)     ;doesn't compile

(assoc a-to-j 4 "no longer E") ;[\A \B \C \D "no longer E" \F \G \H \I \J]
(replace {2 :a, 4 :b} [1 2 3 4 5 6]) ;[1 :a 3 :b 5 6]

;nb: not the best way to work with matrices
(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])

(get-in matrix [1 2])      ;6
(assoc-in matrix [1 2] 'x) ;[[1 2 3] [4 5 x] [7 8 9]]

(update-in matrix [1 2] * 100)

;poppers
(def my-stack [1 2 3])
(peek my-stack)
(pop my-stack)
(conj my-stack 4)

;idiomatic clojure code doesn't use reverse
;reverse required because conj adds to left hand side of list
(defn strict-map1 [f coll]
  (loop [coll coll, acc nil]
    (if (empty? coll)
      (reverse acc)
      (recur (next coll) (cons (f (first coll)) acc)))))

(strict-map1 - (range 5))

;no need for reverse because conj adds to the right hand side of a vector
(defn strict-map2 [f coll]
  (loop [coll coll, acc []]
    (if (empty? coll)
      acc
      (recur (next coll) (conj acc (f (first coll)))))))

(strict-map2 - (range 5))

(conj '(1) 2) ; (2 1)
(conj [1] 2)  ; [1 2]

;sub-vectors
(subvec a-to-j 3 6)

;map entry as vector
(first {:width 10, :height 11, :depth 13})
(vector? (first {:width 10, :height 11, :depth 13})) ;true

;lists
;in idiomatic clojure, lists used almost exclusively to represent code forms

;for ordered map use array map (order of insertion)
(seq (hash-map :a 1, :b 2, :c 2))
(seq (array-map :a 1, :b 2, :c 3))

;5.7: putting it all together
;implement a simple function to locate the positional index of an element within a sequence

(defn index [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll)))

(index {:a 1 :b 2 :c 3})
(index [:a :b :c])

(defn pos [e coll]
  (for [[i v] (index coll) :when (= e v)] i))

(pos 2 {:a 1 :b 2 :c 3})
(pos :c [:a :b :c])

(defn pos [pred coll]
  (for [[i v] (index coll) :when (pred v)] i))

(pos #{2} {:a 1 :b 2 :c 3})
(pos #{:b :c} [:a :b :c])
(pos even? [2 3 6 8 9 10])

;summary
"Clojure favors simplicity in the facce of growing software complexity. If problems are easily solved by collection abstractions
then those abstactions should be used. Most problems can be modeled on such simple types, yet we continue to build monolithic
class hierarchies ina fruitles race toward mirroring the 'real world' - whatever that means. Perhaps it's tie to realise 
that we no longer need to layer self-imposed complexitis ont op of software solutions that area already inherently complex.
Not only does Clojure provide the sequentioal, set, and map thypes useful for pulling ourselves form the doldrumsof softtware
complexity, but it's also optimized for dealing with them."