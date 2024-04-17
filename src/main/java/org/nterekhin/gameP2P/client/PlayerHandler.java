package org.nterekhin.gameP2P.client;

import org.nterekhin.gameP2P.eventBus.EventBus;
import org.nterekhin.gameP2P.eventBus.event.PlayerConnectedEvent;
import org.nterekhin.gameP2P.eventBus.event.PlayerDisconnectedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class helps with logic needed for players communications
 */
public class PlayerHandler extends Thread {

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

    public void run() {
        try {
            init();
            String message;
            while (running && !socket.isClosed() && (message = in.readLine()) != null) {
                if (message.equals("quit")) {
                    disconnect();
                } else {
                    PlayerManager.getInstance().broadcast(
                            playerState.getNickname(),
                            ": " + message);
                    playerState.incrementMessageCounter();
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

    private void init() throws IOException {
        out.println("Please choose nickname");
        playerState.setNickname(in.readLine());
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

    public void close() {
        try {
            running = false;
            if (!socket.isClosed()) {
                socket.close();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public Socket getSocket() {
        return socket;
    }
}

