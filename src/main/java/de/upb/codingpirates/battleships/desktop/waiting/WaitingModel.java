package de.upb.codingpirates.battleships.desktop.waiting;

import de.upb.codingpirates.battleships.desktop.BattleshipsDesktopClientApplication;
import de.upb.codingpirates.battleships.logic.Game;
import de.upb.codingpirates.battleships.network.message.request.RequestBuilder;

public class WaitingModel {
    private Game currentGame;
    private int clientID;


    public WaitingModel (Game currentGame, int clientID){
        this.clientID = clientID;
        this.currentGame = currentGame;

    }

    public void sendLeaveRequest(WaitingController sender){
            BattleshipsDesktopClientApplication.getInstance().getTcpConnector().sendMessageToServer(RequestBuilder.gameLeaveRequest());


    }


}
