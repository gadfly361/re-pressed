(ns re-pressed.effects
  (:require
   [re-frame.core :as rf]))


(rf/reg-fx
 ::alert
 (fn [message]
   (js/alert message)))


(rf/reg-fx
 ::log
 (fn [message]
   (js/console.log message)))
