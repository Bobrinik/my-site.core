(ns ^:figwheel-hooks my-site.core
  (:require
   [goog.dom :as gdom]
   [react :as react]
   [react-dom :as react-dom]
   [create-react-class :as create-react-class]
   [sablono.core :as sab :include-macros true]
   [om.core :as om :include-macros true]
   [ajax.core :as ajax]
   [markdown-to-hiccup.core :as m]
   [cljs.reader :as cljs]))

;; this is to support om with the latest version of React
(set! (.-createClass react) create-react-class)

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:projects []}))

(def base-url "https://raw.githubusercontent.com/Bobrinik/")
(def project-store (str base-url "bobrinik.github.io/master/projects.clj"))

(defn handle-project-urls [projects id urls]
  (if (not (empty? urls))
    (ajax/GET (first urls)
              {:handler 
               (fn [response]
                 (do
                   (om/update! projects [id] {:id id 
                                              :data (->>  response
                                                          (m/md->hiccup)
                                                          (m/component))})
                   (handle-project-urls projects (inc id) (drop 1 urls))))
               :error-handler (fn [response]
                                (do
                                  (println urls)
                                  (handle-project-urls projects id (drop 1 urls))))
               })))

(defn project-urls [url-handler]
  (ajax/GET project-store {:handler (fn [response] 
                                      (url-handler (map #(str base-url % "/master/PROJECT.md") 
                                                        (cljs/read-string response))))
                           :error-handler (fn [response] (println response))}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn update-attributes [cursor key options]
  (if (= (first cursor) key)
    (do 
      (om/update! (second cursor) options)
      (map #(update-attributes % key options) (drop 2 cursor)))
    (map #(update-attributes % key options) (drop 2 cursor))))

(defn render [cursor owner]
  (do
    (reify om/IRender
      (render [_]
        (let [hiccup cursor]
          (sab/html hiccup))))))

(defn project-item [cursor owner]
  (reify om/IRender
    (render [_]
      (sab/html
       [:div {:class "tile is-parent is-vertical"}
        [:article {:class "tile is-child box"}
         (do
           (doall (update-attributes  cursor :h2 {:class "title is-3"}))
           (doall (update-attributes  cursor :ul {:class "content"}))
           (om/build render @cursor))]]))))

(defn project-list [data owner]
  (reify om/IRender
    (render [_]
      (sab/html
       [:div    {:class "section tile is-ancestor"}
        [:div   {:class "container tile is-vertical is-8"}
         [:div  {:class "tile"}
          [:div {:class "tile is-parent is-vertical"}
           (om/build-all 
            project-item 
            (map #(:data %) (:projects data)) {:key :id})]]]]))))

(defn mount [el]
  (om/root
   project-list
   app-state
   {:target el}))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(project-urls
 #(handle-project-urls (:projects (om/root-cursor app-state)) 0 %))

(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
