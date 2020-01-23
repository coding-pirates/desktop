package de.upb.codingpirates.battleships.desktop;

import com.google.errorprone.annotations.FormatMethod;
import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.network.ClientConnectorDesktop;
import de.upb.codingpirates.battleships.desktop.start.Start;
import de.upb.codingpirates.battleships.desktop.start.StartController;
import javafx.application.Application;
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

public final class BattleshipsDesktopClientApplication extends Application {

    private Stage startStage;

    public static ClientConnectorDesktop tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnectorDesktop.class));

    private static final String TITLE = "Coding Pirates Battleships Desktop Client";

    private static BattleshipsDesktopClientApplication instance;

    public static MediaView mediaView;

    public static boolean sounds = true;

    public static void main(@Nonnull final String... args) {
        launch(args);
    }

    public static BattleshipsDesktopClientApplication getInstance() {
        return instance;
    }


    @Override
    public void init() {
        instance = this;
    }

    /**
     * Loads the StartView.fxml, creates a Start.java Window and related Controller.
     */
    public void start(@Nonnull final Stage startStage) throws Exception {
        this.startStage = startStage;
        try {
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
                    nextPlayer.setVolume(0.1);
                    mediaView.setMediaPlayer(nextPlayer);
                    nextPlayer.play();
                });

            }

            startStage.setScene(new Scene(pane));
            Start();

            mediaView.getMediaPlayer().setVolume(0.1);
            mediaView.getMediaPlayer().play();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Stage getStartStage() {
        return startStage;
    }

    public ClientConnectorDesktop getTcpConnector() {
        return tcpConnector;
    }

    public static boolean getSoundsOff() {
        return sounds;
    }

    public static void setSoundsOff(boolean value) {
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
