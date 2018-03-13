(ns re-pressed.subs
  (:require [re-frame.core :as rf]))


(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub
 ::keydown-keys
 (fn [db _]
   (get-in db [:re-pressed.core/keydown :keys])))


(rf/reg-sub
 ::cards
 (fn [db]
   (get db :cards)))

(rf/reg-sub
 ::card-active-index
 (fn [db]
   (get db :card-active-index)))
