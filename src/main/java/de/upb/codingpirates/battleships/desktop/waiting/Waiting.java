package de.upb.codingpirates.battleships.desktop.waiting;

import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.desktop.util.FxmlLoader;
import de.upb.codingpirates.battleships.logic.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Waiting extends Fullscreen implements FxmlLoader {

    private Stage waitingStage;
    private WaitingController waitingController;
    private int clientID;

    public void display(Stage stage, Game currentGame,int clientID) throws IOException {
        this.waitingStage = stage;
        this.clientID = clientID;

        waitingStage.initModality(Modality.APPLICATION_MODAL);
        waitingStage.setTitle("Waiting");

        FXMLLoader loader = this.getLoader("WaitingView");
        AnchorPane pane = loader.load();

        waitingController = loader.getController();
        waitingController.setCurrentGame(currentGame);
        waitingController.setClientId(clientID);
        waitingController.setModel();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        waitingStage.getIcons().add(icon);
        Scene scene = new Scene(pane);
        waitingStage.setScene(scene);
        super.display(waitingStage);

        waitingStage.show();
    }

}
