package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import fr.batimen.core.utils.PropertiesUtils;

/**
 * 
 * Classe abstraite permettant de mettre en place les tests d'integration avec
 * selenium
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class AbstractITTest {

    protected WebDriver driver;
    protected String appUrl;
    protected boolean acceptNextAlert = true;
    protected StringBuilder verificationErrors = new StringBuilder();
    private String ipServeur;
    private String portServeur;
    private String nomApp;
    private String dataSourceAddress;
    private String loginDB;
    private String passwordDB;
    private String browser;
    private String chromeDriverAddress;

    public final static String BON_MOT_DE_PASSE = "lollollol";
    public final static String MAUVAIS_MOT_DE_PASSE = "kikoulolmauvais";
    public final static int TEMPS_ATTENTE_AJAX = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractITTest.class);

    // DBSetup
    public static final Operation DELETE_ALL = deleteAllFrom("notification", "annonce_artisan", "annonce",
            "permission", "notation", "artisan", "categoriemetier", "entreprise", "adresse", "client");
    public static final Operation INSERT_USER_DATA = insertInto("client")
            .columns("id", "email", "nom", "prenom", "login", "password", "numeroTel", "dateInscription", "isActive",
                    "cleactivation")
            .values(100001, "raiden@batimen.fr", "Casaucau", "Cyril", "raiden",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", true, "lolmdr")
            .values(100002, "xaviern@batimen.fr", "Dupont", "Xavier", "xavier",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", false, "lolmdr06")
            .values(100003, "admin@lescastors.fr", "Casaucau", "Cyril", "admin",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", true, "lolmdr101").build();
    public static final Operation INSERT_USER_PERMISSION = insertInto("permission").columns("typecompte", "client_fk")
            .values(4, 100001).values(4, 100002).values(0, 100003).build();

    @Before
    public void setUpITTest() throws Exception {
        setUpDB();
        setUpSelenium();
    }

    public abstract void prepareDB() throws Exception;

    private void setUpDB() throws Exception {

        Properties dbProperties = PropertiesUtils.loadPropertiesFile("dbsetup.properties");
        dataSourceAddress = dbProperties.getProperty("datasource.url");
        loginDB = dbProperties.getProperty("database.login");
        passwordDB = dbProperties.getProperty("database.password");

        prepareDB();
    }

    private void setUpSelenium() {

        Properties wsProperties = PropertiesUtils.loadPropertiesFile("selenium.properties");
        ipServeur = wsProperties.getProperty("app.ip");
        portServeur = wsProperties.getProperty("app.port");
        nomApp = wsProperties.getProperty("app.name");
        browser = wsProperties.getProperty("app.browser.target");
        chromeDriverAddress = wsProperties.getProperty("app.address.chrome.driver");

        StringBuilder sbUrlApp = new StringBuilder("https://");
        sbUrlApp.append(ipServeur);
        sbUrlApp.append(":");
        sbUrlApp.append(portServeur);
        sbUrlApp.append("/");
        sbUrlApp.append(nomApp);

        switch (browser) {
        case "chrome":
            System.setProperty("webdriver.chrome.driver", chromeDriverAddress);
            driver = new ChromeDriver();
            break;
        case "firefox":
            driver = new FirefoxDriver();
            break;
        default:
            driver = new FirefoxDriver();
            break;
        }

        appUrl = sbUrlApp.toString();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

    }

    @After
    public void tearDownSelenium() throws Exception {
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

    protected void connexionApplication(String username, String password, Boolean isVerifLinkMonCompte) {
        driver.findElement(By.id("connexionlbl")).click();
        Boolean checkCondition = (new WebDriverWait(driver, 5)).until(ExpectedConditions
                .textToBePresentInElementLocated(By.id("myModalLabel"), "Connexion à l'espace client / artisan"));
        assertTrue(checkCondition);
        driver.findElement(By.id("loginModal")).click();
        driver.findElement(By.id("loginModal")).clear();
        driver.findElement(By.id("loginModal")).sendKeys(username);
        // On le fait attendre car il y a une probabilité qu'il ecrive trop vite
        // dans les champs et qu'il se trompe
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Fail to wait authentication", e);
            }
        }
        driver.findElement(By.id("passwordModal")).click();
        driver.findElement(By.id("passwordModal")).clear();
        driver.findElement(By.id("passwordModal")).sendKeys(password);
        driver.findElement(By.id("signInButton")).click();

        if (isVerifLinkMonCompte) {
            Boolean checkConditionMonCompteLabel = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                    .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "MON COMPTE"));
            assertTrue(checkConditionMonCompteLabel);
        }
    }

    protected DriverManagerDestination getDriverManagerDestination() {
        return new DriverManagerDestination(dataSourceAddress, loginDB, passwordDB);
    }

}
