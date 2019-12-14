package de.upb.codingpirates.battleships.desktop.network;

import de.upb.codingpirates.battleships.client.Handler;
import de.upb.codingpirates.battleships.desktop.ingame.InGameModel;
import de.upb.codingpirates.battleships.desktop.lobby.LobbyController;
import de.upb.codingpirates.battleships.desktop.serverlogin.ServerLoginController;
import de.upb.codingpirates.battleships.network.exceptions.BattleshipException;
import de.upb.codingpirates.battleships.network.message.notification.*;
import de.upb.codingpirates.battleships.network.message.report.ConnectionClosedReport;
import de.upb.codingpirates.battleships.network.message.response.*;

public class MessageHandler implements Handler {
    @Override
    public void handleGameInitNotification(GameInitNotification message, int clientId) {
        InGameModel.INSTANCE.onGameInitNotification(message);
    }

    @Override
    public void handleContinueNotification(ContinueNotification message, int clientId) {
        InGameModel.INSTANCE.onContinueNotification(message);
    }

    @Override
    public void handleConnectionClosedReport(ConnectionClosedReport message, int clientId) {

    }

    @Override
    public void handleErrorNotification(ErrorNotification message, int clientId) {

    }

    @Override
    public void handleFinishNotification(FinishNotification message, int clientId) {
        InGameModel.INSTANCE.onFinishNotification(message);
    }

    @Override
    public void handleGameJoinPlayer(GameJoinPlayerResponse message, int clientId) {

    }

    @Override
    public void handleGameJoinSpectator(GameJoinSpectatorResponse message, int clientId) {
        try {
            InGameModel.INSTANCE.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleGameStartNotification(GameStartNotification message, int clientId) {
        InGameModel.INSTANCE.onGameStartNotification(message);
    }

    @Override
    public void handleLeaveNotification(LeaveNotification message, int clientId) {
        InGameModel.INSTANCE.onLeaveNotification(message);
    }

    @Override
    public void handleLobbyResponse(LobbyResponse message, int clientId) {
        LobbyController.INSTANCE.parseToGameView(message.getGames());
    }

    @Override
    public void handlePauseNotification(PauseNotification message, int clientId) {
        InGameModel.INSTANCE.onPauseNotification(message);
    }

    @Override
    public void handlePlaceShipsResponse(PlaceShipsResponse message, int clientId) {

    }

    @Override
    public void handlePlayerUpdateNotification(PlayerUpdateNotification message, int clientId) {

    }

    @Override
    public void handleSpectatorUpdateNotification(SpectatorUpdateNotification message, int clientId) {
        InGameModel.INSTANCE.onSpectatorUpdateNotification(message);
    }

    @Override
    public void handlePointsResponse(PointsResponse message, int clientId) {
        InGameModel.INSTANCE.onPointsResponse(message);
    }

    @Override
    public void handleRemainingTimeResponse(RemainingTimeResponse message, int clientId) {
        InGameModel.INSTANCE.onRemainingTimeResponse(message);
    }

    @Override
    public void handleRoundStartNotification(RoundStartNotification message, int clientId) {
        InGameModel.INSTANCE.onRoundStartNotification(message);
    }

    @Override
    public void handleServerJoinResponse(ServerJoinResponse message, int clientId) {
        ServerLoginController.INSTANCE.onServerJoinResponse(message);

    }

    @Override
    public void handleShotsResponse(ShotsResponse message, int clientId) {

    }

    @Override
    public void handleSpectatorGameStateResponse(SpectatorGameStateResponse message, int clientId) {
        try {
            InGameModel.INSTANCE.setGameStateResult(message);
            InGameModel.INSTANCE.start2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePlayerGameStateResponse(PlayerGameStateResponse message, int clientId) {

    }

    @Override
    public void handleBattleshipException(BattleshipException exception, int clientId) {

    }
}
