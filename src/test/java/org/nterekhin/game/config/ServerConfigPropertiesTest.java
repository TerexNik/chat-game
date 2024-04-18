package org.nterekhin.game.config;

import org.junit.Assert;
import org.junit.Test;

public class ServerConfigPropertiesTest {
    @Test
    public void shouldLoadConfig() {
        ServerConfigProperties config = ServerConfigProperties.getInstance();
        Assert.assertNotNull(config);
    }
}