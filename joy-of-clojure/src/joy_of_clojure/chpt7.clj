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

;; elevator example of trampoline
(defn elevator [commands]
  (letfn                    ;; #: Local functions
      [(ff-open [[cmd & r]]   ;; #: binds cmd to the first param, and r to the rest
         "When the elevator is open on the 1st floor
          it can either close or be done."
         #(case cmd
                :close (ff-closed r)
                :done  true
                false))
       (ff-closed [[cmd & r]] ;; #: 1st floor closed
         "When the elevator is closed on the 1st floor
          it can either open or go up." 
         #(case cmd
                :open (ff-open r)
                :up   (sf-closed r)
                false))
       (sf-closed [[cmd & r]] ;; #: 2nd floor closed
         "When the elevator is closed on the 2nd floor 
          it can either go down or open."
         #(case cmd
                :down (ff-closed r)
                :open (sf-open r)
                false))
       (sf-open [[cmd & r]] ;; #: 2nd floor open
         "When the elevator is open on the 2nd floor
          it can either close or be done"
         #(case cmd
                :close (sf-closed r)
                :done  true
                false))]
    (trampoline ff-open commands)))

(defn destr-test [[cmd & r]]
  (println cmd)
  (println r))

;; rules for mutual recursion
;; 1. Make all of the functions participating in the mutual recursion return a function instead of their normal result
;; 2. Invoke the first function in the mutual chain via the trampoline function

;; the trampoline function takes care of the stack while the mutual recursion carries on
;; typical use case is a state machine, of which the elevator function above is an example
