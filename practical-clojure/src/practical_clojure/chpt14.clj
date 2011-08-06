(ns practical_clojure.chpt14)

(set! *warn-on-reflection* true)

(defn nth-char [s n]
  (.charAt s n))

;; prefer == to = for numbers . Doesn't require autoboxing

