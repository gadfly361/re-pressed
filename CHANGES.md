# Changes

## 0.3.2 (2022-05-08)

- add optional `:clear-on-success-event-match` to clear tracked keys if there is a successful match.

## 0.3.1 (2019-09-11)

- bump dependency versions [#8](https://github.com/gadfly361/re-pressed/pull/8)

## 0.3.0 (2019-01-09)

**BREAKING CHANGE** :
- `:which` has been replaced by `:keyCode`

Note: your existing apps will need to replace `:which` with `:keyCode` because in [#7] the dependecy on jQuery was removed and replace with google closure.  This was necessary because jQuery normalizes key identificaiton around `which` and google closure normalizes around `:keyCode`.

Re-pressed no longer relies on jQuery!

## 0.2.2 (2018-06-18)

Bug Fixes

- [#5 need to clear the previously tracked keys whenever the rules change](https://github.com/gadfly361/re-pressed/issues/5)

## 0.2.1 (2018-05-28)

Bug Fixes

- [#4, Key sequences are incorrectly compared to most recent keys in reverse order](https://github.com/gadfly361/re-pressed/issues/4)

## 0.2.0 (2018-04-17)

**Additions**

- add `:always-listen-keys`, which takes a vector of keys. By default,
  all keys are not recorded when a user is typing inside of an input,
  select, or textarea. However, this is a way to effectively whitelist
  a set of keys and to always listen to them. Imagine a user is typing
  in an input and hits the Enter key, this is a way to still dispatch
  an event off of that.

**Breaking changes**

Rename

- `::rp/set-keydown-event`, renamed to `::rp/set-keydown-rules`
- `::rp/set-keypress-event`, renamed to `::rp/set-keypress-rules`
- `::rp/set-keyup-event`, renamed to `::rp/set-keyup-rules`

Update inputs

- `:prevent-default-keys` now takes a vector of keys (instead of a
  vector of key combo vectors)


## 0.1.0-alpha (2018-04-15)

- initial release
