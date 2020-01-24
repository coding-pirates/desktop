package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientType {
    private ClientTypeController clientTypeController;
    private Stage window;
    private Stage LobbyStage;
    private FXMLLoader loader;
    private AnchorPane pane;
    private Scene scene;

    public ClientType() throws IOException {
        this.loader = new FXMLLoader(this.getClass().getResource("/fxml/ClientTypeView.fxml"));
        this.pane = loader.load();
        this.clientTypeController = loader.getController();
        this.scene = new Scene(this.pane);
    }

    public void display(Stage window, Game selectedGame, int clientID, Stage lobbystage) throws IOException {
        this.window=window;
        this.LobbyStage=lobbystage;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");

        this.clientTypeController.setLobbyStage(this.LobbyStage);
        this.clientTypeController.setClientID(clientID);
        this.clientTypeController.setSelectedGame(selectedGame);


        Image icon = new Image(String.valueOf(ClientType.class.getResource("/images/app_icon.png")));
        window.getIcons().add(icon);
        window.setScene(this.scene);
        window.showAndWait();
    }

    public Stage getLobbyStage(){
        return this.LobbyStage;
    }
    public ClientTypeController getClientTypeController(){return this.clientTypeController;}
    public void setClientTypeController(ClientTypeController clientTypeController){this.clientTypeController = clientTypeController;}
}
