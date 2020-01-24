package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class ClientType {
    private ClientTypeController clientTypeController;
    private Stage window;
    private Stage LobbyStage;
    private FXMLLoader loader;
    private AnchorPane pane;
    private Scene scene;

    public ClientType() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientTypeView.fxml"), ResourceBundle.getBundle("lang/desktop", de.upb.codingpirates.battleships.desktop.util.Settings.getLocale()));
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

        if(selectedGame.getState() == GameState.IN_PROGRESS) {
            clientTypeController.setPlayerVisibility(false);
        }
        window.getScene().getStylesheets().add("https://fonts.googleapis.com/css?family=Pirata+One");
        window.showAndWait();
    }

    public Stage getLobbyStage(){
        return this.LobbyStage;
    }
    public ClientTypeController getClientTypeController(){return this.clientTypeController;}
    public void setClientTypeController(ClientTypeController clientTypeController){this.clientTypeController = clientTypeController;}
}
