package fr.batimen.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.core.constant.UrlPage;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.*;

/**
 * Test Selenium concernant la page de modification d'une annonce.
 *
 * @author Casaucau Cyril.
 *
 * Created by Casaucau on 20/04/2015.
 */
public class TestModificationAnnonce  extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION, INSERT_ADRESSE_DATA,
                INSERT_ENTREPRISE_DATA, INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_NOTATION_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN, INSERT_ANNONCE_IMAGE);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Before
    public void connectToAnnonceBeforeModification(){
        driver.get(appUrl);
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        // On calcul l'url d'accés direct à l'annonce.
        StringBuilder appUrlAnnonce = new StringBuilder(appUrl);
        appUrlAnnonce.append(UrlPage.ANNONCE).append("?idAnnonce=").append("toto");
        driver.get(appUrlAnnonce.toString());
    }


    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, puis modifie les données de l'annonce
     *
     */
    @Test
    public void testModificationAnnonce() {
        driver.findElement(By.linkText("Modifier votre annonce")).click();
        new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Téléphone");
        new Select(driver.findElement(By.id("sousCategorieSelect"))).selectByVisibleText("Interphone");
        driver.findElement(By.id("radioTypeTravauxRenovation")).click();
        driver.findElement(By.id("typeTravauxRenovation")).click();
        driver.findElement(By.id("validateQualification")).click();
        Boolean checkSousCategoriePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre annonce a été modifiée avec succés !"));
        Assert.assertTrue(checkSousCategoriePresent);
    }
}
