(ns io.pivotal.pcf.rabbitmq.test-helpers
  (:refer-clojure :exclude [get])
  (:require [clojure.java.io :as io]
            [clj-yaml.core :as yaml]
            [clj-http.client :as httpc]
            [cheshire.core :as json]))

(def ^:dynamic *throw-exceptions* false)
(def username "p1-rabbit")
(def password "p1-rabbit-testpwd")

(defn load-config!
  [^String relative-resource-path]
  (yaml/parse-string (slurp (io/resource relative-resource-path))))

(def load-config (memoize load-config!))

(defn get
  ([^String path]
     (get path "http"))
  ([^String path ^String scheme]
     (json/decode (:body (httpc/get (format "%s://127.0.0.1:4567/%s" scheme path)
                                    {:basic-auth [username password]}))
                  true)))

(defn put
  ([^String path]
     (put path "http" {}))
  ([^String path ^String scheme]
     (put path scheme {}))
  ([^String path ^String scheme {:keys [body] :as options}]
     (json/decode (:body (httpc/put (format "%s://127.0.0.1:4567/%s" scheme path)
                                    (merge options {:basic-auth [username password]
                                                    :accept :json
                                                    :body (json/encode body)
                                                    :throw-exceptions *throw-exceptions*}))) true)))

(defn raw-put
  ([^String path]
     (raw-put path "http" {}))
  ([^String path ^String scheme]
     (raw-put path scheme {}))
  ([^String path ^String scheme {:keys [body] :as options}]
     (httpc/put (format "%s://127.0.0.1:4567/%s" scheme path)
                (merge options {:basic-auth [username password]
                                :accept :json
                                :body (json/encode body)
                                :throw-exceptions *throw-exceptions*}))))

(defn delete
  ([^String path]
     (delete path "http" {}))
  ([^String path ^String scheme]
     (delete path scheme {}))
  ([^String path ^String scheme {:keys [body] :as options}]
     (json/decode (:body (httpc/delete (format "%s://127.0.0.1:4567/%s" scheme path)
                                       (merge options {:basic-auth [username password]
                                                       :accept :json
                                                       :body (json/encode body)
                                                       :throw-exceptions *throw-exceptions*}))) true)))

(defn raw-delete
  ([^String path]
     (raw-delete path "http" {}))
  ([^String path ^String scheme]
     (raw-delete path scheme {}))
  ([^String path ^String scheme {:keys [body] :as options}]
     (httpc/delete (format "%s://127.0.0.1:4567/%s" scheme path)
                   (merge options {:basic-auth [username password]
                                   :accept :json
                                   :body (json/encode body)
                                   :throw-exceptions *throw-exceptions*}))))
