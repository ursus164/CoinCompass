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
    public TextField searchField;
    @FXML
    public Pane fav_slot1;
    @FXML
    public Pane fav_slot2;
    @FXML
    public Pane fav_slot3;
    @FXML
    public Pane fav_slot4;
    @FXML
    public TextField percent_slot4;
    @FXML
    public TextField price_slot4;
    @FXML
    public Label label_slot4;
    @FXML
    public ImageView img_slot4;
    @FXML
    public TextField percent_slot3;
    @FXML
    public TextField price_slot3;
    @FXML
    public Label label_slot3;
    @FXML
    public ImageView img_slot3;
    @FXML
    public TextField percent_slot2;
    @FXML
    public TextField price_slot2;
    @FXML
    public Label label_slot2;
    @FXML
    public ImageView img_slot2;
    @FXML
    public TextField percent_slot1;
    @FXML
    public TextField price_slot1;
    @FXML
    public Label label_slot1;
    @FXML
    public ImageView img_slot1;
    @FXML
    public ImageView img_triangle4;
    @FXML
    public ImageView img_triangle3;
    @FXML
    public ImageView img_triangle2;
    @FXML
    public ImageView img_triangle1;
    @FXML
    public TextField price_field1;
    @FXML
    public TextField change_field1;
    @FXML
    public TextField price_field2;
    @FXML
    public TextField change_field2;
    @FXML
    public TextField price_field3;
    @FXML
    public TextField change_field3;
    @FXML
    public TextField price_field4;
    @FXML
    public TextField change_field4;
    @FXML
    MenuItem menuLogOutButton;
    @FXML
    MenuItem menuCloseButton;
    @FXML
    ChoiceBox <String> currencyChoice;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Boolean RefreshCheckBox = true;

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

            MarketData marketData = MarketDataCache.getMarketData(searchField.getText(),currencyChoice.getValue(),RefreshCheckBox);
            marketData.setCurrency(currencyChoice.getValue());

            if(marketData.getSymbol() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/CoinDataScene.fxml"));
                root = loader.load();
                scene = new Scene(root);

                CoinDataSceneController controller = loader.getController();
                controller.setStage(stage);
                controller.setSearchText(searchField.getText().toLowerCase());
                controller.setSelectedCurrency(currencyChoice.getValue());
                controller.setAutoRefresh(RefreshCheckBox);
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
