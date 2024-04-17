package org.nterekhin.gameP2P.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ServerConfigProperties {

    private static final ServerConfigProperties instance = new ServerConfigProperties();
    private final boolean createServer;
    private final int createPlayers;

    private ServerConfigProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = ServerConfigProperties.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("Config file not found in resources directory");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Access properties
        this.createServer = Boolean.parseBoolean(properties.getProperty("createServer"));
        this.createPlayers = Integer.parseInt(properties.getProperty("createPlayers"));

    }

    public boolean getCreateServer() {
        return createServer;
    }

    public int getCreatePlayers() {
        return createPlayers;
    }

    public static ServerConfigProperties getInstance() {
        return instance;
    }
}
