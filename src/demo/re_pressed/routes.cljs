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
                     [{:keyCode 13}]]

                    [[::events/log "Hello world"]
                     [;; h
                      {:keyCode 72}
                      ;; w
                      {:keyCode 87}]]

                    [[::events/card-move-down]
                     ;; down arrow or s
                     [{:keyCode 40}]
                     [{:keyCode 83}]]

                    [[::events/card-move-up]
                     ;; up arrow or w
                     [{:keyCode 38}]
                     [{:keyCode 87}]]
                    ]

       :clear-keys [;; escape
                    [{:keyCode 27}]
                    ;; ctrl+g
                    [{:keyCode 71
                      :ctrlKey true}]]

       :prevent-default-keys [;; ctrl+g
                              {:keyCode 71
                               :ctrlKey true}]

       :always-listen-keys [;; enter
                            {:keyCode 13}]
       }])

    #_(rf/dispatch
     [::rp/set-keypress-rules
      {:event-keys [[[::events/log "Keypress was pressed"]
                     ;; enter
                     [{:keyCode 13}]]]

       :clear-keys [
                    ;; q
                    [{:keyCode 113}]
                    ]
       }])

    #_(rf/dispatch
     [::rp/set-keyup-rules
      {:event-keys [[[::events/log "Keydown was pressed"]
                     ;; enter
                     [{:keyCode 13}]]]

       :clear-keys [
                    ;; delete
                    [{:keyCode 46}]
                    ]
       }])
    )

  (defroute "/about" []
    (rf/dispatch [::events/set-active-panel :about-panel])
    )


  ;; --------------------
  (hook-browser-navigation!))
