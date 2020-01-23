package de.upb.codingpirates.battleships.desktop.start;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the Start.java Window.
 */
public class StartController implements Initializable {

    //Views
    @FXML
    Button btn_start;
    @FXML
    ImageView startscreen_imageview;
    @FXML
    ImageView startscreen_logoview;

    private BattleshipsDesktopClientApplication main;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        double displayWidth = Screen.getPrimary().getBounds().getWidth();
        double displayHeight = Screen.getPrimary().getBounds().getHeight();

        //used for scaling the background image to screensize
        startscreen_imageview.setFitHeight(displayHeight);
        startscreen_imageview.setFitWidth(displayWidth);

        //start button configuration
        double btn_startXSize = displayWidth * 414/1920;
        double btn_startYSize = displayHeight * 120/1080;

        btn_start.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/button_start_red.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(btn_startXSize,btn_startYSize,true,true,true,true))));
        btn_start.setPrefSize(btn_startXSize, btn_startYSize);

        btn_start.setLayoutX(displayWidth * 0.5 - btn_startXSize / 2);
        btn_start.setLayoutY(displayHeight * 0.8 - btn_startYSize / 2);

        //logo configuration
        double logoViewXSize = displayWidth * 900/1920;
        double logoViewYSize = displayWidth * 800/1080;

        startscreen_logoview.setFitWidth(logoViewXSize);
        startscreen_logoview.setFitHeight(logoViewYSize);
        startscreen_logoview.setLayoutX(displayWidth / 2 - logoViewXSize / 2);
        startscreen_logoview.setLayoutY(displayHeight * 0.7 - logoViewYSize / 2);

    }

    public void setMain(BattleshipsDesktopClientApplication main) {

        this.main = main;
    }


    public void closeStage(){
        main.getStartStage().close();
    }

    @FXML
    public void handleButton() throws Exception {
        ServerLogin login = new ServerLogin();
        Stage loginStage = new Stage();
        try {
            login.display(loginStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
        loginStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
