package com.data.gui;

import com.data.api.CurrencyList;
import com.data.market.MarketData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

            MarketData marketData = new MarketData(searchField.getText(),currencyChoice.getValue(),RefreshCheckBox);
            System.out.println(currencyChoice.getValue());
            if(marketData.getSymbol() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/CoinDataScene.fxml"));
                root = loader.load();
                scene = new Scene(root);

                CoinDataSceneController controller = loader.getController();
                controller.setStage(stage);
                controller.setSearchText(searchField.getText().toLowerCase());

                if(currencyChoice.getValue().equals("vs_currency")) {
                    controller.setSelectedCurrency("usd");          // default, only for view
                } else {
                    controller.setSelectedCurrency(currencyChoice.getValue());

                }
                controller.setAutoRefresh(RefreshCheckBox);
                controller.loadData();

                stage.setScene(scene);
                stage.show();
            } else {
                searchField.setPromptText("No market found for: " + searchField.getText());
                searchField.setText("");
            }
        } else {
            searchField.setPromptText("Field is empty!");
        }
    }

    public void initialize() {
        currencyChoice.setValue("vs_currency");
        CurrencyList list = new CurrencyList();
        List<String> currencies = list.getAllCurrencies();
        currencyChoice.getItems().addAll(currencies);

        updateHistoryDisplay();
    }

    public void updateHistoryDisplay() {
        List<MarketData> history = HistoryManager.getInstance().getHistory();
        if(history.size() > 0) {
            updateSlot(history.get(0),label_slot1,price_slot1,img_slot1,percent_slot1,img_triangle1);
        }
        if (history.size() > 1) {
            updateSlot(history.get(1),label_slot2,price_slot2,img_slot2,percent_slot2,img_triangle2);
        }
        if (history.size() > 2) {
            updateSlot(history.get(2),label_slot3,price_slot3,img_slot3,percent_slot3,img_triangle3);
        }
        if (history.size() > 3) {
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

        String formattedPrice = String.format("%.3f",data.getPrice_change_24h());
        String formattedPercent = String.format("%.3f",data.getPrice_change_percentage_24h());

        String cryptoChange = formattedPercent + "%";
        String finalPrice = formattedPrice + " " + currencyChoice.getValue().toUpperCase();



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
