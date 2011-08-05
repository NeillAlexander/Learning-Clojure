(ns practical_clojure.chpt11)

(def my-average (agent {:nums [] :avg 0}))

(defn update-average [current n]
  (let [new-nums (conj ( :nums current) n)]
    {:nums new-nums
     :avg (/ (reduce + new-nums) (count new-nums))}))


(send my-average update-average 10)
(send my-average update-average 20)
(send my-average update-average 10)
(send my-average update-average 10)

@my-average

;; agents scale very well with number of processors
;; therefore good way to parallelize program

(defn make-heavy [f]
  (fn [& args]
    (Thread/sleep 1000)
    (apply f args)))

(def +heavy (make-heavy +))

(time (+ 2 3))
(time (+heavy 2 3))

;; 3 other ways to do parallel stuff
;; pmap
(time (doall (map (make-heavy inc) [1 2 3 4 5])))  ;; slow, done in sequence
(time (doall (pmap (make-heavy inc) [1 2 3 4 5]))) ;; faster, done in parallel

;; pvalues
(pvalues (+ 4 5) (+ 2 3) (+ 3 5))

;; pcalls
(pcalls #(* 3 4) #(* 3 5))

;; also
(def my-future (future (Thread/sleep 10000)))

@my-future
