package de.upb.codingpirates.battleships.desktop.settings;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.desktop.util.Impressum;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the Settings Window.
 */
public class SettingsController implements Initializable {

    private BattleshipsDesktopClientApplication main;

    //Views
    @FXML
    private Button btn_back;

    @FXML
    private Slider volume;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setMain(BattleshipsDesktopClientApplication main) {

        this.main = main;
    }

    @FXML
    public void closeStage(){
        Stage stage = (Stage) btn_back.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void help() throws IOException {
        Help help = new Help();
        try{
            help.display("Settings-Help");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void impressum() throws IOException{
        Impressum impressum = new Impressum();
        try{
            impressum.display("Impressum");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void piratenkodex() throws IOException{
        Impressum impressum = new Impressum();
        try{
            impressum.display("Piratenkodex");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setVolume(){
        volume.setValue(BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().getVolume()*100);
        volume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setVolume(volume.getValue()/100);
            }
        });
}

    @FXML
    public void volumeOff(){
        if (BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().isMute()){
            BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setMute(false);
            //TODO Icon switching
        }
        else{
            BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setMute(true);
            //TODO Icon switching
        }

    }

    @FXML
    public void soundsOff(){
        if (BattleshipsDesktopClientApplication.sounds){
            BattleshipsDesktopClientApplication.setSoundsOff(false);
            //TODO Icon switching
        }
        else
        {
            BattleshipsDesktopClientApplication.setSoundsOff((true));
            //TODO Icon switchung
        }
    }


}