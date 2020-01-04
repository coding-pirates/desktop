package de.upb.codingpirates.battleships.desktop.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientType {

    @FXML
    RadioButton rb_spectator;
    @FXML
    RadioButton rb_player;
    @FXML
    Label lb_choice;
    @FXML
    Button closeButton;

    public void display() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);

        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.showAndWait();
    }

    /**@FXML
    public void player(){
        lb_choice.setText("Player is chosen");}**/

    /**@FXML
    public void spectator(){
        lb_choice.setText("Spectator is chosen");
    }**/


    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
