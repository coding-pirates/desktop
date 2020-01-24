package de.upb.codingpirates.battleships.desktop.util;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * class to show accessibility tools in every view
 */
public class Help {

    //views
    /**
     * button to close the window
     */
    @FXML
    private Button closeButton;

    /**
     * starts the help window
     * @param title
     * @throws IOException
     */
    public void display(String title) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);



        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/HelpView.fxml"));
        AnchorPane pane = loader.load();


        Image icon = new Image(String.valueOf(getClass().getResource("/images/app_icon.png")));
        window.getIcons().add(icon);

        Scene scene = new Scene(pane);
        window.setResizable(false);
        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.showAndWait();
    }


    /**
     * closes the helpView
     */
    @FXML
    public void closeStage(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


}
