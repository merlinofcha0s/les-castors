package fr.batimen.web.enums;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PropertiesFileWeb {

    APP("app.properties");

    private final Logger logger = LoggerFactory.getLogger(PropertiesFileWeb.class);

    private PropertiesFileWeb(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileWeb.class.getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Erreur de récupération des properties dans le fichier " + propertiesFileName, e);
            }
        }
    }

    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}
