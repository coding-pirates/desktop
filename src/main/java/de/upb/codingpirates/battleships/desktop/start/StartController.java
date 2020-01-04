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
    @FXML
    private Button btn_start;


    private BattleshipsDesktopClientApplication main;

    /**
     * Initial Method.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setMain(BattleshipsDesktopClientApplication main) {

        this.main = main;
    }


    public void closeStage(){
        Stage stage = (Stage) btn_start.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleButton() throws Exception {
        ServerLogin login = new ServerLogin();
        Stage loginStage = new Stage();
        try {
            login.start(loginStage);
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
