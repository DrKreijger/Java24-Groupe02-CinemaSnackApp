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
    private ObservableList<Article> articlesOb = FXCollections.observableArrayList();
    private Article selectedArticle;


    private final List<Article> articles = new ArrayList<>();


    public SnacksViewController() {
        articles.add(new Article("Chips", 2.50));
        articles.add(new Article("Chocolate Bar", 1.75));
        articles.add(new Article("Soda", 1.00));
    }

    @FXML
    protected void onBagButtonClick() {
        snacksText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize() {
        // Populate the ListView with articles
        articleListView.getItems().addAll(articles);
        articleListView.refresh();
        // Set a custom cell factory for the ListView if needed
        // articleListView.setCellFactory(...);
    }
}