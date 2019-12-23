package de.upb.codingpirates.battleships.desktop.serverlogin;

import de.upb.codingpirates.battleships.client.network.ClientApplication;
import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.client.network.ClientModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.InputStream;

/**
 * Class that implements a Window to login to the game
 */
public class ServerLogin extends Application {

    private Stage loginStage;

    private final ClientConnector tcpConnector = ClientApplication.create(new ClientModule<>(ClientConnector.class));

    private static final String TITLE = "Coding Pirates Battleships Desktop Client";

    private static ServerLogin instance;


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


        InputStream input = getClass().getResourceAsStream("/images/Settings_Icon.svg");
        Image image = new Image(input);
        ImageView imageview = new ImageView(image);
        Button btn_settings = new Button("Settings", imageview);
        pane.getChildren().addAll(btn_settings);

        loginStage.setResizable(false);
        loginStage.setTitle(TITLE);
        loginStage.setScene(new Scene(pane, 600, 400));
        loginStage.show();
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public ClientConnector getTcpConnector() {
        return tcpConnector;
    }
}
