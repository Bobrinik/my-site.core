(defproject my-site.core "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [cljsjs/react "16.4.1-0"]
                 [cljsjs/react-dom "16.4.1-0"]
                 [cljsjs/create-react-class "15.6.3-1"]
                 [sablono "0.8.4"]
                 [org.omcljs/om "1.0.0-beta4"]
                 [cljs-ajax "0.8.0"]
                 [markdown-to-hiccup "0.6.2"]]

  :source-paths ["src"]

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:prod"  ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "cruel"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" my-site.test-runner]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.1.9"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]]
                   }})

