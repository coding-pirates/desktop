package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin {

    private Stage loginStage;

    private ServerLoginController serverLoginController;

    private final ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

    private static ServerLogin instance;


    public static ServerLogin getInstance() {
        return instance;
    }


    public void init() {
        instance = this;
    }

    /**
     * Loads the ServerLogin.fxml, creates a ServerLogin Window and related Controller.
     */
    public void display(@Nonnull final Stage loginStage) throws Exception {
        this.loginStage = loginStage;

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        this.serverLoginController = loader.getController();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(pane));
        loginStage.show();
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }
}