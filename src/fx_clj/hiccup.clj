(ns fx-clj.hiccup
  (:require
    [fx-clj.core.pset :as pset]
    [fx-clj.impl.elements :refer [element-factories]]
    [fx-clj.core.convert :refer [convert-arg]]
    [camel-snake-kebab.core :as csk]))

(def ^{:doc "Regular expression that parses a CSS-style id and class from an element name.
             From hiccup.compiler: https://github.com/weavejester/hiccup/blob/master/src/hiccup/compiler.clj
             EPL license."
       :private true}
  re-tag #"([^\s\.#]+)(?:#([^\s\.#]+))?(?:\.([^\s#]+))?")

(def ^:private get-elem-factory
  (memoize
    (fn [elem-name]
      (get @element-factories (csk/->CamelCase elem-name)))))

(defn hiccup-construct [elem-kw]
  (assert (keyword? elem-kw) (str elem-kw " must be a keyword when using hiccup style construction"))
  (let [[_ elem-name id classes] (re-matches re-tag (name elem-kw))
        class-list (when classes (into [] (.split classes "\\.")))
        node ((get-elem-factory elem-name))]
    (pset/set-id+classes node id class-list)
    node))

(defn compile-fx* [[elem-kw & args]]
  (pset/pset!** (fx-clj.hiccup/hiccup-construct elem-kw) args))

(defmacro compile-fx [form]
  `(fx-clj.core.run/run<!! (fx-clj.hiccup/compile-fx* ~form)))

(defmethod convert-arg [Object clojure.lang.PersistentVector] [_ v opts]
  (compile-fx v))
