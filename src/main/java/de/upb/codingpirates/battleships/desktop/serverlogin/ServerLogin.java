package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nonnull;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin {

    private Stage stage;

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
        this.stage = loginStage;

        FXMLLoader loader = new FXMLLoader(ServerLogin.class.getResource("/fxml/ServerLogin.fxml"));
        AnchorPane pane = loader.load();

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        loginStage.getIcons().add(icon);

        this.serverLoginController = loader.getController();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(pane));
        initDimensions(loginStage);
    }

    public Stage getLoginStage() {
        return stage;
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
