; first draft of document term matrix

(require '[clojure.string :as str])
(def documents ["this is a cat" "this is a dog" "woof and a meow"])
(def counts (map frequencies (map #(str/split % #" ") documents)))
(def words (distinct (apply concat (map #(str/split % #" ") documents))))
(def zeroes (zipmap words (repeat 0)))
(def dtm (map #(merge-with + % zeroes) counts))