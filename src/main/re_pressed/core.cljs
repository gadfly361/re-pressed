(ns re-pressed.core
  (:require
   [re-frame.core :as rf]
   [re-pressed.impl]
   ))



(rf/reg-event-fx
 ::add-keyboard-event-listener
 ;; event-type can be:
 ;; "keydown",
 ;; "keypress", or
 ;; "keyup"
 (fn [_ [_ event-type]]
   {::keyboard-event {:event-type event-type}}))


(rf/reg-event-fx
 ::set-keydown-event
 (fn [{:keys [db]}
      [_ {:keys [event-keys
                 clear-keys
                 prevent-default-keys]
          :as   opts}]]

   {:db (-> db
            (assoc-in [::keydown :event-keys] event-keys)
            (assoc-in [::keydown :clear-keys] clear-keys)
            (assoc-in [::keydown :prevent-default-keys] prevent-default-keys)
            )}))


(rf/reg-event-fx
 ::set-keypress-event
 (fn [{:keys [db]}
      [_ {:keys [event-keys
                 clear-keys]
          :as   opts}]]

   {:db (-> db
            (assoc-in [::keypress :event-keys] event-keys)
            (assoc-in [::keypress :clear-keys] clear-keys)
            )}))


(rf/reg-event-fx
 ::set-keyup-event
 (fn [{:keys [db]}
      [_ {:keys [event-keys
                 clear-keys]
          :as   opts}]]

   {:db (-> db
            (assoc-in [::keyup :event-keys] event-keys)
            (assoc-in [::keyup :clear-keys] clear-keys)
            )}))
