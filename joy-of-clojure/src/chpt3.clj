(ns chpt3)

;truthiness
;everything looks true to if except false and nil
(if 1 :truthy :falsey)     ;truthy
(if [] :truthy :falsey)    ;truthy
(if nil :truthy :falsey)   ;falsey
(if false :truthy :falsey) ;falsey

;NEVER DO THIS!
(def evil-false (Boolean. "false"))
(if evil-false :truthy :falsey) ;truthy - only nil or false are false


(if (Boolean/valueOf "false") :truthy :falsey) ;this works (same as in Java)

;since nil is false, can distinguish if actually nil using nil?
(when (nil? nil) "Actually nil")

;since an empty collection is truthy in Clojure (only nil and false are falsey, remember)
;this is the idiomatic way to check if a collection is empty
(defn print-seq [s]
  (when (seq s)         ; (seq s) is the termination condition since that evaluates to nil if s is empty
    (prn (first s))
    (recur (rest s))))

(print-seq [1 2])

;this would be a better way to do the above
(defn print-seq2 [s]
  (doseq [x s]          ;uses destructuring to bind x to each element of s in sequence
    (prn x)))

(print-seq2 [1 2])

;idiomatic to assume that seq has not been called on collection, therefore always call it in the function
(seq "hello")

;destructuring - concentrate now...
;"Destructuring allows us to positionally bind locals based on an expected form for a composite data structure"
;it's a bit like pattern matching in Scala etc, but less rich
;for full pattern matching solution see clojure.core.match

;positional destructuring with a vector
(def guys-whole-name ["Guy" "Lewis" "Steele"])

(let [[f-name m-name l-name] guys-whole-name]
  (str l-name ", " f-name " " m-name))

;note that :as binds all as the collection type it is (a vector above)
;& more is bound as a seq
(let [[a b c & more :as all] (vec (range 10))] ;ampersand to bind the rest, :as to bind the whole collection
  (println "a b c are: " a b c)
  (println "more is: " more)
  (println "all is: " all))

;destructuring with a map
(def guys-name-map {:f-name "Guy" :m-name "Lewis" :l-name "Steele"})

;since it's a map we can't use a vector to pick it apart - use a map
(let [{f-name :f-name, m-name :m-name, l-name :l-name} guys-name-map] ;the new local f-name get's value by looking up key in map
  (str l-name ", " f-name " " m-name))

;other reason for having the local first, it must be a symbol or nested destructuring form
;or a special destructuring keyword
(let [{:keys [f-name m-name l-name]} guys-name-map]
  (str l-name ", " f-name " " m-name))

;if the map had strings for the keys, would use :strs
;if it had symbols, then syms

;can get at the whole map with :as
(let [{f-name :f-name :as whole-name} guys-name-map]
  whole-name)

;can provide a default for a missing value
(let [{:keys [f-name m-name l-name title], :or {title "Mr."}} guys-name-map]
  (println title f-name m-name l-name))

;also, associative destructuring
(let [{first-thing 0, last-thing 3} [1 2 3 4]]
  [first-thing last-thing])

;note that destructuring also works for function parameters

(range 5)
(for [x (range 2) y (range 2)] [x y (bit-xor x y)])

(def frame (java.awt.Frame.))

(for [method (seq (.getMethods java.awt.Frame))
      :let [method-name (.getName method)]
      :when (re-find #"Vis" method-name)]
  method-name)

(.setVisible frame true)
(.setSize frame (java.awt.Dimension. 200 200))
(javadoc frame) ;wow

(def gfx (.getGraphics frame))

(.fillRect gfx 100 100 50 75)

(.setColor gfx (java.awt.Color. 255 128 0))

(defn xors [max-x max-y]
  (for [x (range max-x), y (range max-y)]
    [x y (bit-xor x y)]))

(doseq [[x y xor] (xors 500 500)]
  (.setColor gfx (java.awt.Color. xor xor xor))
  (.fillRect gfx x y 1 1))

(.printStackTrace *e)

(defn clear [g] (.clearRect g 0 0 200 200))

(clear gfx)

(defn f-values [f xs ys]
  (for [x (range xs), y (range ys)]
    [x y (rem (f x y) 256)]))

(defn draw-values [f xs ys]
  (clear gfx)
  (.setSize frame (java.awt.Dimension. xs ys))
  (doseq [[x y v] (f-values f xs ys)]
    (.setColor gfx (java.awt.Color. v v v))
    (.fillRect gfx x y 1 1)))

((draw-values bit-and 256 256))
(draw-values * 256 256)
(draw-values / 256 256)
(draw-values + 256 256)
(draw-values - 256 256)

