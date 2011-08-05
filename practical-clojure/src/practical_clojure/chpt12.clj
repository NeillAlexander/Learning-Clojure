(ns practical_clojure.chpt12)

;; homoiconicity - all code is data, all data is code or
;; "The language's code is represented in terms of the language's data structures"

(println "Hello world")  ;; code
'(println "Hello world") ;; data

;; metaprogramming is no more difficult than creating a list

;; a macro returns a form
;; you are telling clojure to replace the macro with the returned form

(defmacro triple-do [form]
  (list 'do form form form))

(triple-do (println "Hello"))

(defmacro infix [form]
  (cons (second form) (cons (first form) (nnext form))))

(macroexpand  '(infix (2 + 3)))

;; you can run tests against the output of macroexpand to make sure macros work as expected

;; that's all a bit tiresome. to make it easier to generate the macros, there is a built-in
;; macro templating system
(defmacro triple-do [form]
  `(do ~form ~form ~form))

(macroexpand '(triple-do (println "Hello")))

;; note the need for the splicing unquote, since nnext evaluate to a list giving (+1 (2))
;; splice it to get the values out
(defmacro infix [form]
  `(~(second form) ~(first form) ~@(nnext form)))

(macroexpand '(infix (1 + 2)))

;; first determine what we want the generated code to look like
(let [result (/ 4 3)]
  (println (str "Value is: " result))
  result)

(defmacro debug-println [expr]
  `(let [result# ~expr]
     (println (str "Value is: " result#))
     result#))

(macroexpand '(debug-println (/ 3 4)))

(debug-println (/ 3 4))

;; the best way to use macros is to use them as little as possible
;; useful for:
;; implementing control structures
;; wrap def or defn
;; performance
;; codify re-occurring patterns


(defn xml-helper [form]
  (if (not (seq? form))
    (str form)
    (let [name (first form)
          children (rest form)]
      (str "<" name ">"
           (apply str (map xml-helper children))
           "</" name ">"))))

(defmacro xml [form]
  (xml-helper form))

(xml
 (book
  (authors
   (author "Luke")
   (author "Stuart"))))
