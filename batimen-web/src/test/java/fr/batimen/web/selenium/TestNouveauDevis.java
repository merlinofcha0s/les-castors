package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * Classe de test selenium pour la creation de nouveau devis par le client
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestNouveauDevis extends AbstractITTest {

    private final String nouveauDevisDepartementURL = "/nouveaudevis/";

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur rempli son devis en s'étant authentifié juste
     * avant. Le devis doit se créer correctement.
     * 
     * @throws InterruptedException
     * 
     */
    @Test
    public void testCreationNouveauDevisAuthenticatedSucceed() throws InterruptedException {
        driver.get(appUrl + nouveauDevisDepartementURL);

        // On passe à l'etape 1
        etape1();
        // On remplit l'étape 2
        etape2();
        // On passe l'etape 3
        etape3();
        // On s'authentifie à l'application
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE);

        // On vérifie que le label est correcte
        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

    }

    /**
     * Cas de test : L'utilisateur crée un nouveau devis alors qu'il n'est pas
     * inscrit (et donc pas authentifié). Le devis doit se créer correctement.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testCreationNouveauDevisNonAuthenticatedSucceed() throws InterruptedException {
        driver.get(appUrl + nouveauDevisDepartementURL);
        // On passe à l'etape 1
        etape1();
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3();
        // Etape 3
        driver.findElement(By.id("nom")).clear();
        driver.findElement(By.id("nom")).sendKeys("Selenium");
        driver.findElement(By.id("prenom")).clear();
        driver.findElement(By.id("prenom")).sendKeys("Test");
        driver.findElement(By.id("numeroTel")).clear();
        driver.findElement(By.id("numeroTel")).sendKeys("0614528796");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("test@selenium.fr");
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("selenium");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("mdrlollol");
        driver.findElement(By.id("confirmPassword")).clear();
        driver.findElement(By.id("confirmPassword")).sendKeys("mdrlollol");
        driver.findElement(By.name("cguConfirmation")).click();
        driver.findElement(By.id("validateInscription")).click();

        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h5"),
                        "Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer"));
        assertTrue(checkCondition);

        driver.get(appUrl + "/activation?key=lolmdr06");

        WebElement checkConditionActivationlabel1 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[1]")));
        assertNotNull(checkConditionActivationlabel1);

        WebElement checkConditionActivationlabel2 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[2]")));
        assertNotNull(checkConditionActivationlabel2);

    }

    /**
     * Cas de test : L'utilisateur crée un nouveau devis alors qu'il est inscrit
     * mais pas authentifié, il s'authentifie lors de l'étape 3. Le devis doit
     * se créer correctement.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testCreationNouveauDevisSubscribeNotAuthenticatedSucceed() throws InterruptedException {
        driver.get(appUrl + nouveauDevisDepartementURL);
        // On selectionne le bon département
        etape1();
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3();
        // On se connecte
        connexionApplication("raiden", BON_MOT_DE_PASSE);

        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

    }

    /**
     * Cas de test : L'utilisateur crée deux devis, la deuxieme, l'application
     * doit lui renvoyer un message d'erreur.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testCreationNouveauDevisDuplicate() throws InterruptedException {
        // Premiere saisie de l'annonce.
        driver.get(appUrl + nouveauDevisDepartementURL);
        // On selectionne le bon département
        etape1();
        // On remplit l'étape 2
        etape2();
        // On passe à l'étape 3
        etape3();
        connexionApplication("raiden", BON_MOT_DE_PASSE);

        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

        // Deuxieme saisie de l'annonce.
        driver.get(appUrl + nouveauDevisDepartementURL);
        etape1();
        etape2();
        etape3();

        assertEquals("Problème pendant l'enregistrement de l'annonce, veuillez nous excuser pour la gène occasionnée.",
                driver.findElement(By.cssSelector("h5")).getText());

    }

    private void etape3() {
        new Select(driver.findElement(By.id("sousCategorieSelect"))).selectByVisibleText("Installation électrique");
        driver.findElement(By.id("descriptionDevisField")).clear();
        driver.findElement(By.id("descriptionDevisField")).sendKeys("Refonte complete de l'electricite dans la maison");
        new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Email");
        new Select(driver.findElement(By.id("delaiInterventionField")))
                .selectByVisibleText("Le plus rapidement possible");
        driver.findElement(By.id("adresseField")).clear();
        driver.findElement(By.id("adresseField")).sendKeys("106 avenue du test selenium");
        driver.findElement(By.id("adresseComplementField")).clear();
        driver.findElement(By.id("adresseComplementField")).sendKeys("complement du test");
        driver.findElement(By.id("codePostalField")).clear();
        driver.findElement(By.id("codePostalField")).sendKeys("04200");
        driver.findElement(By.id("villeField")).clear();
        driver.findElement(By.id("villeField")).sendKeys("selenium city");
        driver.findElement(By.id("validateQualification")).click();
    }

    private void etape1() {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#mapFrance > svg")));
        } catch (Throwable error) {
            assertFalse("Timeout waiting for Page Load Request to complete.", true);
        }

        WebElement carteFrance = driver.findElement(By.cssSelector("#mapFrance > svg"));

        Actions builder = new Actions(driver);
        builder.clickAndHold(carteFrance).release(carteFrance).build().perform();
    }

    private void etape2() {
        driver.findElement(By.id("electricite")).click();
    }

}
