package de.upb.codingpirates.battleships.desktop.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that initials the Lobby Window.
 */
public class Lobby extends Application {

    /**
     * lobbyStage for this window
     */
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
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        lobbyStage.getIcons().add(icon);
        lobbyStage.setMaximized(true);
        lobbyStage.setResizable(false);
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
