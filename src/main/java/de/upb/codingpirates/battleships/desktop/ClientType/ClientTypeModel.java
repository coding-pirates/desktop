package de.upb.codingpirates.battleships.desktop.ClientType;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;

public class ClientTypeModel {
    private Game selectedGame;
    public void sendGameJoinSpectatorRequest(){
        try {
            BattleshipsDesktopClientApplication
                    .getInstance()
                    .getTcpConnector()
                    .sendMessageToServer(RequestBuilder.gameJoinSpectatorRequest(selectedGame.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSelectedGame(Game selectedGame){
        this.selectedGame = selectedGame;
    }
    public Game getSelectedGame(){
        return selectedGame;
    }
}
