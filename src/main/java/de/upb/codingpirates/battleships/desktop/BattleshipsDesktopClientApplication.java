package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;

public final class BattleshipsDesktopClientApplication extends Application {

    private Stage startStage;

    private final ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

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

        FXMLLoader loader = new FXMLLoader(BattleshipsDesktopClientApplication.class.getResource("/fxml/StartView.fxml"));
        AnchorPane pane = loader.load();

        startStage.setResizable(false);
        startStage.setTitle(TITLE);
        startStage.setScene(new Scene(pane, 600, 400));
        startStage.show();
    }

    public Stage getStartStage() {
        return startStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }
}
