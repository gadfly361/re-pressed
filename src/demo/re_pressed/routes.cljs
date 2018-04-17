(ns re-pressed.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as gevents]
            [goog.history.EventType :as EventType]
            [re-frame.core :as rf]
            [re-pressed.events :as events]
            [re-pressed.core :as rp]
            ))


(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (rf/dispatch [::events/set-active-panel :home-panel])

    (rf/dispatch
     [::rp/set-keydown-rules
      {:event-keys [[[::events/log "Keydown was pressed"]
                     ;; enter
                     [{:which 13}]]

                    [[::events/card-move-down]
                     ;; down arrow or s
                     [{:which 40}]
                     [{:which 83}]]

                    [[::events/card-move-up]
                     ;; up arrow or w
                     [{:which 38}]
                     [{:which 87}]]
                    ]

       :clear-keys [;; escape
                    [{:which 27}]
                    ;; ctrl+g
                    [{:which   71
                      :ctrlKey true}]]

       :prevent-default-keys [;; ctrl+g
                              {:which   71
                               :ctrlKey true}]

       :always-listen-keys [;; enter
                            {:which 13}]
       }])

    #_(rf/dispatch
     [::rp/set-keypress-rules
      {:event-keys [[[::events/log "Keypress was pressed"]
                     ;; enter
                     [{:which 13}]]]

       :clear-keys [
                    ;; q
                    [{:which 113}]
                    ]
       }])

    #_(rf/dispatch
     [::rp/set-keyup-rules
      {:event-keys [[[::events/log "Keydown was pressed"]
                     ;; enter
                     [{:which 13}]]]

       :clear-keys [
                    ;; delete
                    [{:which 46}]
                    ]
       }])
    )

  (defroute "/about" []
    (rf/dispatch [::events/set-active-panel :about-panel])
    )


  ;; --------------------
  (hook-browser-navigation!))
