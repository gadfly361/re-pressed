# re-pressed

Re-pressed is a library that handles keyboard events
for [re-frame](https://github.com/Day8/re-frame) applications.

**Note**: For now, this library should be considered *alpha quality*.

![re-pressed gif not found](re-pressed.gif)

## Usage

Create a new re-frame application.

```
lein new re-frame foo
```

Add the following to the `:dependencies` vector of your *project.clj*
file.

```clojure
[re-pressed "0.1.0-alpha"]
```

### The `::rp/add-keyboard-event-listener` event

Then require re-pressed in the core namespace, and add the
`::rp/add-keyboard-event-listener` event.

```clojure
(ns foo.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
			
            ;; Add this (1 of 2)
            [re-pressed.core :as rp]

            [foo.events :as events]
            [foo.views :as views]
            [foo.config :as config]
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
  
  ;; And this (2 of 2)
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])

  (dev-setup)
  (mount-root))
```

Notes:

- You can pass `"keydown"`, `"keypress"`, or `"keyup"` to
  `::rp/add-keyboard-event-listener`.
- You need to dispatch `::rp/add-keyboard-event-listener` from the
  `init` function instead of `mount-root`. `init` is ran **once** when
  the application loads, and `mount-root` runs **everytime** figwheel
  updates the application. This is significant, because you only want
  to add one event listener!

### The `::rp/set-keydown-event`, `::rp/set-keypress-event`, and  `::rp/set-keyup-event` events

Next, to use re-pressed, you will need to dispatch a
`::rp/set-keydown-event`, `::rp/set-keypress-event`, or
`::rp/set-keyup-event` event somewhere.  Personally, I like
dispatching this in my routes file (because I may want to handle
keyboard events differently on each page).

Here is an example, using the `::rp/set-keydown-event` event.

```clojure
(re-frame/dispatch
 [::rp/set-keydown-event
  {;; takes a collection of events followed by key combos that can trigger the event
   :event-keys [
                ;; Event & key combos 1
                [;; this event
                 [:some-event-id1]
                 ;; will be triggered if
                 ;; enter
                 [{:which 13}]
                 ;; or delete
                 [{:which 46}]]
                ;; is pressed

                ;; Event & key combos 2
                [;; this event
                 [:some-event-id2]
                 ;; will be triggered if
                 ;; tab is pressed twice in a row
                 [{:which 9} {:which 9}]
                 ]]

   ;; takes a collection of key combos that, if pressed, will clear the recorded keys
   :clear-keys
   ;; will clear the previously recorded keys if
   [;; escape
    [{:which 27}]
    ;; or Ctrl+g
    [{:which   71
      :ctrlKey true}]]
   ;; is pressed

   ;; takes a collection of key combos that, if pressed, will prevent the default browser action
   ;; (note: this is only available to keydown)
   :prevent-default-keys
   ;; will prevent the browser default action if
   [;; Ctrl+g
    [{:which   71
      :ctrlKey true}]]
    ;; is pressed
   }])
```

For `:event-keys`, `:clear-keys`, and `:prevent-default-keys`, the
keys in the vector of key combos take the following shape:

```clojure
{:which    <int>
 :altKey   <boolean>
 :ctrlKey  <boolean>
 :metaKey  <boolean>
 :shiftKey <boolean>
 }
```

For `:event-keys`, the event will be called with a few things *conj*ed
on to the end of the event vector. For example:

```
;; this event
[:some-event-id1]

;; will be dispatched as
[:some-event-id1 js-event keyboard-keys]
```

Where:

- `js-event` is the javascript event of the most recently pressed key
- `keyboard-keys` is a collection of the recently pressed keys taking
  the shape of the clojurescript hash-map described above.

## Gotchas

- For keypress events, you only have access to things like letters and
  numbers. This is unlike keydown and keyup events, where you have
  access to more things like the Escape key.
- Using `:prevent-default-keys` only works on keydown-events. This is
  because the default action will happen before keypress and keyup
  events happen.
- Certain browser default actions cannot be overwritten, like Ctrl+n
  in chrome.

## Questions

If you have questions, I can usually be found hanging out in
the [clojurians](http://clojurians.net/) #reagent slack channel (my
handle is [@gadfly361](https://twitter.com/gadfly361)).

## License

Copyright © 2018 Matthew Jaoudi

Distributed under the The MIT License (MIT).
