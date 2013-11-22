package fr.batimen.web.selenium;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Classe abstraite permettant de mettre en place les tests d'integration avec
 * selenium
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class AbstractSeleniumTest {

	protected WebDriver driver;
	protected String appUrl;
	protected boolean acceptNextAlert = true;
	protected StringBuilder verificationErrors = new StringBuilder();
	private String ipServeur;
	private String portServeur;
	private String nomWs;

	private static final Logger logger = LoggerFactory.getLogger(AbstractSeleniumTest.class);

	@Before
	public void setUp() throws Exception {

		Properties wsProperties = new Properties();
		try {
			wsProperties.load(getClass().getClassLoader().getResourceAsStream("selenium.properties"));
			ipServeur = wsProperties.getProperty("app.ip");
			portServeur = wsProperties.getProperty("app.port");
			nomWs = wsProperties.getProperty("app.name");
		} catch (IOException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Erreur de récupération des properties de l'application web : " + e.getMessage());
			}
		}

		StringBuilder sbUrlApp = new StringBuilder("http://");
		sbUrlApp.append(ipServeur);
		sbUrlApp.append(":");
		sbUrlApp.append(portServeur);
		sbUrlApp.append("/");
		sbUrlApp.append(nomWs);

		driver = new FirefoxDriver();
		appUrl = sbUrlApp.toString();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	protected String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

}
