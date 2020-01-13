package de.upb.codingpirates.battleships.desktop.endgame;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Class that implements a Window to place the ships
 */
public class Endgame {


    private Stage endStage;
    private EndgameController EndgameController;

    /**
     * Start Method that creates a new Window and a related Controller.
     */
    public void display(Stage endStage) throws Exception {
        this.endStage = endStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EndgameView.fxml"));
        AnchorPane pane = loader.load();
        this.EndgameController = loader.getController();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        endStage.getIcons().add(icon);
        endStage.setTitle("Endgame");
        endStage.setMaximized(true);
        endStage.setResizable(false);
        endStage.setScene(new Scene(pane));
        endStage.show();
    }

}
