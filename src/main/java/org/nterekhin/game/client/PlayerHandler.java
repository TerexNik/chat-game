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
                    PlayerManager.getInstance().broadcast(playerState.getNickname(), ": " + message);
                    if (PlayerManager.getInstance().isEnoughPlayersOnline()) {
                        checkStopCondition(playerState.incrementMessageCounter());
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

    private void checkStopCondition(int messageCounter) {
        if (messageCounter >= ServerConfigProperties.getInstance().getMessageLimit() + 1) {
            EventBus.getInstance().postEvent(new MessagesLimitEvent());
        }
    }

    /**
     * Here we will collect Player nickname
     */
    public void init() throws IOException {
        out.println("Please choose nickname");
        String nickname = in.readLine();
        while (!PlayerManager.getInstance().verifyNickname(nickname)) {
            out.println("Empty or already existed nicknames are not acceptable");
            nickname = in.readLine();
        }
        playerState.setNickname(nickname);
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

