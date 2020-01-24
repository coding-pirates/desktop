package de.upb.codingpirates.battleships.desktop.start;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller Class for the Start Window.
 */
public class StartController implements Initializable {

    //Views
    /**
     * startingButton
     */
    @FXML
    private Button btn_start;

    /**
     * main Application window
     */
    private BattleshipsDesktopClientApplication main;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /**
     * sets main Application
     * @param main
     */
    public void setMain(BattleshipsDesktopClientApplication main) {

        this.main = main;
    }

    /**
     * closes the startView
     */
    public void closeStage(){
        main.getStartStage().close();
    }

    /**
     * starts the ServerLoginView and closes the startView
     * @throws Exception
     */
    @FXML
    public void handleButton() throws Exception {
        ServerLogin login = new ServerLogin();
        Stage loginStage = new Stage();

        loginStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        try {
            login.display(loginStage);
            closeStage();
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }

    }

}
