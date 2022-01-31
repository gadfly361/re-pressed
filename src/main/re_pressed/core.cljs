(ns re-pressed.core
  (:require
    [re-frame.core :as rf]
    [re-pressed.impl]))

(rf/reg-event-fx
  ::add-keyboard-event-listener
  ;; event-type can be:
  ;; "keydown",
  ;; "keypress", or
  ;; "keyup"
  (fn [_ [_ event-type & {:as args-map}]]
    {::keyboard-event {:event-type event-type :arguments args-map}}))

(rf/reg-event-fx
  ::set-keydown-rules
  (fn [{:keys [db]}
       [_ {:keys [event-keys
                  clear-keys
                  always-listen-keys
                  prevent-default-keys]
           :as   opts}]]
    {:db (-> db
             (assoc-in [:keyboard ::keydown :keys] nil)
             (assoc-in [:keyboard ::keydown :event-keys] event-keys)
             (assoc-in [:keyboard ::keydown :clear-keys] clear-keys)
             (assoc-in [:keyboard ::keydown :always-listen-keys] always-listen-keys)
             (assoc-in [:keyboard ::keydown :prevent-default-keys] prevent-default-keys))}))

(rf/reg-event-fx
  ::set-keypress-rules
  (fn [{:keys [db]}
       [_ {:keys [event-keys
                  clear-keys
                  always-listen-keys]
           :as   opts}]]
    {:db (-> db
             (assoc-in [:keyboard ::keypress :keys] nil)
             (assoc-in [:keyboard ::keypress :event-keys] event-keys)
             (assoc-in [:keyboard ::keypress :clear-keys] clear-keys)
             (assoc-in [:keyboard ::keypress :always-listen-keys] always-listen-keys))}))

(rf/reg-event-fx
  ::set-keyup-rules
  (fn [{:keys [db]}
       [_ {:keys [event-keys
                  clear-keys
                  always-listen-keys]
           :as   opts}]]
    {:db (-> db
             (assoc-in [:keyboard ::keyup :keys] nil)
             (assoc-in [:keyboard ::keyup :event-keys] event-keys)
             (assoc-in [:keyboard ::keyup :clear-keys] clear-keys)
             (assoc-in [:keyboard ::keyup :always-listen-keys] always-listen-keys))}))
