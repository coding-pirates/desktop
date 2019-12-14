package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The inintial Class.
 * Starts the ServerLoginWindow.
 */
public class SpectatorApp extends Application {

    public static ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void close() {
        primaryStage.close();
    }

    /**
     * Loads the ServerLogin.fxml, creates a ServerLogin Window and related Controller.
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(SpectatorApp.class.getResource("/fxml/ServerLogin.fxml"));
        AnchorPane pane = loader.load();
        ServerLoginController serverLoginController = loader.getController();
        serverLoginController.setMain(this);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Server-Login_______Titanic Games");
        primaryStage.setScene(new Scene(pane, 600, 400));
        primaryStage.show();

    }

}
