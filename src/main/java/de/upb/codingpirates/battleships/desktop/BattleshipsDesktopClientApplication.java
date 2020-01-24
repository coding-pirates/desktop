package de.upb.codingpirates.battleships.desktop;

import com.google.errorprone.annotations.FormatMethod;
import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.network.ClientConnectorDesktop;
import de.upb.codingpirates.battleships.desktop.start.Start;
import de.upb.codingpirates.battleships.desktop.start.StartController;
import de.upb.codingpirates.battleships.desktop.util.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * class to start the Battleship Desktop-Client-Application
 */
public final class BattleshipsDesktopClientApplication extends Application {

    /**
     * first Stage in this Application
     */
    private Stage startStage;

    /**
     * Client Connector
     */
    public static ClientConnector tcpConnector = ClientApplication.create();

    /**
     * Title of the starting window
     */
    private static final String TITLE = "Coding Pirates Battleships Desktop Client";

    /**
     * application itself
     */
    private static BattleshipsDesktopClientApplication instance;

    /**
     * MediaView for starting the media-player
     */
    public static MediaView mediaView;

    /**
     * sounds are on or off
     */
    public static boolean sounds = true;

    /**
     * Main method
     * @param args
     */
    public static void main(@Nonnull final String... args) {
        launch(args);
    }

    /**
     * constructor of the class BattleshipDesktopClientApplication
     * @return
     */
    public static BattleshipsDesktopClientApplication getInstance() {
        return instance;
    }

    /**
     * overrides the method from the class Application
     */
    @Override
    public void init() {
        instance = this;
    }

    /**
     * Loads the StartView.fxml, creates a Start.java Window and related Controller
     *
     * @param startStage first stage, which is displayed
     * @throws IOException if stage could not be loaded
     */
    public void start(@Nonnull final Stage startStage) {
            Settings.init();
            this.startStage = startStage;
            startStage.setOnCloseRequest(t-> {
                Platform.exit();
                System.exit(0);
            });
            try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartView.fxml"));
            AnchorPane pane = loader.load();
            StartController startcontroller = loader.getController();
            startcontroller.setMain(this);

            Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
            startStage.getIcons().add(icon);

            //init a mediaplayer for every song
            final URL resource1 = getClass().getResource("/raw/sea_of_thieves_theme_song.mp3");
            final MediaPlayer mediaplayer1 = new MediaPlayer(new Media(resource1.toString()));

            final URL resource2 = getClass().getResource("/raw/becalmed_concertina_hurdygurdy.mp3");
            final MediaPlayer mediaplayer2 = new MediaPlayer(new Media(resource2.toString()));

            final URL resource3 = getClass().getResource("/raw/buson_bill_concertina_hurdygurdy.mp3");
            final MediaPlayer mediaplayer3 = new MediaPlayer(new Media(resource3.toString()));

            final URL resource4 = getClass().getResource("/raw/maiden_voyage.mp3");
            final MediaPlayer mediaplayer4 = new MediaPlayer(new Media(resource4.toString()));

            final URL resource5 = getClass().getResource("/raw/rise_of_the_valkyries_concertina.mp3");
            final MediaPlayer mediaplayer5 = new MediaPlayer(new Media(resource5.toString()));

            final URL resource6 = getClass().getResource("/raw/sea_of_thieves_theme_song.mp3");
            final MediaPlayer mediaplayer6 = new MediaPlayer(new Media(resource6.toString()));

            final List<MediaPlayer> players = new ArrayList<MediaPlayer>(Arrays.asList(mediaplayer1, mediaplayer2, mediaplayer3, mediaplayer4, mediaplayer5, mediaplayer6));

            //set the next mediaplayer, when one player has finished
            mediaView = new MediaView(players.get(new Random().nextInt(5)));
            for (int i = 0; i < players.size(); i++) {
                final MediaPlayer player = players.get(i);
                final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
                player.setOnEndOfMedia(() -> {
                    nextPlayer.setVolume((double)Settings.getMusicVolume()/100);
                    nextPlayer.setMute(Settings.getMusicMute());
                    mediaView.setMediaPlayer(nextPlayer);
                    nextPlayer.play();
                });

            }

            startStage.setScene(new Scene(pane));
            Start();

            mediaView.getMediaPlayer().setVolume(0.1);
            mediaView.getMediaPlayer().setVolume((double)Settings.getMusicVolume()/100);
            mediaView.getMediaPlayer().play();
            mediaView.getMediaPlayer().setMute(Settings.getMusicMute());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns the first stage
     * @return startStage
     */
    public Stage getStartStage() {
        return startStage;
    }

    /**
     * Returns the TcpConnector to server
     * @return tcpConnector
     */

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }

    /**
     * Returns the boolean value of sounds
     * @return true, if the sound is on
     *         false, is the sound is off
     */
    public static boolean getSoundsOff(){
        return sounds;
    }

    /**
     * Sets the sound in the value, which  is given in parameter
     * @param value true, if the sound is turned on,
     *              false, if the sound is turned off
     */
    public static void setSoundsOff(boolean value){
        sounds = value;
    }

    /**
     * used for setting up the start stage in {@link Start}
     * @throws IOException
     */
    @FXML
    private void Start() throws IOException {
        Start start = new Start();
        start.display(startStage);
    }
}
