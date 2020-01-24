package de.upb.codingpirates.battleships.desktop.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class for representing one player of a game
 */
public class Player {

    /**
     * the player's rang
     */
    private final SimpleIntegerProperty rang;
    /**
     * the player's name
     */
    private final SimpleStringProperty name;
    /**
     * the player's points
     */
    private final SimpleIntegerProperty points;

    /**
     * constructor of the player
     * @param rang rang in the game
     * @param name username of the player
     * @param points points of the player in this game
     */
    public Player(Integer rang, String name, Integer points) {
        super();
        this.rang = new SimpleIntegerProperty(rang);
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
    }

    /**
     * Gets the rang of the player
     * @return rang
     */
    public Integer getRang() {
        return rang.get();
    }

    /**
     * Gets the username
     * @return name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Gets the points of a player
     * @return points
     */
    public Integer getPoints() {
        return points.get();
    }
}
