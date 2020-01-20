package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin extends Application {

    /**
     * stage for this view
     */
    private Stage loginStage;
    /**
     * ControllerClass to this view
     */
    private ServerLoginController serverLoginController;
    /**
     * connector to server
     */
    private final ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

    /**
     * instance itself
     */
    private static ServerLogin instance;

    /**
     * Get the instance itself
     * @return instance
     */
    public static ServerLogin getInstance() {
        return instance;
    }

    @Override
    public void init() {
        instance = this;
    }

    /**
     * Loads the ServerLogin.fxml, creates a ServerLogin Window and related Controller.
     */
    public void start(@Nonnull final Stage loginStage) throws Exception {
        this.loginStage = loginStage;

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        this.serverLoginController = loader.getController();
        loginStage.setTitle("Login");
        loginStage.setMaximized(true);
        loginStage.setResizable(false);
        loginStage.setScene(new Scene(pane));
        loginStage.show();
    }

    /**
     * Gets the LoginStage
     * @return loginStage
     */
    public Stage getLoginStage() {

        return loginStage;
    }

    /**
     * Gets the ClientConnector to connect with the server
     * @return tcpVonnector
     */
    public ClientConnector getTcpConnector() {

        return tcpConnector;
    }
}
