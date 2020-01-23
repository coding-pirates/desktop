package de.upb.codingpirates.battleships.desktop.settings;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Class that implements a Window to set the settings
 */
public class Settings {


    Stage settingsStage;
    private SettingsController settingsController;


    /**
     * Start.java Method that creates a new Window and a related Controller.
     */
    public void display(Stage settingsStage) throws IOException {
        this.settingsStage = settingsStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"), ResourceBundle.getBundle("lang/desktop", de.upb.codingpirates.battleships.desktop.util.Settings.getLocale()));
        AnchorPane pane = loader.load();
        this.settingsController = loader.getController();
        settingsController.setVolume();
        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        settingsStage.getIcons().add(icon);

        settingsStage.setTitle("Settings");
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setResizable(false);
        settingsStage.setScene(new Scene(pane));
        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.showAndWait();
    }

}
