# re-pressed (asynch)

> **Arthur**: Shut up, will you, SHUT UP!
> **Man**:	Aha!  Now we see the violence inherent in the system!
> **Arthur**: SHUT UP!
> **Man**:	Come and see the violence inherent in the system!	HELP, HELP, I'M BEING REPRESSED!
> **Arthur**: Bloody PEASANT!
> **Man**:	Oh, what a giveaway!  Did'j'hear that, did'j'hear that, eh?  That's what I'm all about!  Did you see 'im repressing me?  You saw it, didn't you?!
> - Monty Python and the Holy Grail

Re-pressed is a library that handles keyboard events
for [re-frame](https://github.com/Day8/re-frame) applications. This is a fork from https://github.com/gadfly361/re-pressed. The implementation is more re-frame friendly, and has no warnings of `re-frame: Subscribe was called outside of a reactive context` (see [issue](https://github.com/gadfly361/re-pressed/issues/13)), and it uses `dispatch` instead of `dispatch-synch`.

```clojure
[org.clojars.betontalpfa/re-pressed "0.4.0"]
```

Note: if you are upgrading re-pressed from an earlier version, there was a breaking change - all instances of `:which` should be replaced with `:keyCode`. However, the upside is re-pressed no longer relies on jQuery!

And in your ns:
```clojure
(ns your-ns
  (:require [re-pressed.core :as rp]))
```

![re-pressed gif not found](re-pressed.gif)

# The Problem

If you aren't careful, it is easy to add a bunch of keyboard event
listeners scattered throughout your application. When these listeners
collide, this can lead to unexpected and hard to debug behavior.

In addition, the current state of how to identify a keyboard event in
a cross-browser compatible way can be quite cumbersome ... you will
likely be asking yourself, "Should I use keyCode, key, which, etc?".

# Re-pressed's Solution

With re-pressed, you only set up one keyboard event listener when your
application starts, with `::rp/add-keyboard-event-listener`. However,
that does not mean that you are locked in to one set of rules for how
to handle keyboard events. By dispatching `::rp/set-keydown-rules`,
`::rp/set-keypress-rules`, or `::rp/set-keyup-rules`, you can update
the rules dynamically.

In addition, Google Closure is able to ensure cross-browser compatibility with
their `keyCode` attribute. Re-pressed trusts that Google Closure will do a good
job at keeping this current and uses it under the hood.  The list of keycodes
can be found [here](https://github.com/google/closure-library/blob/master/closure/goog/events/keycodes.js#L27).

# API

### `::rp/add-keyboard-event-listener`

`::rp/add-keyboard-event-listener` adds the keyboard event listener to
your application. Needs to be dispatched **only once**, when the
application *first* loads.

There are three options, and you can use more than one if you'd like:

```clojure
(re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])

;; or
(re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keypress"])

;; or
(re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keyup"])
```

### Event Listeners Options

 `::rp/add-keyboard-event-listener` can have optional values to set
 behaviors of event-listeners after the event-type.

 Such as;

 #### Clear `keydown-keys` with Matched Event

 `:clear-on-success-event-match` option is used to remove leftover
 key-presses after a success match of events.

 Default: `false`.

 ``` clojure
 (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown" :clear-on-success-event-match true])

 ;; or
 (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keypress" :clear-on-success-event-match true])

 ;; or
 (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keyup" :clear-on-success-event-match true])

 ```


### `::rp/set-keydown-rules`

`::rp/set-keydown-rules` takes a hash-map of `:event-keys`,
`:clear-keys`, `:always-listen-keys`, and `:prevent-default-keys` and
listens for *keydown* events.

- For `:event-keys`, there is a vector of *event + key combo* vectors.
  If any of the key combos are true, then the event will get
  dispatched.
- For `:clear-keys`, there is a vector of *key combo* vectors. If any
  of the key combos are true, then the recently recorded keys will be
  cleared.
- For `:always-listen-keys`, there is a vector of just *keys*. If any
  of the keys are pressed, then that key will always be recorded by
  re-pressed. *By default, keys are ignored when pressed inside of an input, select, or textarea.*
- For `:prevent-default-keys` there is a vector of just *keys*. If any
  of the keys are pressed, then the default browser action for that
  key will be prevented.

This is a description of the shape:

```clojure
(re-frame/dispatch
 [::rp/set-keydown-rules
  {:event-keys [
                [<event vector>
                 <key-combo vector>
                 ...
                 <key-combo vectorN>]
                ]

   :clear-keys [<key-combo vector>
                ...
                <key-combo vectorN>]

   :always-listen-keys [<key>
                        ...
                        <keyN>]

   :prevent-default-keys [<key>
                          ...
                          <keyN>]

   }])
```

Here is an example:

```clojure
(re-frame/dispatch
 [::rp/set-keydown-rules
  {;; takes a collection of events followed by key combos that can trigger the event
   :event-keys [
                ;; Event & key combos 1
                [;; this event
                 [:some-event-id1]
                 ;; will be triggered if
                 ;; enter
                 [{:keyCode 13}]
                 ;; or delete
                 [{:keyCode 46}]]
                ;; is pressed

                ;; Event & key combos 2
                [;; this event
                 [:some-event-id2]
                 ;; will be triggered if
                 ;; tab is pressed twice in a row
                 [{:keyCode 9} {:keyCode 9}]
                 ]]

   ;; takes a collection of key combos that, if pressed, will clear
   ;; the recorded keys
   :clear-keys
   ;; will clear the previously recorded keys if
   [;; escape
    [{:keyCode 27}]
    ;; or Ctrl+g
    [{:keyCode   71
      :ctrlKey true}]]
   ;; is pressed

   ;; takes a collection of keys that will always be recorded
   ;; (regardless if the user is typing in an input, select, or textarea)
   :always-listen-keys
   ;; will always record if
   [;; enter
    {:keyCode 13}]
   ;; is pressed

   ;; takes a collection of keys that will prevent the default browser
   ;; action when any of those keys are pressed
   ;; (note: this is only available to keydown)
   :prevent-default-keys
   ;; will prevent the browser default action if
   [;; Ctrl+g
    {:keyCode   71
      :ctrlKey true}]
    ;; is pressed
   }])
```


For `:event-keys`, `:clear-keys`, `:always-listen-keys`, and
`:prevent-default-keys`, the keys take the following shape:

```clojure
{:keyCode    <int>
 :altKey   <boolean>
 :ctrlKey  <boolean>
 :metaKey  <boolean>
 :shiftKey <boolean>
 }
```

For `:event-keys`, the *event* will be called with a few things *conj*ed
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


### `::rp/set-keypress-rules`

Listens to *keypress* events, otherwise it is the same as `::rp/set-keydown-rules` (except `:prevent-default-keys` is not supported).

### `::rp/set-keyup-rules`

Listens to *keyup* events, otherwise it is the same as `::rp/set-keydown-rules` (except `:prevent-default-keys` is not supported).

# Usage

Create a new re-frame application and add the `+re-pressed` option.

```
lein new re-frame foo +re-pressed
```

## Gotchas

- If you are seeing multiple instances of the same event being triggered, it is probably because you are dispatching `::rp/add-keyboard-event-listener` multiple times. This happens most often when using hot reload tools, such as figwheel, and you dispatch-sync the `::rp/add-keyboard-event-listener` event in a place that figwheel calls on reload ... instead of in a function that *only gets called once* when the application first starts.
- For keypress events, you only have access to things like letters and
  numbers. This is unlike keydown and keyup events, where you have
  access to more things like the Escape key.
- Using `:prevent-default-keys` only works with
  `::rp/set-keydown-rules`. This is because the default browser action will
  happen before keypress and keyup events happen.
- Certain browser default actions cannot be overwritten, like `ctrl+n`
  in chrome.
- Order matters, and the first matching key combination will consume the event.  So for example, if you want to listen for both forward arrow (`{:keyCode 37}`) and control + forward arrow (`{:keyCode 37 :ctrlKey true}`), then you must put the combination before the singleton.  Similarly, control-shift-arrow must come before control-arrow, and so on.

### Side note:

When using re-pressed, you will need to dispatch a `::rp/set-keydown-rules`,
`::rp/set-keypress-rules`, or `::rp/set-keyup-rules` event somewhere.
Personally, I like dispatching this in my routes file because I may
want to handle keyboard events differently on each page.

## Questions

If you have questions, I can usually be found hanging out in
the [clojurians](http://clojurians.net/) #reagent slack channel (my
handle is [@gadfly361](https://twitter.com/gadfly361)).

## License

Copyright © 2018 Matthew Jaoudi

Copyright © 2019 Arne Schlüter

Copyright © 2023 Boldizsar Racz

Distributed under the The MIT License (MIT).
