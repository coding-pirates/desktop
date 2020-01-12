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
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;

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

        final URL resource = getClass().getResource("/raw/sea_of_thieves_theme_song.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaplayer = new MediaPlayer(media);


        Scene scene = new Scene(pane);
        startStage.setMaximized(true);
        startStage.setResizable(false);
        startStage.setTitle(TITLE);
        startStage.setScene(scene);
        mediaplayer.play();
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
