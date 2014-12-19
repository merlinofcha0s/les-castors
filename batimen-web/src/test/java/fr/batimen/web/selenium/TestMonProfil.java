package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

import fr.batimen.web.selenium.dataset.MesAnnoncesDataset;

public class TestMonProfil extends AbstractITTest {

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
     * la rubrique mon profil. La, il tombe sur ses données de profil.
     * 
     */
    @Test
    public void testAccessToMonProfil() {
        driver.get(appUrl);
        // On s'authentifie à l'application
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE);

        driver.findElement(By.id("signInButton")).click();
        driver.findElement(By.id("connexionLink")).click();

        By voirProfil = By.linkText("Voir le profil");

        WebElement checkConditionVoirProfil = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(voirProfil));
        assertNotNull(checkConditionVoirProfil);

        driver.findElement(voirProfil).click();

        WebElement checkConditionLoginPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#infoNomClient > h1")));

        assertNotNull(checkConditionLoginPresent);

        assertEquals("2", driver.findElement(By.cssSelector("#nbTravauxRealisesLbl > span")).getText());
        assertEquals("Prestation correct", driver.findElement(By.cssSelector("div.commentaireRater")).getText());
        assertEquals("Entreprise de toto", driver.findElement(By.cssSelector("div.nomEntreprise")).getText());
        assertEquals("Artisan moins sympatique", driver.findElement(By.cssSelector("div.commentaireClient")).getText());
    }
}
