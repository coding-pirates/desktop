package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.start.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;

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

        Scene scene = new Scene(pane);
        startStage.setTitle(TITLE);
        startStage.setScene(scene);
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

    public void close() {
        this.close();
    }
}
