(ns re-pressed.events
  (:require
   [re-frame.core :as rf]
   [re-pressed.db :as db]
   [re-pressed.effects :as effects]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))


(rf/reg-event-fx
 ::alert
 (fn [_ [_ message]]
   {::effects/alert message}))

(rf/reg-event-fx
 ::log
 (fn [_ [_ message]]
   {::effects/log message}))


(rf/reg-event-db
 ::card-move-up
 (fn [db _]
   (let [cards              (get db :cards)
         cards-n            (count cards)
         card-active-index (get db :card-active-index)
         updated-index      (if (= card-active-index 0)
                              (dec cards-n)
                              (dec card-active-index))]
     (assoc db :card-active-index updated-index))))

(rf/reg-event-db
 ::card-move-down
 (fn [db _]
   (let [cards         (get db :cards)
         cards-n       (count cards)
         card-active-index (get db :card-active-index)
         updated-index (if (= card-active-index (dec cards-n))
                         0
                         (inc card-active-index))]
     (assoc db :card-active-index updated-index))))
