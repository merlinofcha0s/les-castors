package fr.castor.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
import fr.castor.web.selenium.dataset.MesAnnoncesDataset;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestMonProfil extends AbstractITTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMonProfil.class);

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                MesAnnoncesDataset.INSERT_ADRESSE_DATA, MesAnnoncesDataset.INSERT_ENTREPRISE_DATA,
                MesAnnoncesDataset.INSERT_ARTISAN_DATA, MesAnnoncesDataset.INSERT_AVIS_DATA,
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
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);

        findElement(By.id("connexionlbl")).click();

        By voirProfil = By.linkText("Voir le profil");

        WebElement checkConditionVoirProfil = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(voirProfil));
        assertNotNull(checkConditionVoirProfil);

        findElement(voirProfil).click();

        WebElement checkConditionLoginPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#infoNomClient > h1")));

        assertNotNull(checkConditionLoginPresent);

        assertEquals("4", findElement(By.cssSelector("#nbTravauxRealisesLbl > span")).getText());
        assertEquals("Entreprise de toto", findElement(By.cssSelector("div.nomEntreprise")).getText());
        assertEquals("Artisan moins sympatique", findElement(By.cssSelector("div.commentaireClient")).getText());
    }
}
