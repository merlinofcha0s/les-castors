package fr.batimen.ws.context;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Force l'application a trouver le fichier logback.xml dans le classpath et à
 * relancer la cfg de logback suivant ce dernier.
 * 
 * @author Casaucau Cyril
 * 
 */
public class LogBackContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBackContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // Initialisation des logs (on force logback a trouvé le fichier de
        // config
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        context.reset();
        // override default configuration
        // inject the name of the current application as "application-name"
        // property of the LoggerContext
        context.putProperty("application-name", "batimen-ws");
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("logback.xml");
            if (is == null) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Logback xml file finded");
                }
            } else {
                jc.doConfigure(is);
            }
        } catch (JoranException ex) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Logback contextInitialized error", ex);
            }
            StatusPrinter.print(context);
        }
    }
}
