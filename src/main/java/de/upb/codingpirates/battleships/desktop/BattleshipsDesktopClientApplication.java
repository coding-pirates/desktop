package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.start.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class BattleshipsDesktopClientApplication extends Application {

    private Stage startStage;

    public static ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

    private static final String TITLE = "Coding Pirates Battleships Desktop Client";

    private static BattleshipsDesktopClientApplication instance;

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
     * Loads the StartView.fxml, creates a Start Window and related Controller.
     */
    public void start(@Nonnull final Stage startStage) throws Exception {
        this.startStage = startStage;
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartView.fxml"));
        AnchorPane pane = loader.load();
        StartController startcontroller = loader.getController();
        startcontroller.setMain(this);

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        startStage.getIcons().add(icon);

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


        final MediaView mediaView = new MediaView(players.get( new Random().nextInt(5)));
            for (int i = 0; i < players.size(); i++) {
                final MediaPlayer player     = players.get(i);
                final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
                player.setOnEndOfMedia(new Runnable() {

                    @Override public void run() {
                        mediaView.setMediaPlayer(nextPlayer);
                        nextPlayer.play();

                    }

                });

            }


        Scene scene = new Scene(pane);
        startStage.setMaximized(true);
        startStage.setResizable(false);
        startStage.setTitle(TITLE);
        startStage.setScene(scene);
        mediaView.getMediaPlayer().play();
        startStage.show();}
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public Stage getLoginStage() {
        return startStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }

}
