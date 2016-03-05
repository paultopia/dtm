; first draft of term document matrix and preprocessing.

(require '[clojure.string :as str])
(require '[clojure.walk :as walk])
(defn whitespace-split 
  "split a vector of strings into vector of vectors of strings on whitespace" 
  [preprocessed-docs] 
  (map #(str/split % #"\s") preprocessed-docs))
(defn count-strings 
  "count frequencies of strings in vector of vectors of strings" 
  [stringvecs]
  (map frequencies stringvecs))   
(defn list-strings 
  "list all strings in doc set" 
  [stringvecs]
  (distinct 
    (apply concat stringvecs)))
(defn cartesian-map 
  [stringlist] 
  (zipmap stringlist (repeat 0)))
(defn sparsify-counts 
  "based on strings in all preprocesed docs, fill counts with 0 for unused strings in each single preprocessed doc"
  [zeroes counts]
  (map #(merge-with + % zeroes) counts))
(defn unsorted-TD-map
  "split vector of preprocessed docs by spaces then make zero-filled map of counts"
  [preprocessed-docs]
  (let [stringvecs (whitespace-split preprocessed-docs)]
    (sparsify-counts 
      (-> stringvecs list-strings cartesian-map) 
      (-> stringvecs count-strings))))  
(defn TD-map
  "make a sorted document-term-map of vector of preprocessed docs with keywords"
  [preprocessed-docs]
  (walk/keywordize-keys  ; this is mainly for later flexibility
    (map #(into (sorted-map) %) (unsorted-TD-map preprocessed-docs))))
(defn TD-seqs
  "convert document-term-map into sequence of sequences"
  [tdmap]
  (cons 
    (keys (first tdmap))
    (map vals tdmap)))
(defn seqs-to-vecs 
  "sequence of sequences --> vector of vectors"
  [seqofseq]
  (into [] (map #(into [] %) seqofseq)))
(defn TD-matrix 
  "make term document matrix as vector of vectors from vector of preprocessed docs"
  [preprocessed-docs]
  (-> preprocessed-docs TD-map TD-seqs seqs-to-vecs))

; example/test

(TD-matrix ["this is a cat" "this is a dog" "woof and a meow" "woof woof woof meow meow words"])

; produces [[:cat :is :this :words :dog :and :meow :woof :a] [1 1 1 0 0 0 0 0 1] [0 1 1 0 1 0 0 0 1] [0 0 0 0 0 1 1 1 1] [0 0 0 1 0 0 2 3 0]]

