package fr.castor.web.selenium.common;

import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.utils.PropertiesUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.junit.Assert.*;

/**
 * Classe abstraite permettant de mettre en place les tests d'integration avec
 * selenium
 *
 * @author Casaucau Cyril
 */
public abstract class AbstractITTest {

    public final static String BON_MOT_DE_PASSE = "lollollol";
    public final static String MAUVAIS_MOT_DE_PASSE = "kikoulolmauvais";
    public final static int TEMPS_ATTENTE_AJAX = 20;
    // DBSetup
    public static final Operation DELETE_ALL = deleteAllFrom("notification", "annonce_artisan", "categoriemetier", "motcle", "annonce",
            "permission", "avis", "artisan", "entreprise", "adresse", "client");
    public static final Operation INSERT_USER_DATA = insertInto("client")
            .columns("id", "email", "nom", "prenom", "login", "password", "numeroTel", "dateInscription", "isActive",
                    "cleactivation")
            .values(100001, "raiden0610@hotmail.fr", "Casaucau", "Cyril", "raiden",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", true, "lolmdr")
            .values(100002, "xaviern@batimen.fr", "Dupont", "Xavier", "xavier",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", null,
                    "2014-01-08", true, "lolmdr06")
            .values(100003, "admin@lescastors.fr", "Casaucau", "Cyril", "admin",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", true, "lolmdr101")
            .values(100004, "xd@lescastors.fr", "lolxd", "ptdr", "xdlol",
                    "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=", "0614125696",
                    "2014-01-08", false, "lolmdr201").build();
    public static final Operation INSERT_USER_PERMISSION = insertInto("permission").columns("typecompte", "client_fk")
            .values(4, 100001).values(4, 100002).values(0, 100003).values(4, 100004).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractITTest.class);
    protected WebDriver driver;
    protected String appUrl;
    protected boolean acceptNextAlert = true;
    protected StringBuilder verificationErrors = new StringBuilder();
    protected String browser;
    private String ipServeur;
    private String portServeur;
    private String nomApp;
    private String dataSourceAddress;
    private String loginDB;
    private String passwordDB;
    private String chromeDriverAddress;
    private String ieDriverAddress;
    private String edgeDriverAddress;

    @Before
    public void setUpITTest() throws Exception {
        setUpDB();
        setUpSelenium();

        if (browser.equals("ie")) {
            driver.get(appUrl);
            driver.findElement(By.id("overridelink")).click();
        } else if (browser.equals("edge")) {
            driver.get(appUrl);
            driver.findElement(By.id("invalidcert_continue")).click();
        }
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
        ieDriverAddress = wsProperties.getProperty("app.address.ie.driver");
        edgeDriverAddress = wsProperties.getProperty("app.address.edge.driver");

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
                driver.manage().window().setSize(new Dimension(1440,960));
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "ie":
                System.setProperty("webdriver.ie.driver", ieDriverAddress);
                driver = new InternetExplorerDriver();
            case "edge":
                System.setProperty("webdriver.edge.driver", edgeDriverAddress);
                driver = new EdgeDriver();
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

    protected void connexionApplication(String username, String password, Boolean isVerifLinkMonCompte) {
        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.elementToBeClickable((By.id("connexionlbl"))));
        assertNotNull(checkConditionAnnoncePresent);
        findElement(By.id("connexionlbl")).click();
        WebElement checkCondition = (new WebDriverWait(driver, TEMPS_ATTENTE_AJAX)).until(ExpectedConditions
                .visibilityOfElementLocated(By.id("myModalLabel")));
        assertNotNull(checkCondition);

        findElement(By.id("loginModal")).click();
        findElement(By.id("loginModal")).clear();
        findElement(By.id("loginModal")).sendKeys(username);

        findElement(By.id("passwordModal")).click();
        findElement(By.id("passwordModal")).clear();
        findElement(By.id("passwordModal")).sendKeys(password);
        findElement(By.id("signInButton")).click();

        if (isVerifLinkMonCompte) {
            Boolean checkConditionMonCompteLabel = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                    .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "MON COMPTE"));
            assertTrue(checkConditionMonCompteLabel);
        }
    }

    protected DriverManagerDestination getDriverManagerDestination() {
        return new DriverManagerDestination(dataSourceAddress, loginDB, passwordDB);
    }

    public WebElement findElement(By by) {
        waitForReady();
        WebDriverWait webDriverWait = new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX);
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement findElementAndWaitPresence(By by) {
        waitForReady();
        WebDriverWait webDriverWait = new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX);
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private void waitForReady() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX);
        webDriverWait.until((ExpectedCondition<Boolean>) input -> isAjaxFinished());
    }

    private Boolean isAjaxFinished() {
        Boolean isJqueryUsed = (Boolean) ((JavascriptExecutor) driver).executeScript("return (typeof(jQuery) != 'undefined')");
        if (isJqueryUsed) {
            while (true) {
                // JavaScript test to verify jQuery is active or not
                Boolean ajaxIsComplete = (Boolean) (((JavascriptExecutor) driver).executeScript("return jQuery.active == 0"));
                Boolean dataLoadIsComplete = (Boolean) (((JavascriptExecutor) driver).executeScript("return $('#waiterModal').is(':visible') == false"));
                if (ajaxIsComplete && dataLoadIsComplete) {
                    return true;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
        return false;
    }

    protected void etape1() {
        findElement(By.id("codePostal")).clear();
        findElement(By.id("codePostal")).sendKeys("06700");
        findElement(By.id("valideCodePostal")).click();
    }

    protected void testAjoutPhotoIT(WebDriver driver, boolean isAnnonceModification) throws InterruptedException {
        if(isAnnonceModification){
            findElement(By.id("btn-modif")).click();
        } else {
            findElement(By.linkText("Modifier mes informations")).click();
        }

        Properties seleniumProperties = PropertiesUtils.loadPropertiesFile("selenium.properties");
        StringBuilder adresseToImg = new StringBuilder(seleniumProperties.getProperty("app.temp.img.dir.test"));
        adresseToImg.append("castor.jpg");
        File file = new File(adresseToImg.toString());

        WebElement photoField = driver.findElement(By.id("photoField"));
        photoField.sendKeys(file.getAbsolutePath());

        findElement(By.id("envoyerPhotos")).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Photo(s) rajoutée(s) avec succés"));
        Assert.assertTrue(checkUntilModifOK);
    }

    protected void suppressionPhotoIT(WebDriver driver, boolean isModificationEntreprise) throws InterruptedException {

        String suppressionModificationEntreprise = "";

        //Clique sur le bouton supprimer la photo
        if(isModificationEntreprise){
            suppressionModificationEntreprise = "/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[3]/div[2]/div[2]/div[1]/div/div/div[2]/div[1]/div/div/a/div";
        }else{
            suppressionModificationEntreprise = "/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[3]/div[2]/div/div[1]/div/div/div[2]/div[1]/div/div/a/div";
        }

        Thread.sleep(500);

        findElement(By.xpath(suppressionModificationEntreprise)).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Suppression effectuée !"));
        Assert.assertTrue(checkUntilModifOK);
    }

    /**
     * Positive pour descendre, négative pour monter
     *
     * @param position
     */
    protected void scrollTo(int position){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0 ," + position + ")", "");
    }

}
