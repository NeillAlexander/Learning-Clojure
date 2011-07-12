(ns chpt5)

;persistent data structures

;array is not persistent
(def ds (into-array [:willie :barnabas :adam]))

(seq ds)

(aset ds 1 :quentin)
(seq ds)

;clojure data structures are
(def ds [:willie :barnabas :adam])
ds

(def ds1 (replace {:barnabas :quentin} ds))
ds1