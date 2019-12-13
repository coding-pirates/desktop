package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.network.message.notification.*;

public interface INotificationCallback {

    void onGameInitNotification(GameInitNotification initNotification);

    void onGameStartNotification(GameStartNotification startNotification);

    void onSpectatorUpdateNotification(SpectatorUpdateNotification updateNotification);

    void onFinishNotification(FinishNotification finishNotification);

    void onPauseNotification(PauseNotification pauseNotification);

    void onContinueNotification(ContinueNotification continueNotification);

    void onLeaveNotification(LeaveNotification leaveNotification);

    void onRoundStartNotification(RoundStartNotification roundstartNotification);
}
