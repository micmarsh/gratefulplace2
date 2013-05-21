(ns gratefulplace.db.manage
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as q])
  (:use environ.core)
  (:import java.io.File))

(defn recreate
  []
  (d/delete-database q/db-uri)
  (d/create-database q/db-uri))

(defn load-schema
  []
  (map #(q/t (read-string (slurp %)))
       (.listFiles (File. "resources/migrations"))))
