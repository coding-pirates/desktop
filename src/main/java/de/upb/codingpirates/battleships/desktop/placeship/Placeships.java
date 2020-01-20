package de.upb.codingpirates.battleships.desktop.placeship;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Class that implements a Window to place the ships
 */
public class Placeships extends Application {

    /**
     * stage of this view
     */
    private Stage placeshipsStage;
    /**
     * controllerclass to this view
     */
    private PlaceShipsController placeshipsController;

    /**
     * Start Method that creates a new Window and a related Controller.
     */
    public void start(Stage placeshipsStage) throws Exception {
        this.placeshipsStage = placeshipsStage;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/PlaceshipsView.fxml"));
        AnchorPane pane = loader.load();
        this.placeshipsController = loader.getController();
        placeshipsController.fieldInit(null);
        placeshipsController.setShipForm();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        placeshipsStage.getIcons().add(icon);
        placeshipsStage.setResizable(false);
        placeshipsStage.setMaximized(true);
        placeshipsStage.setTitle("PlaceShips");
        placeshipsStage.setScene(new Scene(pane));
        placeshipsStage.show();
    }

    /**
     * Closes the PlaceShips Window
     */
    protected void close() {
        placeshipsStage.close();
    }


}
