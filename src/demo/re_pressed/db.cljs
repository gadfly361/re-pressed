(ns re-pressed.db)

(def default-db
  {:name "re-frame"

   :cards             (mapv #(str "card" %) (range 8))
   :card-active-index 0
   })
