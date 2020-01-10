package de.upb.codingpirates.battleships.desktop.util;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Impressum {
    @FXML
    private Button closeButton;
    @FXML
    private Text txt_title;
    @FXML
    private Label lb_message;

    public void display(String title) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        AnchorPane pane;
        if (title == "Impressum"){
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ImpressumView.fxml"));
            pane = loader.load();
        }
        else{
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/KodexView.fxml"));
            pane = loader.load();
        }

        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        window.getIcons().add(icon);
        Scene scene = new Scene(pane);
        window.setResizable(false);
        window.setScene(scene);
        window.showAndWait();
    }

    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    }
