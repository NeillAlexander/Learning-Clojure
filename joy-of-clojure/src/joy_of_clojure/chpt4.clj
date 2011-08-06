(ns joy_of_clojure.chpt4)


(defn print-val-and-class [val]
  (println (class val))
  val)

;use M to activate big numbers
(print-val-and-class 3.13234123421341234123412341251234534534512345345145M)

;else truncated, since Java can't represent
(print-val-and-class 3.13234123421341234123412341251234534534512345345145)

;automatic promotion
(print-val-and-class 9)
(print-val-and-class 90000000000000000)
(print-val-and-class 90000000000000000000000000)

;spot the double
(print-val-and-class (+ 0.1M 0.1M 0.1M 0.1 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M)) ;10 * 0.1 != 1, when there is a double

(+ 1/3 2/3) ;rational numbers
(print-val-and-class (rationalize 0.1))

(time (+ 1/3 2/3)) ;slower than integer arithmetic
(time (+ 1 2))

;keywords always evaluate to themselves, making them a good choice for map keys
:keyword

;they also act as functions, taking a map as argument
(def m {:zombies 2000 :humans 9})

;works both ways
(:zombies m)
(m :zombies)

;attaching meta-data
;x and y contain equal symbols (goat) but are not identical, demonstrated by having different meta data
(let [x (with-meta 'goat {:ornery true})
      y (with-meta 'goat {:ornery false})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])

(def meta-function (with-meta print-val-and-class {:has-meta true}))

(meta-function 1)
(meta meta-function)

;clojure is a lisp-1 - uses same name resolution for function and value bindings
;one downside to this is name shadowing i.e. defining a function or value that
;hides (shadows) and existing function

;onwards to...

;regular expressions
(def re #"a regular expression")
(print-val-and-class re)           ;java.util.regex.Pattern

;re-seq - returns sequence of all matches in a string
(re-seq #"\w+" "one-two/three")

;avoid re-matcher, re-groups, and re-find
;they make use of Matcher, which is mutable and can therefore lead to threading issues
;see clojure.contrib.string for functions to use

