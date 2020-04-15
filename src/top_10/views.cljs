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
