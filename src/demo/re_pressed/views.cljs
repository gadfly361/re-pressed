(ns re-pressed.views
  (:require
   [re-frame.core :as rf]
   [re-pressed.subs :as subs]
   [garden.core :refer [css]]
   [re-pressed.core :as rp]

   ))


(defn style [card-active-index]
  [:style
   (css
    [
     [:* {:font-family "sans-serif"}]
     [:.card-outer-container
      {:position "relative"
       :height   "316px"
       :width    " 120px"
       :overflow "hidden"}
      [:.card-container
       {:position "absolute"
        :transition "top 0.5s linear"
        :top      (str  (or (* -44
                               (if (= 0 card-active-index)
                                 0
                                 (dec card-active-index)))
                            0)
                       "px")
        }
       [:.card {:padding       "8px"
                :border        "solid 1px grey"
                :border-radius "4px"
                :width         "100px"
                :text-align    "center"
                :margin-top    "8px"
                :background-color "white"
                :transition "background-color 0.3s linear"
                }
        [:&.active {:background-color "skyblue"}]]
       ]]
     ])])



;; home

(defn home-panel []
  (let [keydown-keys      @(rf/subscribe [::subs/keydown-keys])
        cards             @(rf/subscribe [::subs/cards])
        card-active-index @(rf/subscribe [::subs/card-active-index])]
    [:div
     [:h1 "Home Panel"]

     [:input]

     [style card-active-index]

     [:pre (with-out-str (cljs.pprint/pprint
                          (last keydown-keys)))]

     [:div.card-outer-container
      [:div.card-container
       (doall
        (for [[i card] (map-indexed vector cards)]
          ^{:key card}
          [:div.card
           {:class (when (= i card-active-index)
                     "active")}
           card]))
       ]]

     ]))



;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (rf/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
