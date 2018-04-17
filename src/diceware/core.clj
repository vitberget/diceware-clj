(ns diceware.core
  (:gen-class)
  (:require [clojure.string :as string]
            [diceware.dice :as d]
            [diceware.file :as f]))

(defn get-replace-character
  [third-roll fourth-roll]
  (-> ["~!#$%^"
       "&*()-="
       "+[]\\{}"
       ":;\"'<>"
       "?/0123"
       "456789"]
      (nth (dec fourth-roll))
      (nth (dec third-roll))))

(defn get-valid-dice
  [v]
  (loop [d (d/dice)]
    (if (<= d (count v))
      d
      (recur (d/dice)))))

(defn get-and-map-dice-strings
  [n file]
  (let [dice-strings (d/dice-strings n)
        values (d/into-empty-map dice-strings)
        values (f/populate values file)]
    {:ds dice-strings
     :pw (->> dice-strings
              (map (fn [v] (get values v)))
              (into []))}))

(defn replace-character
  [word letter-roll third-roll fourth-roll]
  (as-> word $
        (into [] $)
        (assoc $ (dec letter-roll) (get-replace-character third-roll fourth-roll))
        (apply str $)))

(defn diceware
  [n file]
  (let [{password :pw dice-strings :ds} (get-and-map-dice-strings n file)
        word-roll (get-valid-dice password)
        word (nth password (dec word-roll))
        letter-roll (get-valid-dice word)
        third-roll (d/dice)
        fourth-roll (d/dice)
        new-word (replace-character word letter-roll third-roll fourth-roll)
        letter-password (assoc password (dec word-roll) new-word)]
    {:words-dice      dice-strings
     :letter-dice     [word-roll letter-roll third-roll fourth-roll]
     :password        (string/join " " password)
     :letter-password (string/join " " letter-password)}))

(defn -main [& args]
  (let [n (first args)
        n (if (string? n) (Integer/parseInt n) n)
        dw (diceware n (second args))
        pw (:password dw)]
    (println "Dice word:" (string/join ", " (:words-dice dw)))
    (println "Dice char:" (apply str (:letter-dice dw)))
    (println "Password option 1:" pw)
    (println "Password option 2:" (:letter-password dw))
    (when (< (count pw) 17)
      (println)
      (println "---!!! Warning, under 17 characters !!!---")
      (println))))