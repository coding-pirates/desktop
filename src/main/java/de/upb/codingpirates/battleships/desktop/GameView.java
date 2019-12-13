package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.logic.Game;

/**
 * Class that represents a Game in the Lobby Game Overview.
 */
public class GameView {

    private Game content = null;

    /**
     * Constructor. Sets the Content.
     *
     * @param g Content
     */
    public GameView(Game g) {
        this.content = g;
    }

    /**
     * Get Method for Content.
     *
     * @return Content
     */
    public Game getContent() {
        return content;
    }

    /**
     * Formats the Content into a String to be shown in the Game Overview.
     *
     * @return String Content
     */
    @Override
    public String toString() {
        //String formatieren einsetzen, je nachdem wie wir das darstellen wollen
        return content.getId() + ", Spielname: " + content.getName() + ", Punkte/Treffer: " + content.getConfig().getHitPoints() + ", Punkte/Schiff versenkt: " + content.getConfig().getSunkPoints() + ", Bedenkzeit: " + content.getConfig().getRoundTime() + " sek, Anzahl Schüsse: " + content.getConfig().getShotCount();
    }

}
