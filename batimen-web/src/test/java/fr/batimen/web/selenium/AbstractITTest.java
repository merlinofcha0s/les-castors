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

    public final static String BON_MOT_DE_PASSE = "lollollol";
    public final static String MAUVAIS_MOT_DE_PASSE = "kikoulolmauvais";
    public final static int TEMPS_ATTENTE_AJAX = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractITTest.class);

    // DBSetup
    public static final Operation DELETE_ALL = deleteAllFrom("annonce", "adresse", "client");
    public static final Operation INSERT_USER_DATA = insertInto("client")
            .columns("id", "email", "nom", "prenom", "login", "password", "numeroTel", "dateInscription", "isArtisan")
            .values(100001, "raiden@batimen.fr", "Casaucau", "Cyril", "raiden",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", false).build();

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

        StringBuilder sbUrlApp = new StringBuilder("https://");
        sbUrlApp.append(ipServeur);
        sbUrlApp.append(":");
        sbUrlApp.append(portServeur);
        sbUrlApp.append("/");
        sbUrlApp.append(nomApp);

        // System.setProperty("webdriver.chrome.driver",
        // "C:\\selenium\\chromedriver.exe");
        driver = new FirefoxDriver();
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

    protected void connexionApplication(String password) {
        driver.findElement(By.id("connexionlbl")).click();
        Boolean checkCondition = (new WebDriverWait(driver, 5)).until(ExpectedConditions
                .textToBePresentInElementLocated(By.id("ui-id-1"), "Connexion à l'espace client / artisan"));
        assertTrue(checkCondition);
        driver.findElement(By.name("login")).click();
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys("raiden");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("signInButton")).click();
    }

    protected DriverManagerDestination getDriverManagerDestination() {
        return new DriverManagerDestination(dataSourceAddress, loginDB, passwordDB);
    }

    protected void waitForTheElementById(String id) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= TEMPS_ATTENTE_AJAX)
                fail("timeout");
            try {
                if (isElementPresent(By.id(id)))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    protected void waitForTheElementByXPAth(String xPath) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= TEMPS_ATTENTE_AJAX)
                fail("timeout");
            try {
                if (isElementPresent(By.xpath(xPath)))
                    ;
                break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

}
