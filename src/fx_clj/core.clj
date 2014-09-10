(ns fx-clj.core
  (:require
    [potemkin :refer [import-vars]]
    [fx-clj.core.run]
    [fx-clj.core.pset]
    [fx-clj.hiccup]
    [fx-clj.enlive]
    [fx-clj.elements]
    [fx-clj.css]
    [fx-clj.sandbox]))

(import-vars
  [fx-clj.core.run run! run<! run<!!]
  [fx-clj.core.pset pset! pset<! pset<!!]
  [fx-clj.hiccup compile-fx]
  [fx-clj.enlive at]
  [fx-clj.sandbox sandbox]
  [fx-clj.css set-global-css!])

(eval
  `(potemkin/import-vars [fx-clj.elements
      ~@(keys (ns-publics (find-ns 'fx-clj.elements)))]))

