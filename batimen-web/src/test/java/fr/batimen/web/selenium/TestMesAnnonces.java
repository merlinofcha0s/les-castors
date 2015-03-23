package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

import fr.batimen.web.selenium.dataset.MesAnnoncesDataset;

/**
 * Test Selenium concernant la page mes annonces.
 * 
 * @author Casaucau Cyril.
 * 
 */
public class TestMesAnnonces extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                MesAnnoncesDataset.INSERT_ADRESSE_DATA, MesAnnoncesDataset.INSERT_ENTREPRISE_DATA,
                MesAnnoncesDataset.INSERT_ARTISAN_DATA, MesAnnoncesDataset.INSERT_NOTATION_DATA,
                MesAnnoncesDataset.INSERT_ANNONCE_DATA, MesAnnoncesDataset.INSERT_NOTIFICATION_DATA);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : l'utilisateur se rend sur le site, se connecte et va dans
     * la rubrique mon compte. La, il tombe sur ses notifications ainsi que sur
     * ces annonces postées. Enfin il essaye d'accéder à une de ces annonces via
     * cette page
     * 
     */
    @Test
    public void testAccessToMesAnnonce() {
        driver.get(appUrl);
        // On s'authentifie à l'application
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);

        driver.findElement(By.id("connexionlbl")).click();

        Boolean checkConditionNotificationPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//td[2]/span"),
                        "s'est inscrit à votre"));
        assertTrue(checkConditionNotificationPresent);

        Boolean checkConditionAnnonceDescription = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(
                        By.xpath("//div[2]/table/tbody/tr/td[2]/span"), "Construction compliqué qui nec..."));
        assertTrue(checkConditionAnnonceDescription);

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h4.headInModule")));
        assertNotNull(checkConditionAnnoncePresent);

        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[2]/table/tbody/tr[1]/td[5]/a"))
                .click();

        Boolean checkConditionAccessToAnnonceViaList = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1.title"),
                        "ANNONCE PARTICULIER"));
        assertTrue(checkConditionAccessToAnnonceViaList);

        driver.findElement(By.id("connexionlbl")).click();

        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[1]/table/tbody/tr[1]/td[2]/a[2]"))
                .click();

        Boolean checkConditionAccessToAnnonceViaNotif = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1.title"),
                        "ANNONCE PARTICULIER"));
        assertTrue(checkConditionAccessToAnnonceViaNotif);

    }

}
