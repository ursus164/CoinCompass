package com.data.gui;

import com.data.currency.CurrencyList;
import com.data.currency.CurrencySettings;
import com.data.market.MarketData;
import com.data.market.MarketDataCache;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainSceneController {
    @FXML
    public Label errorLabel;
    @FXML
    public TextField searchField, change_field1, price_field1, price_slot1,
            percent_slot1,change_field2, price_field2, price_slot2, percent_slot2,
            change_field3, price_field3, price_slot3, percent_slot3, change_field4,
            price_field4, price_slot4, percent_slot4;
    @FXML
    public Pane fav_slot1, fav_slot2, fav_slot3, fav_slot4;
    @FXML
    public Label label_slot1, label_slot2, label_slot3, label_slot4;
    @FXML
    public ImageView img_slot1, img_slot2, img_slot3, img_slot4,
            img_triangle1, img_triangle2, img_triangle3, img_triangle4;

    @FXML
    public ChoiceBox<Integer> chartChoice;
    @FXML
    ChoiceBox <String> currencyChoice;
    @FXML
    MenuItem menuLogOutButton, menuCloseButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final List<Integer> chartScale = List.of(1,3,5,7,14,21,30);

    public void logout(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/LoginScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        LoginSceneController loginSceneController = loader.getController();
        loginSceneController.setStage(stage);

        stage.setScene(scene);
        stage.show();

    }

    public void search(ActionEvent event) throws IOException {

        if (!searchField.getText().isEmpty()) {

            MarketData marketData = MarketDataCache.getMarketData(searchField.getText(),
                    currencyChoice.getValue(), true);

            marketData.setCurrency(currencyChoice.getValue());          // każdy obiekt ma przypisaną walutę parę w jakiej byłwyszukiwany

            if(marketData.getSymbol() != null){

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/CoinDataScene.fxml"));
                root = loader.load();
                scene = new Scene(root);

                CoinDataSceneController controller = loader.getController();

                controller.setStage(stage);
                controller.setSearchText(searchField.getText().toLowerCase());
                controller.setSelectedCurrency(currencyChoice.getValue());
                controller.setAutoRefresh(true);
                controller.setSelectedDays(chartChoice.getValue());

                controller.loadData();

                stage.setScene(scene);
                stage.show();

                HistoryManager.getInstance().addSearch(marketData);
            } else {
                searchField.setPromptText("No market found for: " + searchField.getText());
                searchField.setText("");
            }
        } else {
            searchField.setPromptText("Field is empty!");
        }
    }

    public void initialize() {

        CurrencyList list = new CurrencyList();
        List<String> currencies = list.getAllCurrencies();

        chartChoice.getItems().addAll(chartScale);
        chartChoice.setValue(1);

        currencyChoice.getItems().addAll(currencies);
        currencyChoice.setValue(CurrencySettings.getInstance().getSelectedCurrency());
        currencyChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                CurrencySettings.getInstance().setSelectedCurrency(newVal);
            }
        });
        updateHistoryDisplay();
    }

    public void updateHistoryDisplay() {
        List<MarketData> history = HistoryManager.getInstance().getHistory();
        if(history.size() > 0) {
            price_field1.setVisible(true);
            change_field1.setVisible(true);

            updateSlot(history.get(0),label_slot1,price_slot1,img_slot1,percent_slot1,img_triangle1);
        }
        if (history.size() > 1) {
            price_field2.setVisible(true);
            change_field2.setVisible(true);

            updateSlot(history.get(1),label_slot2,price_slot2,img_slot2,percent_slot2,img_triangle2);
        }
        if (history.size() > 2) {
            price_field3.setVisible(true);
            change_field3.setVisible(true);

            updateSlot(history.get(2),label_slot3,price_slot3,img_slot3,percent_slot3,img_triangle3);
        }
        if (history.size() > 3) {
            price_field4.setVisible(true);
            change_field4.setVisible(true);

            updateSlot(history.get(3),label_slot4,price_slot4,img_slot4,percent_slot4,img_triangle4);
        }
    }
    private void updateSlot(MarketData data, Label symbol, TextField price,ImageView icon,TextField percent,ImageView triangle) {

        Image image = new Image(data.getIconUrl());

        String trianglePath = null;

        if(Double.toString(data.getPrice_change_percentage_24h()).contains("-")) {
            trianglePath = getClass().getResource("/com.data.gui/images/red_triangle.png").toExternalForm();
        } else {
            trianglePath = getClass().getResource("/com.data.gui/images/green_triangle.png").toExternalForm();
        }

        Image triangleIcon = new Image(trianglePath);

        String formattedPrice = String.format("%.3f",data.getCurrent_price());
        String formattedPercent = String.format("%.3f",data.getPrice_change_percentage_24h());

        String cryptoChange = formattedPercent + "%";
        String finalPrice = formattedPrice + " " + data.getCurrency().toUpperCase();

        symbol.setText(data.getSymbol().toUpperCase());
        price.setText(finalPrice);
        percent.setText(cryptoChange);
        icon.setImage(image);
        triangle.setImage(triangleIcon);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close(ActionEvent event) {
        stage.close();
    }
}
