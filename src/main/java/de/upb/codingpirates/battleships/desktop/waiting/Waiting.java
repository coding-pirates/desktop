package de.upb.codingpirates.battleships.desktop.waiting;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Waiting  {

    private Stage waitingStage;
    public void display(Stage waitingStage) throws IOException {
        this.waitingStage = waitingStage;

        waitingStage.initModality(Modality.APPLICATION_MODAL);
        waitingStage.setTitle("Waiting");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/WaitingView.fxml"));
        AnchorPane pane = loader.load();

        WaitingController waitingController = loader.getController();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        waitingStage.getIcons().add(icon);
        Scene scene = new Scene(pane);
        waitingStage.setScene(scene);
        waitingStage.showAndWait();
    }

}
