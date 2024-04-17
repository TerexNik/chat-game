package org.nterekhin.gameP2P.client;

import org.nterekhin.gameP2P.config.ServerConfigProperties;
import org.nterekhin.gameP2P.eventBus.EventBus;
import org.nterekhin.gameP2P.eventBus.event.MessagesLimitEvent;
import org.nterekhin.gameP2P.eventBus.event.PlayerConnectedEvent;
import org.nterekhin.gameP2P.eventBus.event.PlayerDisconnectedEvent;
import org.nterekhin.gameP2P.util.IOFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class helps with logic needed for players communications
 */
public class PlayerHandler implements Runnable {

    // Flag for execution
    private volatile boolean running = true;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final PlayerState playerState;

    public PlayerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.playerState = new PlayerState();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            init();
            String message;
            while (running && !socket.isClosed() && (message = in.readLine()) != null) {
                // If player want to quit before message limit is exceeded
                if (message.equals("quit")) {
                    disconnect();
                } else {
                    PlayerManager.getInstance().broadcast(playerState.getNickname(), ": " + message);
                    if (PlayerManager.getInstance().isEnoughPlayersOnline()) {
                        PlayerHandler.checkStopCondition(playerState.incrementMessageCounter());
                    } else {
                        out.println("Please wait until at least 2 players will be online");
                    }
                }
            }
        } catch (SocketException e) {
            // this catch helps keep logs clean from players that not chosen their nicknames yet
            close();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private static void checkStopCondition(int messageCounter) {
        if (messageCounter >= ServerConfigProperties.getInstance().getMessageLimit() + 1) {
            EventBus.getInstance().postEvent(new MessagesLimitEvent());
        }
    }

    /**
     * Here we will collect Player nickname
     */
    private void init() throws IOException {
        out.println("Please choose nickname");
        String nickname = in.readLine();
        while (nickname == null || nickname.isEmpty()) {
            out.println("Empty nickname won't work, please try again");
            nickname = in.readLine();
        }
        playerState.setNickname(nickname);
        EventBus.getInstance().postEvent(new PlayerConnectedEvent(
                playerState.getNickname(),
                socket.getRemoteSocketAddress().toString())
        );
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Will close connection with server wide announcement
     */
    public void disconnect() {
        EventBus.getInstance().postEvent(new PlayerDisconnectedEvent(
                playerState.getNickname(),
                socket.getRemoteSocketAddress().toString())
        );
    }

    /**
     * Gracefully close PlayerHandler
     */
    public void close() {
        IOFunction.executeWithLogOnIOException(() -> {
            running = false;
            if (!socket.isClosed()) {
                socket.close();
            }
            in.close();
            out.close();
        });
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public Socket getSocket() {
        return socket;
    }
}

