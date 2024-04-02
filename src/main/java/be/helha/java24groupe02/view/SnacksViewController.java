package be.helha.java24groupe02.view;

import be.helha.java24groupe02.model.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class SnacksViewController {
    @FXML
    private Label snacksText;

    @FXML
    private ListView<Article> articleListView;
    private ListView<Article> articleBagListView;

    private final ObservableList<Article> articlesOb;

    public SnacksViewController() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Chips", 2.50));
        articles.add(new Article("Chocolate Bar", 1.75));
        articles.add(new Article("Soda", 1.00));


        articlesOb = FXCollections.observableArrayList(articles);
    }

    @FXML
    protected void onBagButtonClick() {
        Article selectedArticle = articleListView.getSelectionModel().getSelectedItem();

        if (selectedArticle != null) {

            articleBagListView.getItems().add(selectedArticle);
        } else {
            snacksText.setText("Veuillez sélectionner un article.");
        }
    }

    @FXML
    public void initialize() {

        articleListView.setItems(articlesOb);

    }
}