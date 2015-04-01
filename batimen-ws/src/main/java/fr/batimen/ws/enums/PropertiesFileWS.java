package fr.batimen.ws.enums;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enum qui permet de gérer / charger les fichiers properties
 * 
 * @author Casaucau Cyril
 * 
 */
public enum PropertiesFileWS {

    URL("url.properties"), EMAIL("email.properties"), CASTOR("castor.properties"), JOBS("jobs.properties"), IMAGE(
            "image.properties");

    private final Logger logger = LoggerFactory.getLogger(PropertiesFileWS.class);

    private PropertiesFileWS(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileWS.class.getClassLoader().getResourceAsStream(propertiesFileName));
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
