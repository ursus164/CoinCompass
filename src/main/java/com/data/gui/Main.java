package com.data.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/LoginScene.fxml"));
            Parent root = loader.load();
            // klasa controller

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
