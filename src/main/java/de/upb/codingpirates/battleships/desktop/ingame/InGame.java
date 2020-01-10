package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.logic.Client;
import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Collection;

public class InGame {
    private InGameController inGameController;
    public void start(Stage inGameStage, Game currentGame, Collection<Client> clientList) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InGameView.fxml"));
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        inGameController.setGame(currentGame);
        inGameController.gameInit(currentGame.getConfig(),clientList);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        inGameStage.getIcons().add(icon);
        inGameStage.setTitle("InGame");
        inGameStage.setScene(new Scene(pane));
        inGameStage.setMaximized(true);
        inGameStage.show();
        //inGameController.spectatorGameStateResponse(player, shots, ships, gameState);
        //inGameController.setGame(ausgewaehltesSpiel);
    }
    }
