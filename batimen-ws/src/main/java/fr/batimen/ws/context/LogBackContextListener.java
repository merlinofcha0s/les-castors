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

public class LogBackContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(LogBackContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
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
		context.putProperty("application-name", "batimen");
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("logback.xml");
			if (is == null) {
				if (logger.isErrorEnabled()) {
					logger.error("Logback xml file finded");
				}
			} else {
				jc.doConfigure(is);
			}
		} catch (JoranException ex) {
			if (logger.isErrorEnabled()) {
				logger.error("Logback contextInitialized error");
			}
			StatusPrinter.print(context);
			ex.printStackTrace();
		}
	}
}
