package com.data.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller class for the login scene in a JavaFX application.
 * This class manages the user interface for user login functionality.
 */
public class LoginSceneController {

    @FXML
    public ImageView appLogo;
    @FXML
    TextField UsernameField, UsernameRegisterField, PasswordRegisterField, PasswordRepeatField;
    @FXML
    PasswordField PasswordField;
    @FXML
    Label InvalidLabel, AccountExists, SuccesfullRegister, PasswordsNotMatch;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Handles the login action when the user attempts to log in.
     * The method validates the user's credentials and directs them to the main scene of the application.
     *
     * @param event The event triggered by the login action.
     * @throws IOException if loading the FXML resource fails.
     */
    public void login(ActionEvent event) throws IOException {
        String username = UsernameField.getText();
        String password = PasswordField.getText();

//        if (username.equals("student") && (password.equals("student"))) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.data.gui/fxml/MainScene.fxml"));

            root = loader.load();
            InvalidLabel.setVisible(false);

            MainSceneController mainSceneController = loader.getController();
            mainSceneController.setStage(stage);

            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

//        } else {
//            InvalidLabel.setVisible(true);
//            UsernameField.setText("");
//            PasswordField.setText("");
        }
//    }
    public void initialize() {
        String iconPath = getClass().getResource("/com.data.gui/images/bitcoin.png").toExternalForm();
        Image appIcon = new Image(iconPath);

        appLogo.setImage(appIcon);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
