package de.upb.codingpirates.battleships.desktop.settings;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that implements a Window to set the settings
 */
public class Settings extends Application {


    Stage settingsStage;
    private SettingsController settingsController;

    /**
     * Start Method that creates a new Window and a related Controller.
     */
    public void start(Stage settingStage) throws Exception {
        this.settingsStage = settingStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"));
        AnchorPane pane = loader.load();
        this.settingsController = loader.getController();
        settingsStage.setTitle("Settings");
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setScene(new Scene(pane));
        settingsStage.show();
    }

}
