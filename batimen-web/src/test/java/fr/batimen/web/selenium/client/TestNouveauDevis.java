package fr.batimen.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.web.selenium.common.AbstractITTest;
import fr.batimen.web.utils.UtilsSelenium;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.*;

/**
 * Classe de test selenium pour la creation de nouveau devis par le client
 *
 * @author Casaucau Cyril
 */
public class TestNouveauDevis extends AbstractITTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestNouveauDevis.class);
    private final String nouveauDevisURL = "/nouveaudevis/";

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur rempli son devis en s'étant authentifié juste
     * avant. Le devis doit se créer correctement.
     *
     * @throws InterruptedException
     */
    @Test
    public void testCreationNouveauDevisAuthenticatedSucceed() throws InterruptedException {
        driver.get(appUrl + nouveauDevisURL);

        // On passe à l'etape 1
        UtilsSelenium.etape1(driver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Fail to wait authentication", e);
            }
        }
        // On remplit l'étape 2
        etape2();
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        // On passe l'etape 3
        etape3(false);

        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        // On s'authentifie à l'application
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);

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
        driver.get(appUrl + nouveauDevisURL);
        // On passe à l'etape 1
        UtilsSelenium.etape1(driver);
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);
        // Etape 3
        driver.findElement(By.id("nom")).clear();
        driver.findElement(By.id("nom")).sendKeys("Selenium");
        driver.findElement(By.id("prenom")).clear();
        driver.findElement(By.id("prenom")).sendKeys("Test");
        driver.findElement(By.id("numeroTel")).clear();
        driver.findElement(By.id("numeroTel")).sendKeys("0614528796");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("castor0610@hotmail.fr");
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("selenium");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("mdrlollol");
        driver.findElement(By.id("confirmPassword")).clear();
        driver.findElement(By.id("confirmPassword")).sendKeys("mdrlollol");
        driver.findElement(By.name("fieldContainer:CGUContainer:cguConfirmation")).click();
        driver.findElement(By.id("validateInscription")).click();

        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h5"),
                        "Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer"));
        assertTrue(checkCondition);
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
        driver.get(appUrl + nouveauDevisURL);
        // On selectionne le bon département
        UtilsSelenium.etape1(driver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Fail to wait authentication", e);
            }
        }
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        // On se connecte
        connexionApplication("raiden", BON_MOT_DE_PASSE, Boolean.TRUE);

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
        driver.get(appUrl + nouveauDevisURL);
        // On selectionne le bon département
        UtilsSelenium.etape1(driver);
        // On remplit l'étape 2
        etape2();
        Thread.sleep(1000);
        // On passe à l'étape 3
        etape3(false);

        connexionApplication("raiden", BON_MOT_DE_PASSE, Boolean.TRUE);

        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

        // Deuxieme saisie de l'annonce.
        driver.get(appUrl + nouveauDevisURL);
        UtilsSelenium.etape1(driver);
        etape2();
        etape3(true);
        Thread.sleep(1000);
        assertEquals("Problème pendant l'enregistrement de l'annonce, veuillez nous excuser pour la gène occasionnée.",
                driver.findElement(By.cssSelector("h5")).getText());
    }

    /**
     * Cas de test : L'utilisateur crée un devis mais veut modifier les
     * informations de l'etape précédente, il clique soit sur le bouton etape
     * précédente ou soit sur le wizard, l'operation doit etre un succés
     *
     * @throws InterruptedException
     */
    @Test
    public void testEtapePrecedente() throws InterruptedException {
        driver.get(appUrl + nouveauDevisURL);
        // On selectionne le bon département
        UtilsSelenium.etape1(driver);

        Thread.sleep(1000);
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        driver.findElement(By.id("etapePrecedente4")).click();

        Thread.sleep(1000);
        driver.findElement(By.id("etapePrecedente3")).click();

        Thread.sleep(1000);
        driver.findElement(By.id("etapePrecedente2")).click();

        Thread.sleep(1000);

        // On selectionne le bon département
        UtilsSelenium.etape1(driver);

        Thread.sleep(1000);
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        WebElement checkConditionPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@id='batimenWizard']/ul/li[2]"))));
        assertNotNull(checkConditionPresent);
        driver.findElement(By.xpath("//div[@id='batimenWizard']/ul/li[2]")).click();

        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        connexionApplication("raiden", BON_MOT_DE_PASSE, Boolean.TRUE);

        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

    }

    /**
     * Cas de test : L'utilisateur crée un devis. Il est connecté, lors de l'etape 3, il dit qu'il veut etre contacté par téléphone
     * mais il n'a pas spécifié de numéro, on lui refuse tant qu'il ne l'a pas fait.
     *
     * @throws InterruptedException
     */
    @Test
    public void testContactTelMaisPasTelPasRenseigne() throws InterruptedException {
        driver.get(appUrl + nouveauDevisURL);
        // On selectionne le bon département
        UtilsSelenium.etape1(driver);

        Thread.sleep(1000);
        // On remplit l'étape 2
        etape2();
        Thread.sleep(1000);
        connexionApplication("xavier", BON_MOT_DE_PASSE, Boolean.TRUE);
        // On remplit l'étape 3
        etape3(true);

        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type6"),
                        "Veuillez renseigner un numéro de téléphone dans la rubrique mon compte > modifier mon profil si vous voulez être contacté par téléphone"));
        assertTrue(checkConditionModificationInformation);

        Thread.sleep(1000);
    }

    private void etape3(boolean isAlreadyAuthenticate) throws InterruptedException {
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated((By.id("typeContactField"))));
        assertNotNull(checkConditionAnnoncePresent);
        driver.findElement(By.id("descriptionDevisField")).clear();
        driver.findElement(By.id("descriptionDevisField")).sendKeys("Refonte complete de l'electricite dans la maison");
        new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Téléphone");
        new Select(driver.findElement(By.id("delaiInterventionField")))
                .selectByVisibleText("Le plus rapidement possible");
        driver.findElement(By.id("radioTypeTravauxRenovation")).click();
        driver.findElement(By.id("adresseField")).clear();
        driver.findElement(By.id("adresseField")).sendKeys("106 avenue du test selenium");
        driver.findElement(By.id("adresseComplementField")).clear();
        driver.findElement(By.id("adresseComplementField")).sendKeys("complement du test");
        driver.findElement(By.id("codePostalField")).clear();
        driver.findElement(By.id("codePostalField")).sendKeys("06700");
        driver.findElement(By.id("villeField")).clear();
        driver.findElement(By.id("villeField")).sendKeys("ST LAURENT DU VAR");
        driver.findElement(By.id("validateQualification")).click();

        if (!isAlreadyAuthenticate) {
            (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("dejaInscrit")));
        }

        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
    }

    private void etape2() throws InterruptedException {
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }

        driver.findElement(By.id("motCleField")).sendKeys("Salles de bain");
        driver.findElement(By.linkText("Salles de bain")).click();
        driver.findElement(By.id("etapeSuivante")).click();
    }

}