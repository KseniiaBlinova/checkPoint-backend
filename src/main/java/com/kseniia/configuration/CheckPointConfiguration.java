package com.kseniia.configuration;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class CheckPointConfiguration {

    private static final Logger log = LoggerFactory.getLogger("checkPointConfiguration");

    private final int maxRoomIdValue;
    private final int minRoomIdValue;
    private final int minKeyIdValue;
    private final int maxKeyIdValue;

    private CheckPointConfiguration() {
        Properties property = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("validation.properties")) {
            property.load(inputStream);
            minRoomIdValue = Integer.parseInt(property.getProperty("room.min"));
            maxRoomIdValue = Integer.parseInt(property.getProperty("room.max"));
            minKeyIdValue = Integer.parseInt(property.getProperty("key.min"));
            maxKeyIdValue = Integer.parseInt(property.getProperty("key.max"));
        } catch (IOException e) {
            log.error("Unable to read configuration", e);
            throw new RuntimeException("Unable to read configuration");
        }
    }

    public static class Holder {
        public static final CheckPointConfiguration INSTANCE = new CheckPointConfiguration();
    }

}

