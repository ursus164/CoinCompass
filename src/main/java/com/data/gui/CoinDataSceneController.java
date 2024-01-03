package com.data.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CoinDataSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void getBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));
        root = loader.load();
        scene = new Scene(root);

        MainSceneController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }
}
