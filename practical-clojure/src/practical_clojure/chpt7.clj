(ns practical_clojure.chpt7)

(defn hello [who]
  (println (str "Hello " who)))

(sort (keys (ns-publics 'clojure.core)))
