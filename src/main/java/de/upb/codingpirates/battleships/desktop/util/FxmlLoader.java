package de.upb.codingpirates.battleships.desktop.util;

import javafx.fxml.FXMLLoader;

import java.util.ResourceBundle;

public interface FxmlLoader {
    default FXMLLoader getLoader(String name){
        return new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml",name)), ResourceBundle.getBundle("lang/desktop", Settings.getLocale()));
    }
}
