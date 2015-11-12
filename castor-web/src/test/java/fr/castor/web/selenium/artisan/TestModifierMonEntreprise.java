package fr.castor.web.selenium.artisan;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.castor.web.selenium.dataset.ModifierMonProfilDataset.*;

/**
 * Test d'integration de modification des données entreprise de l'artisan
 */
public class TestModifierMonEntreprise extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                INSERT_ADRESSE_DATA, INSERT_ENTREPRISE_DATA,
                INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_AVIS_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Before
    public void goToModifierMonEntreprise() {
        driver.get(appUrl);
        connexionApplication("pebron", BON_MOT_DE_PASSE, Boolean.TRUE);
        findElement(By.id("connexionlbl")).click();
        findElement(By.linkText("Modifier mes informations")).click();
    }

    /**
     * Cas de test : L'artisan se rend dans son espace et modifie les informations de son entreprise.
     */
    @Test
    public void modifierInformationMonEntreprise() throws InterruptedException {
        (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX)).until(ExpectedConditions
                .elementToBeClickable(By.id("electricite")));
        findElement(By.id("electricite")).click();
        findElement(By.id("menuiserie")).click();

        findElement(By.id("nbEmployeField")).clear();
        findElement(By.id("nbEmployeField")).sendKeys("20");
        findElement(By.id("entreprisedateCreation")).click();
        findElement(By.id("codePostalField")).clear();
        findElement(By.id("codePostalField")).sendKeys("06700");
        findElement(By.id("villeField")).clear();
        findElement(By.id("villeField")).sendKeys("ST LAURENT DU VAR");

        scrollTo(250);

        findElementAndWaitPresence(By.id("validateEtape3Partenaire")).click();

        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Profil mis à jour avec succés"));

        Assert.assertTrue(checkConditionModificationInformation);
        verificationModificationPageMonEntreprise();

    }

    private void verificationModificationPageMonEntreprise() {
        findElement(By.linkText("Voir mon entreprise")).click();
        (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX)).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//div[@id='containerActivite']/span[2]")));
    }

    /**
     * Cas de test : L'artisan se rend dans son espace clique sur modifier son profil et rajoute une photo chantier témoin.
     */
    @Test
    public void ajouterPhotoChantierTemoin() throws InterruptedException {
        testAjoutPhotoIT(driver, false);
    }

    /**
     * Cas de test : L'artisan se rend dans son espace clique sur modifier son profil et rajoute une photo chantier témoin.
     */
    @Test
    public void supprimerPhotoChantierTemoin() throws InterruptedException {
        testAjoutPhotoIT(driver, false);
        suppressionPhotoIT(driver);
    }
}