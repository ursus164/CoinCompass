package com.data.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSceneController {
    @FXML
    MenuItem menuLogOutButton;
    @FXML
    MenuItem menuCloseButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/CoinDataScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        CoinDataSceneController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close(ActionEvent event) {
        stage.close();
    }
}
