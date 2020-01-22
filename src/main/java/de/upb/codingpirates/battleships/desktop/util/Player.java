package de.upb.codingpirates.battleships.desktop.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {

    private final SimpleIntegerProperty rang;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty points;

    public Player(Integer rang, String name, Integer points) {
        super();
        this.rang = new SimpleIntegerProperty(rang);
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
    }

    public Integer getRang() {
        return rang.get();
    }

    public String getName() {
        return name.get();
    }

    public Integer getPoints() {
        return points.get();
    }
}
