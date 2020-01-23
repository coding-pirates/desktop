package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.util.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nonnull;
import java.util.ResourceBundle;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin {

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

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"), ResourceBundle.getBundle("lang/serverLogin", Settings.getLocale()));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        ServerLoginController serverLoginController = loader.getController();
        serverLoginController.setMain(main);
        loginStage.setTitle("Login");
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Pirata+One");
        loginStage.setScene(scene);
        initDimensions(loginStage);
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }

    /**
     * this method sets the dimensions for {@param stage}. Currently sets width and height to display size. Also removes the windows task bar.
     * @param stage instance of the stage the dimensions shall be set
     */
    private void initDimensions(Stage stage) {

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        //remove the windows taskbar
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();
    }
}
