package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.network.message.notification.*;

public interface INotificationCallback {

  public void onGameInitNotification(GameInitNotification initNotification);

  public void onGameStartNotification(GameStartNotification startNotification);

  public void onSpectatorUpdateNotification(SpectatorUpdateNotification updateNotification);

  public void onFinishNotification(FinishNotification finishNotification);

  public void onPauseNotification(PauseNotification pauseNotification);

  public void onContinueNotification(ContinueNotification continueNotification);

  public void onLeaveNotification(LeaveNotification leaveNotification);

  public void onRoundStartNotification(RoundStartNotification roundstartNotification);
}
