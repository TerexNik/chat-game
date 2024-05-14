package org.nterekhin.game.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Properties class that helps with configuration
 */
public final class ServerConfigProperties {

    private static ServerConfigProperties instance;
    private final int port;
    private final boolean createServer;
    private final int createPlayers;
    private final int messageLimit;
    private final boolean defaultNicknames;
    private final boolean sendHelloMessage;

    private ServerConfigProperties(Map<String, Object> parameters) {
        Properties properties = new Properties();
        loadProperties(properties);
        // Set up properties
        this.port = Integer.parseInt(getProperty("port", parameters, properties));
        this.createServer = Boolean.parseBoolean(getProperty("createServer", parameters, properties));
        this.defaultNicknames = Boolean.parseBoolean(getProperty("defaultNicknames", parameters, properties));
        this.sendHelloMessage = Boolean.parseBoolean(getProperty("sendHelloMessage", parameters, properties));
        this.createPlayers = Integer.parseInt(getProperty("createPlayers", parameters, properties));
        String messageLimit = getProperty("messageLimit", parameters, properties);
        if (messageLimit == null) {
            this.messageLimit = 10;
        } else {
            this.messageLimit = Integer.parseInt(messageLimit);
        }
    }

    public static ServerConfigProperties getInstance() {
        return instance;
    }

    public int getPort() {
        return port;
    }

    public boolean getCreateServer() {
        return createServer;
    }

    public int getCreatePlayers() {
        return createPlayers;
    }

    public int getMessageLimit() {
        return messageLimit;
    }

    public boolean isDefaultNicknames() {
        return defaultNicknames;
    }

    public boolean isSendHelloMessage() {
        return sendHelloMessage;
    }

    public static ServerConfigProperties setUpPropertiesByArgs(String[] args) {
        Map<String, Object> parameters = new HashMap<>();
        for (String arg : args) {
            String[] parts = arg.trim().split("=");
            parameters.put(parts[0], parts[1]);
        }
        instance = new ServerConfigProperties(parameters);
        return instance;
    }

    private String getProperty(String name, Map<String, Object> parameters, Properties properties) {
        return Optional.ofNullable(parameters.get(name))
                .map(Object::toString)
                .orElse(properties.getProperty(name));
    }

    private void loadProperties(Properties properties) {
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
    }
}
