package de.upb.codingpirates.battleships.desktop.util;

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

public class Help {

    @FXML
    private Button closeButton;

    @FXML
    private Label lb_message;


    public void display(String title, String message) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        //lb_message.setText(message);


        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/HelpView.fxml"));
        AnchorPane pane = loader.load();


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

    private String helpingText(String title){
        //TODO
        return null;
    }


}
