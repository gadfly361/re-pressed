# Changes

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
