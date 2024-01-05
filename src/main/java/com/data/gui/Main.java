package com.data.gui;

import com.data.market.MarketDataPoint;
import com.data.market.MarketDataSeries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {

        PrintWriter writer = new PrintWriter("logs/app.log");
        writer.print("");
        writer.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/LoginScene.fxml"));
            Parent root = loader.load();

            String css = getClass().getResource("/com.data.gui/style/style.css").toExternalForm();
            String iconPath = getClass().getResource("/com.data.gui/images/bitcoin.png").toExternalForm();

            Scene introScene = new Scene(root);

            LoginSceneController controller = loader.getController();
            controller.setStage(stage);

            Image appIcon = new Image(iconPath);
            introScene.getStylesheets().add(css);

            stage.getIcons().add(appIcon);
            stage.setTitle("CoinCompass v0.0");

            stage.setScene(introScene);
            stage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
