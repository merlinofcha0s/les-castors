package fr.batimen.ws.client.enums;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PropertiesFileWsClient {

    WS("ws.properties");

    private final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileWsClient.class);

    private PropertiesFileWsClient(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileWsClient.class.getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur de récupération des properties dans le fichier " + propertiesFileName, e);
            }
        }
    }

    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}
