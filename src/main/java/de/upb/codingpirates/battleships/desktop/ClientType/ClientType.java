package de.upb.codingpirates.battleships.desktop.ClientType;

import de.upb.codingpirates.battleships.desktop.lobby.Lobby;
import de.upb.codingpirates.battleships.desktop.placeship.Placeships;
import de.upb.codingpirates.battleships.desktop.waiting.Waiting;
import javafx.application.Platform;
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

    public void display() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        AnchorPane pane = loader.load();

        ClientTypeController clientTypeController = loader.getController();

        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);

        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.showAndWait();
    }


}
