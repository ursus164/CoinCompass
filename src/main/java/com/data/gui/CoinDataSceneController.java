package com.data.gui;

import com.data.market.MarketData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CoinDataSceneController {
    @FXML
    public TextField crypto_id;
    @FXML
    public TextField crypto_price;
    @FXML
    public TextField crypto_change;
    @FXML
    public ImageView CryptoSymbol;
    @FXML
    public TextField atl_value;
    @FXML
    public TextField ath_value;
    @FXML
    public TextField market_cap_value;
    @FXML
    public TextField market_cap_rank_value;
    @FXML
    public TextField high_24h_value;
    @FXML
    public TextField low_24h_value;
    @FXML
    public ImageView triangle_symbol;
    @FXML
    public Button addFavourites;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String selectedCurrency;
    private String searchText;
    private Boolean autoRefresh;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadData() {

        MarketData marketData = new MarketData(searchText,selectedCurrency,autoRefresh);
        String formattedPrice = String.format("%.3f",marketData.getPrice_change_24h());
        String formattedPercent = String.format("%.3f",marketData.getPrice_change_percentage_24h());

        String trianglePath = null;

        if(formattedPercent.contains("-")) {
            trianglePath = getClass().getResource("/com.data.gui/images/red_triangle.png").toExternalForm();
        } else {
            trianglePath = getClass().getResource("/com.data.gui/images/green_triangle.png").toExternalForm();
        }

        Image triangleIcon = new Image(trianglePath);
        triangle_symbol.setImage(triangleIcon);

        String cryptoChange = formattedPrice + " (" + formattedPercent + "%)";
        String price = marketData.getCurrent_price() + " " + selectedCurrency;
        String cryptoID = "Kurs " + marketData.getName() + " (" + marketData.getSymbol().toUpperCase() + ")";
        String formattedMarket = String.format("%.3f",marketData.getMarket_cap());
        Image cryptoImage = new Image(marketData.getIconUrl());


        CryptoSymbol.setImage(cryptoImage);
        crypto_id.setText(cryptoID);
        crypto_price.setText(price.toUpperCase());
        crypto_change.setText(cryptoChange);
        ath_value.setText(Double.toString(marketData.getAth()));
        atl_value.setText(Double.toString(marketData.getAtl()));
        market_cap_value.setText(formattedMarket);
        market_cap_rank_value.setText(Integer.toString(marketData.getMarket_cap_rank()));
        high_24h_value.setText(Double.toString(marketData.getHigh_24h()));
        low_24h_value.setText(Double.toString(marketData.getLow_24h()));

        HistoryManager.getInstance().addSearch(marketData);
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public void getBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        MainSceneController controller = loader.getController();
        controller.updateHistoryDisplay();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }
}