(ns re-pressed.core_demo
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-pressed.effects :as effects]
            [re-pressed.events :as events]
            [re-pressed.routes :as routes]
            [re-pressed.views :as views]
            [re-pressed.config :as config]
            [re-pressed.core :as rp]
            ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  #_(re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keypress"])
  #_(re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keyup"])
  (routes/app-routes)
  (dev-setup)
  (mount-root))
