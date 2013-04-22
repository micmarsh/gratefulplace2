(ns gratefulplace.db
  (:require [datomic.api :as d])
  (:use environ.core))

(def db-uri (:db-uri (env :datomic)))
(def conn (d/connect db-uri))
(defmacro db
  []
  '(d/db conn))

(def collections
  {:topics
   {:all :topic/title
    :attributes [:topic/title]}
   :posts
   {:all :post/topic
    :attributes [:post/content]}})

;; '[:find ?c :where [?c :topic/title]]
(def q
  #(d/q % (db)))

(def ent
  #(d/entity (db) (first %)))

(defn ent->map
  [collection entity]
  (reduce (fn [acc attr] (conj acc [attr (attr entity)]))
          {}
          (:attributes (get collections collection))))

(defn entseq->maps
  [collection seq]
  (map (partial ent->map collection) seq))

(defn all
  [collection]
  (let [coldef (get collections collection)]
    (map ent (q [:find '?c :where ['?c (:all coldef)]]))))