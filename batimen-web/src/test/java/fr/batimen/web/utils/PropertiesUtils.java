package fr.batimen.web.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Properties urlProperties;

    private PropertiesUtils() {

    }

    public static Properties loadPropertiesFile(String name) {

        final Properties properties = new Properties();
        try {
            properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(name));
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur de récupération des properties dans le fichier " + name, e);
            }
        }

        return properties;

    }

}
