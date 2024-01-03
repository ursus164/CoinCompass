package com.data.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginSceneController {

    @FXML
    TextField UsernameField;
    @FXML
    PasswordField PasswordField;
    @FXML
    Label InvalidLabel;
    @FXML
    Label AccountExists;
    @FXML
    Label SuccesfullRegister;
    @FXML
    Label PasswordsNotMatch;
    @FXML
    TextField UsernameRegisterField;
    @FXML
    TextField PasswordRegisterField;
    @FXML
    TextField PasswordRepeatField;


    private Stage stage;
    private Scene scene;
    private Parent root;
    private static final String userList_cache = "src/cache/user_data.json";

    public void login(ActionEvent event) throws IOException {
        String username = UsernameField.getText();
        String password = PasswordField.getText();

        if (username.equals("student") && (password.equals("student"))) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));

            root = loader.load();
            InvalidLabel.setVisible(false);

            MainSceneController mainSceneController = loader.getController();
            mainSceneController.setStage(stage);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            InvalidLabel.setVisible(true);
            UsernameField.setText("");
            PasswordField.setText("");
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
