; first draft of document term matrix--well, actually term document matrix.

(require '[clojure.string :as str])
(require '[clojure.walk :as walk])
(defn whitesplit 
  "split a vector of string into vector of vectors of strings on whitespace" 
  [docs] 
  (map #(str/split % #" ") docs))
(defn stringcounts 
  "count frequencies of strings in vector of vectors of strings" 
  [stringvecs]
  (map frequencies stringvecs))   
(defn liststrings 
  "list all strings in doc set" 
  [stringvecs]
  (distinct 
    (apply concat stringvecs)))
(defn makezeroes 
  [stringlist] 
  (zipmap stringlist (repeat 0)))
(defn expandcounts 
  "based on strings in all docs, fill counts with 0 for unused strings in each single doc"
  [zeroes counts]
  (map #(merge-with + % zeroes) counts))
(defn bigmap
  "split vector of docs by spaces then make zero-filled map of counts"
  [docs]
  (let [stringvecs (whitesplit docs)]
    (expandcounts 
      (-> stringvecs liststrings makezeroes) 
      (-> stringvecs stringcounts))))  
(defn terdocmmap
  "make a sorted document-term-map of vector of docs with keywords"
  [docs]
  (walk/keywordize-keys  ; this is mainly for later flexibility
    (map #(into (sorted-map) %) (bigmap docs))))
(defn tdseqs
  "convert document-term-map into sequence of sequences"
  [tdmap]
  (cons 
    (keys (first tdmap))
    (map vals tdmap)))
(defn nestvecify 
  "sequence of sequences --> vector of vectors"
  [seqofseq]
  (into [] (map #(into [] %) seqofseq)))
(defn termdocmatrix 
  "make document term matrix as vector of vectors from vector of docs"
  [docs]
  (-> docs terdocmmap tdseqs nestvecify))

; example/test

(termdocmatrix ["this is a cat" "this is a dog" "woof and a meow" "woof woof woof meow meow words"])

; produces [[:cat :is :this :words :dog :and :meow :woof :a] [1 1 1 0 0 0 0 0 1] [0 1 1 0 1 0 0 0 1] [0 0 0 0 0 1 1 1 1] [0 0 0 1 0 0 2 3 0]]

