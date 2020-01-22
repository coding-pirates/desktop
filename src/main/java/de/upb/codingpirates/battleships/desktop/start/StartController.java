package de.upb.codingpirates.battleships.desktop.start;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the Start.java Window.
 */
public class StartController implements Initializable {

    //Views
    @FXML
    private Button btn_start;
    @FXML
    ImageView startscreen_imageview;

    private BattleshipsDesktopClientApplication main;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        double displayWidth = Screen.getPrimary().getBounds().getWidth();
        double displayHeight = Screen.getPrimary().getBounds().getHeight();

        //used for scaling the background image
        startscreen_imageview.setImage(new Image("images/startscreen_full_hd.png", displayWidth, displayHeight, true, false));
        btn_start.setLayoutX(displayWidth/2);
        btn_start.setLayoutY(displayHeight*0.8);
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
