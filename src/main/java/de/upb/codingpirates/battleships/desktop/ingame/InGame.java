package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InGame {
    private InGameController inGameController;
    public void start(Stage inGameStage ) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InGameView.fxml"));
        AnchorPane pane = loader.load();
        inGameController = loader.getController();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        inGameStage.getIcons().add(icon);
        inGameStage.setTitle("InGame");
        inGameStage.setScene(new Scene(pane));
        inGameStage.setMaximized(true);
        inGameStage.show();
        //inGameController.spectatorGameStateResponse(player, shots, ships, gameState);
        //inGameController.setGame(ausgewaehltesSpiel);
    }

    public void setCurrentGame(Game currentGame){
       this.inGameController.setGame(currentGame);
    }
}
