package de.upb.codingpirates.battleships.desktop.network;

import de.upb.codingpirates.battleships.client.network.ClientConnector;
import de.upb.codingpirates.battleships.network.connectionmanager.ClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.IOException;

public class ClientConnectorDesktop extends ClientConnector {
    private static final Logger LOGGER = LogManager.getLogger(ClientConnector.class);

    @Inject
    public ClientConnectorDesktop(ClientConnectionManager clientConnector) {
        super(clientConnector);
    }

    @Override
    @Deprecated
    public void connect(String host, int port) {
        new Thread(() -> {
            synchronized (clientConnector){
                try {
                    this.clientConnector.create(host,port);
                } catch (IOException e) {
                    LOGGER.info("Could not connect to Server");
                }
            }
        }).start();
    }

    public void connect(String host, int port, Runnable onFail, Runnable onSuccess){
        new Thread(() -> {
            synchronized (clientConnector){
                try {
                    this.clientConnector.create(host,port);
                    if(onSuccess != null)
                        onSuccess.run();
                } catch (IOException | IllegalArgumentException e) {
                    if(onFail != null)
                        onFail.run();
                    LOGGER.info("Could not connect to Server");
                }
            }
        }).start();
    }
}
