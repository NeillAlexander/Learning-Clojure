(ns practical-clojure.chpt3)

;; the fn special from in its simplest form takes 2 arguments
;; 1. a vector of argument symbols
;; 2. an expression which will be evaluated when the function is
;; called
(fn [x y] (+ x y))

;; this is what I didn't fully pick up the first time round...
;; the whole code as data thing
;; I have been trained by Java programming to regard something like
;; (defn name []) as some sacred incantation
;; in fact it is a special form that takes (in this case)
;; 2 arguments

;; (fn name? [params* ] exprs*)
;; this means
;; fn takes an optional argument name, a vector containing one or more param, and one or more expressions

;; rather than type out a function and wait for auto-complete to do
;; the work, simply do this
(doc defn)

;; variable arity
(defn add-arg-count
  "Returns the sum of the first argument and the number of additional arguments"
  [first & more]
  (+ first (count more)))

;; the reader macro for defining a function
(def sq #(* % %))

(sq 2)

(def add #(+ %1 %2))

(def sub #(- %1 %2))

(add 2 3)

(add 1 2)
