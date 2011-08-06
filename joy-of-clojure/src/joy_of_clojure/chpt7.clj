(ns joy_of_clojure.chpt7)

;vector as function
(map [:cthon :phthor :beowulf :grendel] #{0 3})

(take 5 (repeat rest)) ;builds a list of 5 rest functions

(cons first (take 5 (repeat rest))) ; (first rest rest rest rest rest)

(defn fnth [n]
  (apply comp
         (cons first
               (take (dec n) (repeat rest)))))

((fnth 5) '[a b c d e]) ;builds function, then applies it to the vector

((partial + 23) 100 200)

(def incrementer (partial + 1))

(incrementer 100)

;a higher order function takes one or more functions as arguments, and returns a function
(def plays [{:band "Burial" :plays 979 :loved 9}
            {:band "Eno" :plays 2333 :loved 15}
            {:band "Bill Evans" :plays 979 :loved 9}
            {:band "Magma" :plays 2665 :loved 31}])

(def sort-by-loved-ratio
  (partial sort-by #(/ (:plays %) (:loved %))))

(sort-by-loved-ratio plays)

(defn columns [column-names]
  (fn [row]
    (vec (map row column-names))))

(sort-by (columns [:plays :loved :band]) plays)

;closures
(defn times-n [n]
  (let [x n]
    (fn [y] (* y x))))

(def times-4 (times-n 4))

(times-4 3)

(defn times-n [n]
  (fn [y] (* y n)))

(def times-3 (times-n 3))

(times-3 2)
