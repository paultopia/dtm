; first draft of document term matrix

(require '[clojure.string :as str])
(def documents ["this is a cat" "this is a dog" "woof and a meow"])
(def counts (map frequencies (map #(str/split % #" ") documents)))
(require '[clojure.set :as set])
(def words (apply set/union (map #(str/split % #" ") documents)))