package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientType {
    private ClientTypeController clientTypeController;
    private Stage window;
    private Stage LobbyStage;

    public void display(Stage window, Game selectedGame, int clientID,Stage Lobbystage) throws IOException {
        this.window=window;
        this.LobbyStage=Lobbystage;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        AnchorPane pane = loader.load();
        clientTypeController = loader.getController();
        clientTypeController.setSelectedGame(selectedGame);
        clientTypeController.setClientID(clientID);
        clientTypeController.setLobbyStage(Lobbystage);
        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);
        Scene scene = new Scene(pane);
        if(selectedGame.getState() == GameState.IN_PROGRESS) {
            clientTypeController.setPlayerVisibility(false);
        }
        window.setScene(scene);
        window.setAlwaysOnTop(true);
        window.getScene().getStylesheets().add("https://fonts.googleapis.com/css?family=Pirata+One");
        window.showAndWait();
    }

    public Stage getLobbyStage(){
        return this.LobbyStage;
    }
    public ClientTypeController getClientTypeController(){return this.clientTypeController;}
    public void setClientTypeController(ClientTypeController clientTypeController){this.clientTypeController = clientTypeController;}
}
