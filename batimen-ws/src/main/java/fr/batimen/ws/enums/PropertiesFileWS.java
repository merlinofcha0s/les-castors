package fr.batimen.ws.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Enum qui permet de gérer / charger les fichiers properties
 * 
 * @author Casaucau Cyril
 * 
 */
public enum PropertiesFileWS {

    URL("url.properties"), EMAIL("email.properties"), CASTOR("castor.properties"), JOBS("jobs.properties"), IMAGE(
            "image.properties");

    private final transient Logger logger = LoggerFactory.getLogger(PropertiesFileWS.class);
    private Properties properties;

    PropertiesFileWS(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileWS.class.getClassLoader().getResourceAsStream(propertiesFileName));
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
