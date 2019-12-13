package de.upb.codingpirates.battleships.desktop;

import de.upb.codingpirates.battleships.network.exceptions.parser.ParserException;
import de.upb.codingpirates.battleships.network.message.Message;
import de.upb.codingpirates.battleships.network.message.notification.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Builds a Connection to the Server.
 */
public class TCPClient {

  private Socket clientSocket = null;
  private BufferedReader inFromServer;
  private DataOutputStream outToServer;
  private boolean online = true;
  private String lastMessage = null;
  private int timeoutInSec = 5;
  private InGameModel notifier = null;

  /**
   * Constructor. Creates a new ServerThread, Socket, BufferedReader and runs start().
   * @param targetIP	ServerIp
   * @param port		ServerPort
   * @throws Exception
   */
  public TCPClient(String targetIP, int port) throws Exception {
    clientSocket = new Socket(targetIP, port);
    inFromServer = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
    outToServer = new DataOutputStream(clientSocket.getOutputStream());

    ServerThread st = new ServerThread();
    st.start();

    timeoutInSec *= 10;
  }

  /**
   * Tests of the typed in Ip is valid.
   * @param ip String ServerIp
   * @return
   */
  public static boolean validIP(String ip) {
    try {
      if (ip == null || ip.isEmpty()) {
        return false;
      }

      String[] parts = ip.split("\\.");
      if (parts.length != 4) {
        return false;
      }

      for (String s : parts) {
        int i = Integer.parseInt(s);
        if ((i < 0) || (i > 255)) {
          return false;
        }
      }
      if (ip.endsWith(".")) {
        return false;
      }

      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }

  /**
   * Sends a Request to the Server. Returns the Response of the Server.
   * @param urlParameters String
   * @return	result Response of the Server.
   * @throws Exception
   */
  public String sendeAnfrage(String urlParameters) throws Exception {

    String serverText = urlParameters + "\r\n";
    lastMessage = null;
    if (clientSocket.isConnected()) {
      outToServer.write(serverText.getBytes(StandardCharsets.UTF_8));
      outToServer.flush();
      System.out.println("Send to server: " + serverText);
      String result = null;

      int i = 0;
      while (lastMessage == null && timeoutInSec > i) {

        try {
          Thread.sleep(100);
        } catch (Exception ex) {
          System.out.println("Fehler beim warten: " + ex);
        }
        i++;
      }

      // parse to erg
      if (lastMessage != null) {
        result = lastMessage;
        lastMessage = null;
      }

      return result;
    }

    return "";
  }

  /**
   * Closes the Connection to the Server.
   * @throws Exception
   */
  public void closeConnection() throws Exception {
    online = false;
    clientSocket.close();
  }

  /**
   * Set Method for Notifier.
   * @param callback InGameModel
   */
  public void setNotifier(InGameModel callback) {
    this.notifier = callback;
  }


  /**
   * Creates a new Thread that waits for Notifications by the Server.
   */
  private class ServerThread extends Thread {

	/**
	 * Waits for Notifications by the Server and reacts on them.
	 */
    @Override
    public void run() {
      while (online) {
        try {
          lastMessage = inFromServer.readLine();
          System.out.println("Message from Server: " + lastMessage);

          Message m = ServerLoginModel.MESSAGE_PARSER.deserialize(lastMessage);

          switch (m.messageId) {
            case 366:
              notifier.onGameInitNotification((GameInitNotification) m);
              break;
            case 363:
              notifier.onGameStartNotification((GameStartNotification) m);
              break;
            case 368:
              notifier.onSpectatorUpdateNotification((SpectatorUpdateNotification) m);
              break;
            case 365:
              notifier.onRoundStartNotification((RoundStartNotification) m);
              break;
            case 364:
              notifier.onFinishNotification((FinishNotification) m);
              break;
            case 361:
              notifier.onPauseNotification((PauseNotification) m);
              break;
            case 362:
              notifier.onContinueNotification((ContinueNotification) m);
              break;
            case 367:
              notifier.onLeaveNotification((LeaveNotification) m);
              break;

          }
        } catch (IOException | ParserException ex) {
          System.out.println("Fehler beim Lesen: " + ex);
        }
      }

    }
  }
  // TODO Auto-generated method stub
}
