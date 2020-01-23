package de.upb.codingpirates.battleships.desktop.waiting;

import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Waiting  {

    private Stage waitingStage;
    private WaitingController waitingController;
    public void display(Stage waitingStage, Game currentGame) throws IOException {
        this.waitingStage = waitingStage;

        waitingStage.initModality(Modality.APPLICATION_MODAL);
        waitingStage.setTitle("Waiting");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/WaitingView.fxml"));
        AnchorPane pane = loader.load();

        waitingController = loader.getController();
        waitingController.setCurrentGame(currentGame);
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        waitingStage.getIcons().add(icon);
        Scene scene = new Scene(pane);
        waitingStage.setScene(scene);
        waitingStage.showAndWait();
    }
    public void setCurrentGame(Game currentGame){
        waitingController.setCurrentGame(currentGame);
    }
}
