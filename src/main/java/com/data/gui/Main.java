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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Main class for the JavaFX application.
 * This class is responsible for initializing and starting the primary stage of the application.
 */
public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Main method to launch the application.
     *
     * @param args Arguments passed from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the primary stage of the application.
     * Initializes the application's GUI by loading the LoginScene FXML file, setting the application icon, and displaying the initial scene.
     *
     * @param stage The primary stage for this application.
     * @throws FileNotFoundException if the log file cannot be accessed.
     */
    @Override
    public void start(Stage stage) throws FileNotFoundException {

        PrintWriter writer = new PrintWriter("logs/app.log");
        writer.print("");
        writer.close();
        logger.info("Log file data has been cleared");

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
