package org.nterekhin.game.client;

import org.nterekhin.game.config.ServerConfigProperties;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.event.MessagesLimitEvent;
import org.nterekhin.game.eventBus.event.PlayerChooseNicknameEvent;
import org.nterekhin.game.eventBus.event.PlayerDisconnectedEvent;
import org.nterekhin.game.util.IOFunction;

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

    private final static PlayerManager playerManager = PlayerManager.getInstance();
    private static final String MESSAGE_FORMAT = "%s: %s, Messages left: %d";

    // Flag for execution
    private volatile boolean running = true;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final PlayerState playerState;

    public PlayerHandler(Socket socket, PlayerState playerState) throws IOException {
        this.socket = socket;
        this.playerState = playerState;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // For tests
    protected PlayerHandler(Socket socket, BufferedReader in, PrintWriter out, PlayerState playerState) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.playerState = playerState;
    }

    @Override
    public void run() {
        try {
            EventBus.getInstance().postEvent(new PlayerChooseNicknameEvent(
                    playerState.getNickname(),
                    socket.getRemoteSocketAddress().toString())
            );
            String message;
            while (running && !socket.isClosed() && (message = in.readLine()) != null) {
                // If player want to quit before message limit is exceeded
                if (message.equals("quit")) {
                    disconnect();
                } else {
                    playerManager.broadcast(playerState.getNickname(), message);
                }
            }
        } catch (SocketException e) {
            // For cleaner logs on server shutdown
            close();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     * Here we will collect Player nickname
     */
    public void init() throws IOException {
        out.println("Please choose nickname");
        String nickname = in.readLine();
        while (!playerManager.verifyNickname(nickname)) {
            out.println("Empty or already existed nicknames are not acceptable");
            nickname = in.readLine();
        }
        playerState.setNickname(nickname);
    }

    // For own messages
    public void acceptMessage(String message) {
        out.println(String.format(MESSAGE_FORMAT,
                playerState.getNickname(),
                message,
                ServerConfigProperties.getInstance().getMessageLimit() - playerState.incrementMessageCounter()));
        checkStopCondition(playerState.getMessageCounter());
    }

    public void acceptServerMessage(String message) {
        out.println(message);
    }

    public void acceptOtherPlayerMessage(String nickname, String message) {
        out.println(nickname + ": " + message);
    }

    private void checkStopCondition(int messageCounter) {
        if (messageCounter >= ServerConfigProperties.getInstance().getMessageLimit()) {
            EventBus.getInstance().postEvent(new MessagesLimitEvent());
        }
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

