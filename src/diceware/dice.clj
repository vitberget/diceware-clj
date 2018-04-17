(ns diceware.dice)

(defn dice
  []
  (inc (rand-int 6)))

(defn five-dice-str
  []
  (reduce (fn [a _]
            (str a (dice)))
          ""
          (range 5)))

(defn dice-strings
  [n]
  (reduce (fn [a _]
            (conj a (five-dice-str)))
          []
          (range n)))

(defn into-empty-map
  [strings]
  (reduce (fn [a s]
            (assoc a s nil))
          {}
          strings))
