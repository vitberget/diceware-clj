(ns diceware.core
  (:gen-class)
  (:require [clojure.string :as string]
            [diceware.dice :as d]
            [diceware.file :as f]))

(defn diceware
  [n file]
  (let [dice-strings (d/five-dice-strings n)
        values (d/into-empty-map dice-strings)
        values (f/populate values file)]
    {:dice     dice-strings
     :password (->> dice-strings
                    (map (fn [v] (get values v)))
                    (string/join " "))}))

(comment
  (diceware 5 "resources/diceware-sv.txt")
  )

(defn -main [& args]
  (let [dw (diceware (Integer/parseInt (first args))
                     (second args))
        pw (:password dw)]
    (println "Dice:" (string/join ", " (:dice dw)))
    (println "Password:" pw)
    (when (< (count pw) 17)
      (println)
      (println "---!!! Warning, under 17 characters !!!---")
      (println))))