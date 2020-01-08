package de.upb.codingpirates.battleships.desktop.ingame;

import de.upb.codingpirates.battleships.client.listener.*;

interface InGameModelMessageListener extends ContinueNotificationListener, FinishNotificationListener, GameStartNotificationListener, LeaveNotificationListener, PauseNotificationListener, SpectatorUpdateNotificationListener, PointsResponseListener, RemainingTimeResponseListener, RoundStartNotificationListener, SpectatorGameStateResponseListener {
}
