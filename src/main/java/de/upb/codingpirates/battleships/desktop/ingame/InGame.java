package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
import de.upb.codingpirates.battleships.logic.ClientType;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.logic.PlacementInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Map;

public class InGame extends Fullscreen implements FxmlLoader {
    private InGameController inGameController;

    /**
     * Initialisation Method for GameView
     * @param inGameStage
     * @param currentGame
     * @param clientType
     * @param placedShips
     * @throws Exception
     */
    public void start(Stage inGameStage, Game currentGame, ClientType clientType, Map<Integer, PlacementInfo> placedShips, int clientID) throws Exception {
        FXMLLoader loader = this.getLoader("InGameView");
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        inGameController.setGame(currentGame);
        inGameController.setClientType(clientType);
        inGameController.setClientID(clientID);
        inGameController.setOwnShipPlacements(placedShips);
        inGameController.sendGameStateRequest();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        inGameStage.getIcons().add(icon);
        inGameStage.setTitle("InGame");
        inGameStage.setScene(new Scene(pane));
        super.display(inGameStage);
        //inGameController.spectatorGameStateResponse(player, shots, ships, gameState);
        //inGameController.setGame(ausgewaehltesSpiel);
    }

    /**
     * Initialisation for SpectatorView
     * @param inGameStage
     * @param currentGame
     * @param clientType
     * @throws Exception
     */
    public void start(Stage inGameStage, Game currentGame, ClientType clientType) throws Exception {
        FXMLLoader loader = this.getLoader("InGameView");
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        inGameController.setGame(currentGame);
        inGameController.setClientType(clientType);
        inGameController.sendGameStateRequest();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        inGameStage.getIcons().add(icon);
        inGameStage.setTitle("InGame");
        inGameStage.setScene(new Scene(pane));
        super.display(inGameStage);
        inGameStage.setMaximized(true);
        inGameStage.show();
        //inGameController.spectatorGameStateResponse(player, shots, ships, gameState);
        //inGameController.setGame(ausgewaehltesSpiel);
    }

    }
