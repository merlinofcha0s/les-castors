package fr.castor.web.selenium;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.castor.web.selenium.dataset.RechercheDataset.*;

/**
 * Test d'intégration pour l'IHM de recherche d'annonce
 *
 * @author Casaucau Cyril
 */
public class TestRechercheAnnonce extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION, INSERT_ADRESSE_DATA,
                INSERT_ENTREPRISE_DATA, INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_AVIS_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN, INSERT_ANNONCE_IMAGE,
                INSERT_ANNONCE_MOT_CLE, INSERT_CATEGORIE_METIER);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : un artisan se connecte sur son compte et recherche des annonces.
     *
     * @throws Exception
     */
    @Test
    public void testRechercheNominal() throws Exception {
        driver.get(appUrl);
        connexionApplication("pebron", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);

        findElement(By.id("rechercheAnnonce")).click();

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.title")));

        Assert.assertNotNull(checkConditionAnnoncePresent);

        findElement(By.id("electricite")).click();
        findElement(By.id("plomberie")).click();
        findElement(By.id("rechercheDepartement")).clear();
        findElement(By.id("rechercheDepartement")).sendKeys("6");
        findElement(By.id("rechercheValider")).click();

        Boolean checkConditionAnnonceRechercheOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("infoNbAnnonce"), "5 annonces affichées sur 6"));

        Assert.assertTrue(checkConditionAnnonceRechercheOK);

        findElement(By.id("btnPlusDAvisEntreprise")).click();

        Boolean checkConditionAnnonceRechercheOKPlusDeResultat = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("infoNbAnnonce"), "6 annonces affichées sur 6"));

        Assert.assertTrue(checkConditionAnnonceRechercheOKPlusDeResultat);
    }
}