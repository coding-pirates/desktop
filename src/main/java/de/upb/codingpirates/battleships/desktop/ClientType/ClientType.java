package de.upb.codingpirates.battleships.desktop.ClientType;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientType {

    private Stage window;

    public void display(Stage window) throws IOException {
        this.window=window;

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
