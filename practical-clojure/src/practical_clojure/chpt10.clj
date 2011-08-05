(ns practical_clojure.chpt10)

(new String) ;; would never do this
(new java.util.Date)

;; dot special form
(. (new java.util.Date) toString)

(. Integer MAX_VALUE)

;; better
(.toString (java.util.Date.))

(Integer/parseInt "101")

(int-array [1 2 3 4 5])

(into-array Comparable ["aa" "bb" "cc"]) ;; comparable array
(into-array ["aa" "bb" "cc"]) ;; string array



