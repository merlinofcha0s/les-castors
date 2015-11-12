package fr.castor.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
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
        etape1();

        // On remplit l'étape 2
        etape2();

        // On passe l'etape 3
        etape3(false);

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
        etape1();
        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);
        // Etape 3
        findElement(By.id("nom")).clear();
        findElement(By.id("nom")).sendKeys("Selenium");
        findElement(By.id("prenom")).clear();
        findElement(By.id("prenom")).sendKeys("Test");
        findElement(By.id("numeroTel")).clear();
        findElement(By.id("numeroTel")).sendKeys("0614528796");
        findElement(By.id("email")).clear();
        findElement(By.id("email")).sendKeys("castor0610@hotmail.fr");
        findElement(By.id("login")).clear();
        findElement(By.id("login")).sendKeys("selenium");
        findElement(By.id("password")).clear();
        findElement(By.id("password")).sendKeys("mdrlollol");
        findElement(By.id("confirmPassword")).clear();
        findElement(By.id("confirmPassword")).sendKeys("mdrlollol");
        findElement(By.name("fieldContainer:CGUContainer:cguConfirmation")).click();
        findElement(By.id("validateInscription")).click();

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
        etape1();
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
        etape1();
        // On remplit l'étape 2
        etape2();
        // On passe à l'étape 3
        etape3(false);

        connexionApplication("raiden", BON_MOT_DE_PASSE, Boolean.TRUE);

        assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
                .findElement(By.cssSelector("h5")).getText());

        // Deuxieme saisie de l'annonce.
        driver.get(appUrl + nouveauDevisURL);
        etape1();
        etape2();
        etape3(true);
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
        etape1();

        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        Thread.sleep(1000);
        findElement(By.id("etapePrecedente4")).click();

        findElement(By.id("etapePrecedente3")).click();

        findElement(By.id("etapePrecedente2")).click();

        // On selectionne le bon département
        etape1();

        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        Thread.sleep(1000);
        WebElement checkConditionPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@id='batimenWizard']/ul/li[2]"))));
        assertNotNull(checkConditionPresent);
        findElement(By.xpath("//div[@id='batimenWizard']/ul/li[2]")).click();

        // On remplit l'étape 2
        etape2();
        // On remplit l'étape 3
        etape3(false);

        Thread.sleep(1000);
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
        etape1();

        // On remplit l'étape 2
        etape2();

        connexionApplication("xavier", BON_MOT_DE_PASSE, Boolean.TRUE);
        // On remplit l'étape 3
        etape3(true);

        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type6"),
                        "Veuillez renseigner un numéro de téléphone dans la rubrique mon compte > modifier mon profil si vous voulez être contacté par téléphone"));
        assertTrue(checkConditionModificationInformation);
    }

    /**
     * Cas de test : L'utilisateur crée un devis. Il est connecté, lors de l'etape 3, il dit qu'il veut etre contacté par téléphone
     * mais il n'a pas spécifié de numéro, on lui refuse tant qu'il ne l'a pas fait.
     *
     * @throws InterruptedException
     */
    @Test
    public void testSaisiMotCleSansClique() throws InterruptedException {
        driver.get(appUrl + nouveauDevisURL);
        // On selectionne le bon département
        etape1();

        findElement(By.id("motCleField")).sendKeys("Salles de bain");
        findElement(By.id("etapeSuivante")).click();

        etape3(false);

        connexionApplication("raiden", BON_MOT_DE_PASSE, Boolean.TRUE);
    }

    private void etape3(boolean isAlreadyAuthenticate) throws InterruptedException {
        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated((By.id("typeContactField"))));
        assertNotNull(checkConditionAnnoncePresent);
        findElement(By.id("descriptionDevisField")).clear();
        findElement(By.id("descriptionDevisField")).sendKeys("Refonte complete de l'electricite dans la maison");
        new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Téléphone");
        new Select(driver.findElement(By.id("delaiInterventionField")))
                .selectByVisibleText("Le plus rapidement possible");
        findElement(By.id("radioTypeTravauxRenovation")).click();
        findElement(By.id("adresseField")).clear();
        findElement(By.id("adresseField")).sendKeys("106 avenue du test selenium");
        findElement(By.id("adresseComplementField")).clear();
        findElement(By.id("adresseComplementField")).sendKeys("complement du test");
        findElement(By.id("codePostalField")).clear();
        findElement(By.id("codePostalField")).sendKeys("06700");
        findElement(By.id("villeField")).clear();
        findElement(By.id("villeField")).sendKeys("ST LAURENT DU VAR");
        findElement(By.id("validateQualification")).click();

        if (!isAlreadyAuthenticate) {
            (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("dejaInscrit")));
        }
        Thread.sleep(1000);
    }

    private void etape2() throws InterruptedException {
        findElement(By.id("motCleField")).sendKeys("Salles de bain");
        findElement(By.linkText("Salles de bain")).click();

        findElement(By.id("etapeSuivante")).click();
    }

}