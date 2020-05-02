(ns top-10.views
  (:require [re-frame.core :as rf]))

(defn add-item-section []
  [:div.add-item-section
   [:input {:type      "text"
            :on-change #(rf/dispatch [:change-current-item
                                      (-> % .-target .-value)])
            :value     @(rf/subscribe [:current-item])}]
   [:button {:on-click #(rf/dispatch [:add-item])} "Adicionar"]])

(defn list-items-section []
  [:div
   (let [items @(rf/subscribe [:items])]
     (for [[item-name _] items]
       [:div
        {:key item-name}
        item-name
        [:button
         {:on-click #(rf/dispatch [:remove-item item-name])}
         "Remover"]]))])

(defn choose-between-two-section []
  [:div
   [:button
    {:on-click #(rf/dispatch [:generate-choices])}
    "Sortear"]
   (for [item-name @(rf/subscribe [:choices])]
     [:div
      {:key item-name}
      item-name
      [:button
       {:on-click #(rf/dispatch [:choose-item item-name])}
       "Escolher"]])])
   
(defn current-top-10 []
  [:div
   (if @(rf/subscribe [:show-top-10])
     (for [item-name @(rf/subscribe [:top-10])]
       [:div
        {:key item-name}
        item-name])
     [:button
      {:on-click #(rf/dispatch [:present-top-10])}
      "Mostrar top 10"])])
   
