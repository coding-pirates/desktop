package de.upb.codingpirates.battleships.desktop.util;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nonnull;
import java.io.IOException;

public class Fullscreen {

    public void display(@Nonnull final Stage loginStage) throws IOException {
        this.initDimensions(loginStage);
    }

    /**
     * this method sets the dimensions for {@param stage}. Currently sets width and height to display size. Also removes the windows task bar.
     * @param stage instance of the stage the dimensions shall be set
     */
    protected void initDimensions(Stage stage) {

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        //remove the windows taskbar
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setAlwaysOnTop(true);
        stage.getScene().getStylesheets().add("https://fonts.googleapis.com/css?family=Pirata+One");
        stage.show();
    }
}
