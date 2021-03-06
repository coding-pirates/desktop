package de.upb.codingpirates.battleships.desktop.lobby;

import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that initials the Lobby Window.
 */
public class Lobby extends Fullscreen implements FxmlLoader {

    /**
     * lobbyStage for this window
     */
    private Stage lobbyStage;

    /**
     * Creates a Lobby Window and the related Controller.
     */
    public void display(Stage lobbyStage, int clientID) throws IOException {
        this.lobbyStage = lobbyStage;
        FXMLLoader loader = this.getLoader("Lobby");
        AnchorPane pane = loader.load();
        LobbyController lobbyController = loader.getController();
        lobbyController.setLobby(this);
        lobbyController.setClientID(clientID);
        lobbyController.setChangeListener();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        lobbyStage.getIcons().add(icon);
        lobbyStage.setTitle("Lobby");
        lobbyStage.setScene(new Scene(pane));
        super.display(lobbyStage);
        lobbyController.showgames();

    }

    /**
     * Closes the Lobby Window.
     */
    protected void close() {
        lobbyStage.close();
    }
    public Stage getStage(){
        return this.lobbyStage;
    }
}
