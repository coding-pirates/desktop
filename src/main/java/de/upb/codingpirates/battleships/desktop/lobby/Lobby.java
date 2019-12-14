package de.upb.codingpirates.battleships.desktop.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that initials the Lobby Window.
 */
public class Lobby extends Application {

    private Stage lobbyStage;

    /**
     * Creates a Lobby Window and the related Controller.
     */
    public void start(Stage lobbyStage) throws IOException {
        this.lobbyStage = lobbyStage;
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
        AnchorPane pane = loader.load();
        LobbyController lobbyController = loader.getController();
        lobbyController.setLobby(this);
        lobbyStage.setTitle("Lobby");
        lobbyStage.setScene(new Scene(pane));
        lobbyStage.show();
        lobbyController.showgames();

    }

    /**
     * Closes the Lobby Window.
     */
    protected void close() {
        lobbyStage.close();
    }

}
