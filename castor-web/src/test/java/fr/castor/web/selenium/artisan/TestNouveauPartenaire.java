package fr.castor.web.selenium.artisan;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
import fr.castor.web.utils.UtilsSelenium;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertTrue;

/**
 * Classe de cas de test concernant l'inscription d'un nouveau partenaire
 *
 * @author Casaucau Cyril
 */
public class TestNouveauPartenaire extends AbstractITTest {

    private final String nouveauPartenaireURL = "/nouveaupartenaire/";

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur (artisan) crée son compte, l'operation doit
     * être un succés
     *
     * @throws InterruptedException
     */
    @Test
    public void testInscriptionNouveauPartenaireNominal() throws InterruptedException {
        driver.get(appUrl + nouveauPartenaireURL);

        // On selectionne un departement
        UtilsSelenium.etape1(driver);
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        etape2();
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        etape3();
        if (browser.equals("ie")) {
            Thread.sleep(1000);
        }
        etape4();
    }

    /**
     * Cas de test : L'utilisateur (artisan) crée son compte, mais il n'est pas
     * sur des informations donc il revient sur les etapes précédentes,
     * l'operation doit être un succés
     *
     * @throws InterruptedException
     */
    @Test
    public void testInscriptionNouveauPartenaireEtapePrecedente() throws InterruptedException {
        driver.get(appUrl + nouveauPartenaireURL);

        // On selectionne un departement
        UtilsSelenium.etape1(driver);
        etape2();

        WebElement etapeButionNouveauArtisan = new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX).until(ExpectedConditions
                .presenceOfElementLocated(By.id("etapePrecedenteNouveauArtisan3")));
        Thread.sleep(1000);

        driver.findElement(By.id("etapePrecedenteNouveauArtisan3")).click();
        etape2();
        Thread.sleep(1000);
        driver.findElement(By.id("etapePrecedenteNouveauArtisan3")).click();
        Thread.sleep(1000);
        etape2();
        Thread.sleep(1000);
        etape3();

        etape4();
    }

    private void etape2() {
        // Etape 2
        new Select(driver.findElement(By.id("civilite"))).selectByVisibleText("Monsieur");
        driver.findElement(By.id("nom")).clear();
        driver.findElement(By.id("nom")).sendKeys("Dupont");
        driver.findElement(By.id("prenomField")).clear();
        driver.findElement(By.id("prenomField")).sendKeys("Xavier");
        driver.findElement(By.id("numeroTelField")).clear();
        driver.findElement(By.id("numeroTelField")).sendKeys("0493854578");
        driver.findElement(By.id("emailField")).clear();
        driver.findElement(By.id("emailField")).sendKeys("artisan.castor@outlook.fr");
        driver.findElement(By.id("logintField")).clear();
        driver.findElement(By.id("logintField")).sendKeys("xavier06");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("lolmdr06");
        driver.findElement(By.id("confirmPassword")).clear();
        driver.findElement(By.id("confirmPassword")).sendKeys("lolmdr06");
        driver.findElement(By.id("validateEtape2Partenaire")).click();
    }

    private void etape3() throws InterruptedException {
        // Etape 3
        (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX)).until(ExpectedConditions
                .visibilityOfElementLocated(By.id("electricite")));

        driver.findElement(By.id("electricite")).click();
        driver.findElement(By.id("plomberie")).click();

        driver.findElement(By.id("nomComplet")).clear();
        driver.findElement(By.id("nomComplet")).sendKeys("Xav Entreprise");
        new Select(driver.findElement(By.id("statutJuridique"))).selectByVisibleText("SARL");
        driver.findElement(By.id("nbEmployeField")).clear();
        driver.findElement(By.id("nbEmployeField")).sendKeys("5");
        driver.findElement(By.id("entreprisedateCreation")).clear();
        driver.findElement(By.id("entreprisedateCreation")).sendKeys("01/05/2013");
        driver.findElement(By.id("siretField")).clear();
        driver.findElement(By.id("siretField")).sendKeys("43394298400017");
        driver.findElement(By.id("adresseField")).clear();
        driver.findElement(By.id("adresseField")).sendKeys("450 chemin du xav");
        driver.findElement(By.id("codePostalField")).clear();
        driver.findElement(By.id("codePostalField")).sendKeys("06700");
        driver.findElement(By.id("villeField")).clear();
        driver.findElement(By.id("villeField")).sendKeys("ST LAURENT DU VAR");

        driver.findElement(By.id("validateEtape3Partenaire")).click();
    }

    private void etape4() {
        // Etape 4 confirmation
        Boolean checkConditionConfirmation1 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@id='confirmation']/h5[1]"),
                        "Votre compte a été créé avec succés"));

        Boolean checkConditionConfirmation2 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@id='confirmation']/h5[2]"),
                        "Les équipes de les castors vont valider votre dossier, nous vous tiendrons informé dès que votre compte sera activé."));

        assertTrue(checkConditionConfirmation1);
        assertTrue(checkConditionConfirmation2);
    }



}