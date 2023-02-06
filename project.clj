(defproject blaclojack "0.1.0-SNAPSHOT"
  :description "A simple blackjack game"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [mount "0.1.17"]]
  :main ^:skip-aot blaclojack.core
  :target-path "target/%s"
  :profiles {:uberjar       {:aot      :all
                             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev           [:project/dev :profiles/dev]
             :test          [:project/test :profiles/test]
             :profiles/dev  {}
             :profiles/test {}
             :project/dev   {:source-paths   ["dev/src" "spec"]
                             :resource-paths ["dev/resources"]
                             :dependencies   [[org.clojure/tools.namespace "1.3.0"]
                                              [orchestra "2021.01.01-1"]]}
             :project/test  {:source-paths ["spec"]}
             :repl          {:repl-options {:init-ns user}}})
