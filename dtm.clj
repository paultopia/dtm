; first draft of document term matrix

(require '[clojure.string :as str])
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

