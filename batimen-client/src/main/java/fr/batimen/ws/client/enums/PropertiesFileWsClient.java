package fr.batimen.ws.client.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public enum PropertiesFileWsClient {

    WS("ws.properties");

    private final transient Logger logger = LoggerFactory.getLogger(PropertiesFileWsClient.class);
    private Properties properties;

    PropertiesFileWsClient(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileWsClient.class.getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Erreur de récupération des properties dans le fichier " + propertiesFileName, e);
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }
}