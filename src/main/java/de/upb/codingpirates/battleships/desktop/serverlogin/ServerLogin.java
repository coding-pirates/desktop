package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.util.Fullscreen;
import de.upb.codingpirates.battleships.desktop.util.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.util.ResourceBundle;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin extends Fullscreen {

    private Stage loginStage;

    private BattleshipsDesktopClientApplication main;

    private final ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

    private static ServerLogin instance;

    public ServerLogin(BattleshipsDesktopClientApplication main) {
        this.main = main;
    }

    /**
     * Loads the ServerLogin.fxml, creates a ServerLogin Window and related Controller.
     */
    public void display(@Nonnull final Stage loginStage) throws Exception {
        this.loginStage = loginStage;

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"), ResourceBundle.getBundle("lang/desktop", Settings.getLocale()));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        ServerLoginController serverLoginController = loader.getController();
        serverLoginController.setMain(main);
        loginStage.setTitle("Login");
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Pirata+One");
        loginStage.setScene(scene);
        super.display(loginStage);
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }

}
