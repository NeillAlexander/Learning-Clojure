(ns chpt6)

;shared structure
(def baselist (list :barnabas :adam))
(def lst1 (cons :willie baselist))
(def lst2 (cons :phoenix baselist))

lst1
lst2

(identical? (next lst1) (next lst2)) ;identical - share structure (baselist)
(identical? baselist (next lst1))
(identical? baselist (next lst2))

;building trees with shared structure
(defn xconj [t v]
  (cond
    (nil? t) {:val v, :L nil, :R nil}))

(xconj nil :a) ;{:val :a, :L nil, :R nil}

;support insert into non-empty
(defn xconj [t v]
  (cond
    (nil? t) {:val v, :L nil, :R nil}
    (< v (:val t)) {:val (:val t)
                    :L (xconj (:L t) v)
                    :R (:R t)}))

(def tree1 (xconj nil 5)) ;{:val 5, :L nil, :R nil}

(def tree1 (xconj tree1 3))
(def tree1 (xconj tree1 2)) ;{:val 5, :L {:val 3, :L {:val 2, :L nil, :R nil}, :R nil}, :R nil}

;convert to seq in order
(defn xseq [t]
  (when t
    (concat (xseq (:L t)) [(:val t)] (xseq (:R t)))))

(xseq tree1) ; (2 3 5)


(defn xconj [t v]
  (cond
    (nil? t) {:val v, :L nil, :R nil}
    (< v (:val t)) {:val (:val t)
                    :L (xconj (:L t) v)
                    :R (:R t)}
    :else {:val (:val t),
           :L (:L t)
           :R (xconj (:R t) v)}))

(def tree1 (xconj tree1 10))
(xseq tree1)  ;(2 3 5 10)
tree1         ;{:val 5, :L {:val 3, :L {:val 2, :L nil, :R nil}, :R nil}, :R {:val 10, :L nil, :R nil}}

(def tree2 (xconj tree1 7))
(identical? (:L tree1) (:L tree2))

;laziness
(defn rec-step [[x & xs]]
  (if x
    [x (rec-step xs)]
    []))

(rec-step (range 200000)) ;stackoverflow

;defer and force (implementing laziness)
(defn defer-expensive [cheap expensive]
  (if-let [good-enough (force cheap)] ;binds good-enough to true if it's truthy
    good-enough
    (force expensive)))

(defer-expensive (delay :cheap)
  (delay (do (Thread/sleep 500)
           :expensive)))

(defer-expensive (delay false)
  (delay (do (Thread/sleep 500) :expensive)))


;lazy quicksort
(defn nom [n] (take n (repeatedly #(rand-int n))))

;um, huh?
(defn sort-parts [work]
  (lazy-seq
    (loop [[part & parts] work]
      (if-let [[pivot & xs] (seq part)]
        (let [smaller? #(< % pivot)]
          (recur (list*
                   (filter smaller? xs)
                   pivot
                   (remove smaller? xs)
                   parts)))
        (when-let [[x & parts] parts]
          (cons x (sort-parts parts)))))))

(defn qsort [xs]
  (sort-parts (list xs)))

(qsort (nom 20))