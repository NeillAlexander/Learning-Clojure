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

(defn weather-judge [temp]
  (cond
   (< temp 20) "It's cold"
   (> temp 25) "It's hot"
   :else "It's comfortable"))

(weather-judge 10)
(weather-judge 20)
(weather-judge 26)

;; recommendation to use polymorphic dispatch instead of large cond statements
;; in the same way that you would use polymorphism in Java instead of have a
;; large if / switch statement
;; see chpt 9

;; local bindings
;; "in a functional language, new values are obtained by function composition - nesting
;; multiple functions calls. Sometimes, however, it is necessary to assign a name ot the result
;; of a computation, bot ofr clarity and, if the value might be used more than once, for efficiency"
;; enter let - consists of a vector of bindings and a body expression
(let [a 2, b 3] (+ a b))

(defn seconds-to-weeks
  "Converts seconds to weeks"
  [seconds]
  (let [minutes (/ seconds 60)
        hours (/ minutes 60)
        days (/ hours 24)
        weeks (/ days 7)]
    weeks))

(double (seconds-to-weeks 929552)) ;; double to force non-ratio response


;; recursion guidelines
;; - use recursive function's arguments to store and modify progress of computation
;; - make sure it has a base case
;; - every iteration should make some progress towards base case

(defn abs
  "Calculates the absolute value of a number"
  [n]
  (if (< n 0) (* -1 n) n))

(abs -2)

(defn avg
  "returns the average of 2 arguments"
  [a b]
  (/ (+ a b) 2))

(avg 10 5)

(defn good-enough?
  "Tests if guess is close enough to real square root"
  [number guess]
  (let [diff (- (* guess guess) number)]
    (if (< (abs diff) 0.001)
      true
      false)))

(defn sqrt
  "Returns the square root of the supplied number"
  ([number]
     (sqrt number 1.0))
  ([number guess]
     (if (good-enough? number guess)
       guess
       (sqrt number (avg guess (/ number guess))))))

(sqrt 25)


;; demonstarting the need for recur
(defn add-up
  "Adds all the numbers below a given limit"
  ([limit] (add-up limit 0 0))
  ([limit current sum]
     (if (< limit current)
       sum
       (add-up limit (+ 1 current) (+ current sum)))))

(add-up 3)

;; this blows up
;;(add-up 50000)

;; replace tail call add-up to recur
(defn add-up
  "Adds all the numbers below a given limit"
  ([limit] (add-up limit 0 0))
  ([limit current sum]
     (if (< limit current)
       sum
       (recur limit (+ 1 current) (+ current sum)))))

;; now it works - the only limit is how long you want to wait
(add-up 50000)
(time (add-up 50000))   ;; 4.2 ms
(time (add-up 500000))  ;; 37.1 ms
(time (add-up 5000000)) ;; 272 ms


;; loop provides a way to define and immediately call an anonymous recursive function
;; "This is very useful, to the point where almost all uses of recur in practice are
;; couples with a loop".
(loop [i 0]
  (if (= i 10)
    i
    (recur (+ i 1))))


;; can use this to make the sqrt function more idiomatic
(defn sqrt-loop
  "Returns the square root of the supplied number"
  [number]
  (loop [guess 1.0]
     (if (good-enough? number guess)
       guess
       (recur (avg guess (/ number guess)))))) ;; binds the arg to guess

(sqrt 25)


;; do is required to execute side effects
;; from a functional point of view only the last expression has meaning
;; so this evaluates to 5
(do
  (println "hello")
  (println "hello again")
  (+ 3 2))

;; the same logic applies to a function with multiple expressions
(defn side-effects [x]
  (println "I'm going to do something")
  (println "this is a side effect")
  x)

(side-effects 10)

;; also applies to fn
(def side
  (fn [x]
    (println "another pissing side effect")
    x))

(side 3)


;; clojure has first class functions. Functions can be passed to and returned from functions.
;; "This isn't just a way of doing clever tricks with code, but a key way to structure programs".

(defn arg-switch
  "Applies the compiled function to the arguments in both possible orders"
  [fun arg1 arg2]
  (list (fun arg1 arg2) (fun arg2 arg1)))

(arg-switch str "this" "that")

;; by defining a first-class function you open it up to be used in lots of different ways
;; much better than defining something that is limited to a particular function

;; can also return functions
(defn rangechecker
  "Returns a function that determines if a number is in a provider range"
  [min max]
  (fn [num]
    (and (<= num max) (<= min num))))

(def agerange (rangechecker 5 10))
(agerange 6)
(agerange 11)
(agerange 4)


;; currying
(def times3 (partial * 3))

(times3 2)


;; composition can be used to simplify
;; these 2 functions are equivalent
(defn verbose-fn [x y]
  (- (* x y)))

(verbose-fn 3 5) ;; -15

;; returns a function that takes any number of args, multipliees them, and negates them
;; in fact this is even more general than the verbose version since you can pass any
;; number of args to *
(def succinct-fn (comp - *))

(succinct-fn 3 5) ;; -15
(succinct-fn 3 5 10) ;; -150

;; cool :-)

;; "This example should demonstrate how it is possible to use function composition and currying to create
;; arbitrarily complex functions, as long as they are definable in terms of existing functions. Using
;; currying and composition will make the intent of you code clear and keep things very succinct.
;; Often, complex multiline function definitions can be replaced with a single line of composed or curried
;; functions."

;; "Clojure encourages you to program "vertically" by building up your own control structures on top of the
;; provided primitives, rather than using them directly....By recognizing patterns particular to your program
;; or problem domain, it is possible to buld you won controls that are far more powerful than the primitive
;; structures could ever be."

;; "By the time any Clojure program reaches a certain level of complexity, if it's well designed, you should
;; find that it looks very much like a highly customized domain specific language."

;; "loop, recur, and cond are useful, but they should be the building blocks, not the substance of a program."
