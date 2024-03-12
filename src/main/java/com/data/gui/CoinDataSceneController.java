package com.data.gui;

import com.data.currency.CurrencySettings;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller class for the Coin Data Scene in a JavaFX application.
 * This class manages the user interface for displaying cryptocurrency market data.
 */
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
    private MarketData marketData;

    private static final Logger logger = LogManager.getLogger(CoinDataSceneController.class);

    /**
     * Loads and displays market data into the scene based on user-defined parameters.
     * It fetches data using MarketDataCache and updates UI components accordingly.
     */
    public void loadData() {
        logger.info("Selected currency before loading: " + selectedCurrency);
        marketData = MarketDataCache.getMarketData(searchText, selectedCurrency, autoRefresh);

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

    /**
     * Handles the action to return to the previous scene. It clears cache for previously searched market.
     *
     * @param event The event triggered by the user action.
     * @throws IOException if loading the FXML resource fails.
     */
    public void getBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        MainSceneController controller = loader.getController();
        controller.setSelectedCurrency(selectedCurrency);



//        controller.updateHistoryDisplay();
        controller.setStage(stage);

        MarketDataCache.clearCacheFor(searchText, selectedCurrency, autoRefresh);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Converts a UNIX timestamp to a formatted date string.
     *
     * @param timestamp The UNIX timestamp to convert.
     * @return A formatted date string.
     */
    public static String convertTimestampToDate(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        return dateTime.format(formatter);
    }


    /**
     * Creates a chart for displaying market data over a specified number of days.
     *
     * @param days The number of days to include in the chart.
     */
    public void chartCreate(int days) {
        MarketDataPoint chart = new MarketDataPoint(crypto, selectedCurrency, days);        // only to fetch data

        List<MarketDataPoint> points = MarketDataSeries.convert();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        coinChart.getData().clear();        // erase data before adding new

        if (points != null) {
            for (MarketDataPoint point : points) {
                String date = convertTimestampToDate(point.getTimestamp());
                series.getData().add(new XYChart.Data<>(date, point.getValue()));
            }
        }
        coinChart.getYAxis().setAutoRanging(true);
        coinChart.getData().add(series);

        logger.debug("Initializing chart for market: " + crypto);
    }

    /**
     * Handles the action to log out of the application.
     *
     * @param event The event triggered by the user action.
     * @throws IOException if loading the FXML resource fails.
     */
    public void logout(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/LoginScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        LoginSceneController loginSceneController = loader.getController();
        loginSceneController.setStage(stage);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public void close(ActionEvent event) {
        stage.close();
    }

    /**
     * Refreshes the displayed market data. Reloads all data for e.g. prices, charts etc...
     *
     * @param event The event triggered by the user action.
     */
    public void refreshData(ActionEvent event) {
        System.out.println("Metoda refreshData: " + CurrencySettings.getInstance().getSelectedCurrency());
//        MarketDataCache.clearCacheFor(searchText, selectedCurrency, autoRefresh);
        MarketData data = new MarketData(searchText, selectedCurrency, autoRefresh);
        data.setCurrency(selectedCurrency);
        crypto = marketData.getId();
        chartCreate(selectedDays);

        String formattedPrice = String.format("%.6f", data.getPrice_change_24h());
        String formattedPercent = String.format("%.6f", data.getPrice_change_percentage_24h());
        String currentPrice = String.format("%6f",data.getCurrent_price());

        String trianglePath = null;

        if (formattedPercent.contains("-")) {
            trianglePath = getClass().getResource("/com.data.gui/images/red_triangle.png").toExternalForm();
        } else {
            trianglePath = getClass().getResource("/com.data.gui/images/green_triangle.png").toExternalForm();
        }

        Image triangleIcon = new Image(trianglePath);
        Image cryptoImage = new Image(data.getIconUrl());

        String cryptoChange = formattedPrice + " (" + formattedPercent + "%)";
        String price = currentPrice + " " + selectedCurrency;
        String cryptoID = "Kurs " + data.getName() + " (" + data.getSymbol().toUpperCase() + ")";
        String formattedMarket = String.format("%.1f", data.getMarket_cap());

        CryptoSymbol.setImage(cryptoImage);
        crypto_id.setText(cryptoID);
        crypto_price.setText(price.toUpperCase());
        crypto_change.setText(cryptoChange);
        ath_value.setText(Double.toString(data.getAth()));
        atl_value.setText(Double.toString(data.getAtl()));
        market_cap_value.setText(formattedMarket);
        market_cap_rank_value.setText(Integer.toString(data.getMarket_cap_rank()));
        high_24h_value.setText(Double.toString(data.getHigh_24h()));
        low_24h_value.setText(Double.toString(data.getLow_24h()));
        triangle_symbol.setImage(triangleIcon);

        HistoryManager.getInstance().addSearch(data);
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
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}


