package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.desktop.util.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin extends Fullscreen {

    /**
     * stage for this view
     */
    private Stage loginStage;

    private final ClientConnector tcpConnector = ClientApplication.create();

    /**
     * instance itself
     */
    private static ServerLogin instance;



    /**
     * Loads the ServerLogin.fxml, creates a ServerLogin Window and related Controller.
     */
    public void display(@Nonnull final Stage loginStage) throws IOException {
        this.loginStage = loginStage;

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"), ResourceBundle.getBundle("lang/desktop", Settings.getLocale()));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        loginStage.setTitle("Login");
        Scene scene = new Scene(pane);
        loginStage.setScene(scene);
        super.display(loginStage);
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
