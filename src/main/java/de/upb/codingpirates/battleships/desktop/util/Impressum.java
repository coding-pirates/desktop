package de.upb.codingpirates.battleships.desktop.util;

import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLogin;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * class to show the impressum and piratenkodex
 */
public class Impressum {
    //views
    /**
     * button to close the window
     */
    @FXML
    private Button closeButton;

    /**
     * used for displaying the {@link Impressum}
     * @param title String to which the title is set
     * @throws IOException
     */
    public void display(String title) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        AnchorPane pane;
        if (title.equals("Impressum")){
            FXMLLoader loader =  new FXMLLoader(ServerLogin.class.getResource("/fxml/ImpressumView.fxml"), ResourceBundle.getBundle("lang/desktop", Settings.getLocale()));
            pane = loader.load();
        }
        else{
            FXMLLoader loader =  new FXMLLoader(ServerLogin.class.getResource("/fxml/KodexView.fxml"), ResourceBundle.getBundle("lang/desktop", Settings.getLocale()));
            pane = loader.load();
        }

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        window.getIcons().add(icon);
        Scene scene = new Scene(pane);
        window.setResizable(false);
        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.showAndWait();
    }

    /**
     * closes the window
     */
    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    }
