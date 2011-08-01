(ns practical_clojure.chpt6)

;; difference between state and identity
;; state is a value associated with an identity at a particular point in time
;; very non-Aristotelian :-)

;; each of the reference types in Clojure models an identity and points to a state
;; over time they will point to a whole series of states

(def my-ref (ref 5))

(dosync (ref-set my-ref 6))
(dosync (alter my-ref + 3))

;; if the value were updated then the new value would be used
;; only use if the function passed is commutative (can be applied in any order)
(dosync (commute my-ref + 3))

;; provies a way to include a ref in a transaction even if you don't want to change the value
;; in other words, prevent someone else from changing it
(dosync (ensure my-ref))

;; bank accounts
(def account1 (ref 1000))
(def account2 (ref 1500))

(defn transfer
  "transfers amount of money from a to b"
  [a b amount]
  (dosync
   (alter a - amount)
   (alter b + amount)))

(transfer account1 account2 300)

(println @account1)
(println @account2)


;; atoms
(def my-atom (atom 5))
(swap! my-atom + 3)
(reset! my-atom 1)


;; agents
(def my-agent (agent 5))
(send my-agent + 3) ;; updated at some future point (asynchronously)

;; for io intensive (which this example clearly isn't)
(send-off my-agent + 3)

(error-mode my-agent) ;; fail
(set-error-mode! my-agent :fail)


(def an-agent (agent 10))
(send an-agent / 0)
(send an-agent + 1) ;; throws exception
(agent-error an-agent) ;; ArithmeticException
(restart-agent an-agent 5 :clear-actions true)

;; must call (shutdown-agents) to enable the program to terminate
;; shuts down the thread pool
;; this is non-reversible


;; validation
(def my-ref (ref 5))
(set-validator! my-ref (fn [x] (> x 0)))
(dosync (alter my-ref - 10))


;; watches
(defn my-watch [key identity old-val new-val]
  (println (str "Old: " old-val))
  (println (str "New: " new-val)))

(add-watch my-ref "watch1" my-watch)

(dosync (alter my-ref - 1))
(remove-watch my-ref "watch1")
