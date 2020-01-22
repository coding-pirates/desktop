package de.upb.codingpirates.battleships.desktop.start;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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


    private BattleshipsDesktopClientApplication main;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        btn_start.setLayoutX(Screen.getPrimary().getBounds().getWidth()/2);
        btn_start.setLayoutY(Screen.getPrimary().getBounds().getHeight()*0.8);
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
