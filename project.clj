(defproject org.clojars.betontalpfa/re-pressed "0.4.0"
  :description "A keyboard events library for re-frame"
  :url "https://github.com/gadfly361/re-pressed"
  :license {:name "MIT"}
  :scm {:name "git"
        :url  "https://github.com/M-sleeper/re-pressed"}

  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [reagent "0.8.1" :scope "provided"]
                 [re-frame "0.10.9" :scope "provided"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/main"]

  :clean-targets ^{:protect false} ["dev-resources/public/js/compiled" "target"]

  :profiles
  {:dev
   {:dependencies [[secretary "1.2.3"]
                   [garden "1.3.9"]
                   [binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.4.3"]
                   [re-frisk "0.5.4.1"]
                   [figwheel-sidecar "0.5.19"]]
    :plugins      [[lein-figwheel "0.5.19"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/demo" "src/main"]
     :figwheel     {:on-jsload "re-pressed.core-demo/mount-root"}
     :compiler     {:main                 re-pressed.core-demo
                    :output-to            "dev-resources/public/js/compiled/app.js"
                    :output-dir           "dev-resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload
                                           re-frisk.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}}}

     {:id           "min"
      :source-paths ["src/demo" "src/main"]
      :compiler     {:main            re-pressed.core-demo
                     :output-to       "dev-resources/public/js/compiled/app.js"
                     :optimizations   :advanced
                     :closure-defines {goog.DEBUG false}
                     :pretty-print    false}}
     ]}
   )
