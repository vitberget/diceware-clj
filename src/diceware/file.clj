(ns diceware.file
  (:require [clojure.string :as string]))

(defn populate
  [dice-string-map file]
  (let [reader (clojure.java.io/reader file)]
    (loop [map dice-string-map]
      (if (not (contains? (into #{} (vals map)) nil))
        map
        (if-let [line (.readLine reader)]
          (let [[key val] (string/split line #"\s" 2)]
            (recur (if (contains? map key)
                     (assoc map key val)
                     map)))
          map)))))
