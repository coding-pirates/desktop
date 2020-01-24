package de.upb.codingpirates.battleships.desktop.clienttype;

import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.GameState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ClientType implements FxmlLoader {
    private ClientTypeController clientTypeController;
    private Stage window;
    private Stage LobbyStage;
    private FXMLLoader loader;
    private AnchorPane pane;
    private Scene scene;

    /**
     * Constructor for ClientType.
     * @throws IOException
     */
    public ClientType() throws IOException {
        this.loader = this.getLoader("ClientTypeView");
        this.pane = loader.load();
        this.clientTypeController = loader.getController();
        this.scene = new Scene(this.pane);
    }

    /**
     * Displays the Stage and sets all Variables.
     * @param window
     * @param selectedGame
     * @param clientID
     * @param lobbystage
     * @throws IOException
     */
    public void display(Stage window, Game selectedGame, int clientID, Stage lobbystage) throws IOException {
        this.window=window;
        this.LobbyStage=lobbystage;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ClientType");
        window.initStyle(StageStyle.UNDECORATED);
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
}
