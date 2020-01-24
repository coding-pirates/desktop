package de.upb.codingpirates.battleships.desktop.settings;

import com.google.common.collect.Maps;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.util.Help;
import de.upb.codingpirates.battleships.desktop.util.Impressum;
import de.upb.codingpirates.battleships.desktop.util.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller Class for the Settings Window.
 */
public class SettingsController implements Initializable {

    /**
     * main class
     */
    private BattleshipsDesktopClientApplication main;

    //Views
    /**
     * button to leave the settings
     */
    @FXML
    private Button btn_back;
    /**
     * Slider to changes the volume
     */
    @FXML
    private Slider volume;
    /**
     * Image for sounds
     */
    @FXML
    private ImageView soundImg;
    /**
     * Image for volume
     */
    @FXML
    private ImageView volumeImg;

    @FXML
    public ChoiceBox<String> language;

    @FXML
    private Label settings_restart_info_label;

    private Map<String, Locale> languages = Maps.newHashMap();

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        settings_restart_info_label.setWrapText(true);
        if (BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().isMute()){
            volumeImg.setImage(new Image(String.valueOf(getClass().getResource("/images/SoundOff_Icon.png"))));
        } else {
            volumeImg.setImage(new Image(String.valueOf(getClass().getResource("/images/icon_sound.png"))));
        }

        if (BattleshipsDesktopClientApplication.sounds){
            soundImg.setImage(new Image(String.valueOf(getClass().getResource("/images/icon_music.png"))));
        } else {
            soundImg.setImage(new Image(String.valueOf(getClass().getResource("/images/SoundEffect_Icon.png"))));
        }

        List<Locale> lists = Settings.getAllLanguages();
        lists.sort(Comparator.comparing(locale->((Locale)locale).getDisplayLanguage()).thenComparing(locale-> ((Locale)locale).getDisplayCountry()));
        lists.forEach(locale -> languages.put(locale.getDisplayName(), locale));
        language.setItems(FXCollections.observableList(lists.stream().map(Locale::getDisplayName).collect(Collectors.toList())));
        Locale locale = Settings.getLocale();
        language.getSelectionModel().select(locale.getDisplayName());
        language.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> Settings.setLocale(languages.get(newValue))));
    }

    /**
     * set the main Application-window
     * @param main
     */
    public void setMain(BattleshipsDesktopClientApplication main) {
        this.main = main;
    }

    /**
     * closes the settingsWindow
     */
    @FXML
    public void closeStage(){
        Stage stage = (Stage) btn_back.getScene().getWindow();
        Settings.save();
        stage.close();
    }

    /**
     * starts the HelpView with accessibility tools in an extra window
     * @throws IOException
     */
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

    /**
     * starts the ImpressumWindow in an extra window
     * @throws IOException
     */
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

    /**
     * starts the piratenkodexWindow in an extra window
     * @throws IOException
     */
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

    /**
     * sets the volume to another value by the slider
     */
    public void setVolume(){
        volume.setValue(BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().getVolume()*100);
        volume.valueProperty().addListener(observable -> {
            BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setVolume(volume.getValue()/100);
            Settings.setMusicVolume((int)volume.getValue());
        });
}

    /**
     * sets the volume off, changes Image
     */
    @FXML
    public void volumeOff(){
        if (BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().isMute()){
            BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setMute(false);
            volumeImg.setImage(new Image(String.valueOf(getClass().getResource("/images/icon_sound.png"))));
            Settings.setMusicMute(false);
        } else{
            BattleshipsDesktopClientApplication.mediaView.getMediaPlayer().setMute(true);
            volumeImg.setImage(new Image(String.valueOf(getClass().getResource("/images/SoundOff_Icon.png"))));
            Settings.setMusicMute(true);
        }

    }

    /**
     * sets the sounds off, changes Image
     */
    @FXML
    public void soundsOff(){
        if (BattleshipsDesktopClientApplication.sounds){
            BattleshipsDesktopClientApplication.setSoundsOff(false);
            soundImg.setImage(new Image(String.valueOf(getClass().getResource("/images/SoundEffect_Icon.png"))));
        } else {
            BattleshipsDesktopClientApplication.setSoundsOff((true));
            soundImg.setImage(new Image(String.valueOf(getClass().getResource("/images/icon_music.png"))));
        }
        Settings.setMusicMute(!BattleshipsDesktopClientApplication.sounds);
    }


}