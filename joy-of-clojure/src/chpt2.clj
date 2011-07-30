(ns chpt2
  (:require [clojure.set :as s])
  (:import [java.util.concurrent.atomic AtomicLong]))

; ALT-SHIFT-UP to select the enclosing form
; CTRL-ENTER to send to the REPL
; CTRL-ALT-X to evaluate the current top level form

; keywords
:chpt2/test
(keyword "chpt2" "test")

; strings
"this is a string"

"this is a 
multiline
string"

; characters
\a
\u30DE ; unicode katakana character

; lists
'("one" "two" "three") ;quoted to prevent first arg evaluated as function, macro, or special form
(quote ("one" "two" "three")) ;same as above but using the quote function

; vectors
[1 2 3 4] ; note, no evaluation of first form, which presumably is why they are used for args in defn

;maps
{1 "one" 2 "two"}
{1, "one", 2, "two"} ; note that commas are whitespace in clojure
{(+ 0 1) "one" 2 "two"} ; note that every map literal is evaluated

; sets
#{1 1 2 3} ;; duplicate key not allowed
#{1 2 3}

;functions
((fn [x y] #{x y}) 1 2) ; anonymous function, with invokation

;overloading / arity
((fn
  ([x] #{x})
  ([x y] #{x y})) 32)

;varargs
((fn [x y & z] #{x y z}) 1 2 3 4 5) ;note z is a list with 3,4,5

;more useful
(def make-a-set
  (fn [x y & z] #{x y z}))

(make-a-set 1 2 3 4 5)

;even better
(defn make-a-set
  "This is a documentation string (optional"
  ([x] #{x})
  ([x y] #{x y})
  ([x y & z] #{x y z}))

(make-a-set 1)
(make-a-set 1 2) 
(make-a-set 1 2 3 4 5)

;reader-feature for anonymous function
(def make-a-list #(list %1 %2))
(def make-a-list #(list %1 %2 %&)) ;with varargs

(make-a-list 1 2 3 4 5)

;vars
(def x 42) ;root binding across all threads (can be re-bound in individual threads)
(.start (Thread. #(println "Answer: " x))) ;prints 42 to the console


;do - all forms evaluated, but only the last one returned
(def z 
  (do 
  (+ 1 2)
  (print "hello") ;side effect
  42))

z ;is 42

;let - create locals (not variable)
(let [r 5
      pi 3.1415
      r-squared (* r r)]
  (println "radius is" r) ;this bit (the body) is known as an implicit do
  (* pi r-squared))

;loops
;recur - get's round lack of tail call optimization
;the call to recur:
;1. rebinds x (the argument to the function)
;2. returns control to the top of the function
;NB: when provides an implicit do i.e. when x is positive, do these 2 things
(defn print-down-from [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))

(print-down-from 10)

;if the function has multiple arguments then so must the recur
(defn sum-down-from [sum x]
  (if (pos? x)                   ;if
    (recur (+ sum x) (dec x))    ;then
    sum))                        ;else

(sum-down-from 0 10)

(defn sum-down-from [initial-x]
  (loop [sum 0, x initial-x]       ;defines the initial binding for the loop (comma is whitespace, remember)
    (if (pos? x)
      (recur (+ sum x) (dec x))    ;recurs always loops back to closest enclosing loop or fn (loop locals rebound to new values)
      sum)))

(sum-down-from 10)

;recur can only be used in the tail position. what's that exactly?
;a form is in the tail position of an expression when it's value may be the return value of the whole expression
(defn absolute-value [x]
  (if (pos? x)
    x           ;tail pos
    (- x)))     ;tail pos - both are possible return values (depending on the value of x)

(absolute-value 3)
(absolute-value -3)

;quoting - demonstrating homiconicity. now you see how macros would be possible
(quote (defn absolute-value [x]
  (if (pos? x)
    x           ;tail pos
    (- x))))

;there are 2 types of quote
; 1. quote
; 2. syntax quote

;but first...
;evaluation
(cons 1 [2 3])  ;constructs a new sequence with the first arg in front of the sequence provided by the second arg
(cons 1 2 3)    ;this doesn't work (needs sequence)
(cons 1 '(2 3)) ;synax-quote (?) required to prevent list from being evaluated as function
(cons (- 2 1) [2 3]) ;demonstrating that all forms are evaluated (when not quoted)
(cons 1 [(+ 1 1) (+ 1 2)]) ;again, the function calls are evaluated
[2 3] ;vector / map / scalar all evaluate to themselves

;evaluation of special forms
;each special form has it's own evaluation rules
(def tena 9)
tena
(quote tena)
'tena

;NB: SINGLE QUOTE IS USED LESS IN CLOJURE
;WARNING, WARNING, NON-IDIOMATIC CODE
'this-is-non-idiomatic ;and a bit untidy if you ask me
;PREFER
(quote this-is-much-better)

;the empty list evaluates to itself - non-idiomatic to quote
()

;syntax-quote is something different again
; 1. quote and ' are equivalent
; 2. syntax quote is a backtick `
`(1 2 3)

;syntax quote automatically qualifies all unqualified symbols
`map
`(map even? [1 2 3])
(eval `(map even? [1 2 3])) ;nasty

;if it doesn't exist, then uses current namespace
`blah ;chpt2/blah

;this behaviour is required for macros...

;as is this...
;unquote
`(1 2 ~(+ 2 1)) ;within syntax quote unquote says 'evaluate this form' - (1 2 3)

;I really like the fact that JOC is introducing syntax-quote, unquote etc. before macros, rather than conflating the two.

;compare these
(let [x '(2 3)] `(1 ~x))   ; (1 (2 3)
(let [x '(2 3)] `(1 ~@x))  ; (1 2 3)

;~@ tells Clojure to unpack the sequence - its called unquote-splicing

;auto-gensym - create a new unqualified symbol
`new-sym#

;Java interop

;statics
(Math/sqrt 9)
(java.util.Locale/JAPAN)

;new instances
(new java.util.HashMap {"foo" 32 "bar" 34})

;idiomatically expressed as
(java.util.HashMap. {"foo" 32 "bar" 34})

;access a property
(.x (java.awt.Point. 10 20)) ;10

;access an instance method
(.divide (java.math.BigDecimal. "32") 2M)

;to set a public property (if there is no setter)
(let [origin (java.awt.Point. 0 0)]
  (set! (.x origin) 15)
  (str origin))

;chaining calls
(.. (java.util.Date.) toString (endsWith "20"))
(.. (StringBuilder.) (append "this") (append " is a test") toString) ;nice

;nicer?
(doto (StringBuilder.)
  (.append "another")
  (.append " test")
  (.toString))

;exceptions
;throwing
(throw (Exception. "uh oh"))

;try catch
(defn throw-catch [f]
  (try 
    (f)
    (catch ArithmeticException e "No dividing by zero")
    (catch Exception e (str "other exception: " (.getMessage e)))
    (finally (println "returning..."))))

(throw-catch #(/ 10 5))
(throw-catch #(/ 10 0))
(throw-catch #(throw (Exception. "foo")))

;namespaces (see top of file)
(s/intersection #{1 2 3} #{3 4 5})  ; (:require [clojure.set :as s])

(AtomicLong. 42)

;specifying exactly the vars that you want to use is good practice
(ns chpt2.demo
  (:use [clojure.string :only [capitalize reverse]]))
(map capitalize ["kilgore" "trout"])

(reverse "abc")