package de.upb.codingpirates.battleships.desktop.settings;



import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that implements a Window to set the settings
 */
public class Settings {


    Stage settingsStage;
    private SettingsController settingsController;


    /**
     * Start Method that creates a new Window and a related Controller.
     */
    public void start() throws Exception {
        this.settingsStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"));
        AnchorPane pane = loader.load();
        this.settingsController = loader.getController();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        settingsStage.getIcons().add(icon);


        settingsStage.setTitle("Settings");
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setResizable(false);
        settingsStage.setScene(new Scene(pane));
        settingsStage.showAndWait();
    }

}
