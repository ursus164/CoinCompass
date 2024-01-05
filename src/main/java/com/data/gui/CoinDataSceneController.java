package com.data.gui;

import com.data.market.MarketData;
import com.data.market.MarketDataCache;
import com.data.market.MarketDataPoint;
import com.data.market.MarketDataSeries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CoinDataSceneController {
    @FXML
    public TextField crypto_id, crypto_price, crypto_change, atl_value, ath_value,
            market_cap_value, market_cap_rank_value, high_24h_value, low_24h_value;
    @FXML
    public ImageView CryptoSymbol, triangle_symbol;
    @FXML
    public Button refreshButton;
    @FXML
    public LineChart<String, Double> coinChart;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private int selectedDays;
    private String selectedCurrency;
    private String searchText;
    private String crypto;
    private Boolean autoRefresh;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadData() {
        MarketData marketData = MarketDataCache.getMarketData(searchText, selectedCurrency, autoRefresh);


        crypto = marketData.getId();
        chartCreate(selectedDays);

        String formattedPrice = String.format("%.6f", marketData.getPrice_change_24h());
        String formattedPercent = String.format("%.6f", marketData.getPrice_change_percentage_24h());
        String currentPrice = String.format("%6f",marketData.getCurrent_price());

        String trianglePath = null;

        if (formattedPercent.contains("-")) {
            trianglePath = getClass().getResource("/com.data.gui/images/red_triangle.png").toExternalForm();
        } else {
            trianglePath = getClass().getResource("/com.data.gui/images/green_triangle.png").toExternalForm();
        }

        Image triangleIcon = new Image(trianglePath);
        Image cryptoImage = new Image(marketData.getIconUrl());

        String cryptoChange = formattedPrice + " (" + formattedPercent + "%)";
        String price = currentPrice + " " + selectedCurrency;
        String cryptoID = "Kurs " + marketData.getName() + " (" + marketData.getSymbol().toUpperCase() + ")";
        String formattedMarket = String.format("%.1f", marketData.getMarket_cap());

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
        triangle_symbol.setImage(triangleIcon);

        HistoryManager.getInstance().addSearch(marketData);
    }


    public void getBack(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        MainSceneController controller = loader.getController();

        controller.updateHistoryDisplay();
        controller.setStage(stage);

        MarketDataCache.clearCacheFor(searchText, selectedCurrency, autoRefresh);

        stage.setScene(scene);
        stage.show();
    }

    public static String convertTimestampToDate(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        return dateTime.format(formatter);
    }

    public void chartCreate(int days) {
        MarketDataPoint chart = new MarketDataPoint(crypto, selectedCurrency, days);

        List<MarketDataPoint> points = MarketDataSeries.convert();
        XYChart.Series<String, Double> series = new XYChart.Series<>();


        if (points != null) {
            for (MarketDataPoint point : points) {
                String date = convertTimestampToDate(point.getTimestamp());
                series.getData().add(new XYChart.Data<>(date, point.getValue()));
            }
        }
        coinChart.getYAxis().setAutoRanging(true);
        coinChart.getData().add(series);
    }
    public void refreshData(ActionEvent event) {
        loadData();
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
    public void setSelectedDays(int selectedDays) {
        this.selectedDays = selectedDays;
    }
}


