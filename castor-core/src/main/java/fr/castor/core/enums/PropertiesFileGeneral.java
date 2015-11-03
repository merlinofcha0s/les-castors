package fr.castor.core.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public enum PropertiesFileGeneral {

    GENERAL("general.properties"), URL("url.properties");

    private transient final Logger logger = LoggerFactory.getLogger(PropertiesFileGeneral.class);
    private Properties properties;

    private PropertiesFileGeneral(String propertiesFileName) {

        this.properties = new Properties();
        try {
            properties.load(PropertiesFileGeneral.class.getClassLoader().getResourceAsStream(propertiesFileName));
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
