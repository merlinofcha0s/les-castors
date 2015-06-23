package fr.batimen.web.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public enum PropertiesFileWeb {

    APP("app.properties"), DEPARTEMENT_ALLOWED("departement_allowed.properties");

    private final Logger logger = LoggerFactory.getLogger(PropertiesFileWeb.class);

    PropertiesFileWeb(String propertiesFileName) {

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
