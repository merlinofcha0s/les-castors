package fr.batimen.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.core.constant.UrlPage;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.*;

/**
 * Test Selenium concernant la page de modification d'une annonce.
 *
 * @author Casaucau Cyril.
 *         <p/>
 *         Created by Casaucau on 20/04/2015.
 */
public class TestModificationAnnonce extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION, INSERT_ADRESSE_DATA,
                INSERT_ENTREPRISE_DATA, INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_NOTATION_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN, INSERT_ANNONCE_IMAGE);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Before
    public void connectToAnnonceBeforeModification() {
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
     */
    @Test
    public void testModificationAnnonceNominale() {
        driver.findElement(By.linkText("Modifier votre annonce")).click();
        new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Téléphone");
        new Select(driver.findElement(By.id("sousCategorieSelect"))).selectByVisibleText("Interphone");
        driver.findElement(By.id("radioTypeTravauxRenovation")).click();
        driver.findElement(By.id("typeTravauxRenovation")).click();
        driver.findElement(By.id("validateQualification")).click();
        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre annonce a été modifiée avec succés !"));
        Assert.assertTrue(checkUntilModifOK);
    }

    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, puis ne modifie pas les données de son annonce et clique sur le bouton revenir à l'annonce.
     */
    @Test
    public void testModificationAnnonceRetourAnnonce() {
        driver.findElement(By.linkText("Modifier votre annonce")).click();
        driver.findElement(By.id("etapePrecedente3")).click();
        WebElement checkUntilBackToAnnonce = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.headInModule")));
        Assert.assertNotNull(checkUntilBackToAnnonce);
    }

    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, puis rajoute une photo, tout doit bien se passer.
     */
    @Test
    public void testAjoutPhotoModificationAnnonce() {
        testAjoutPhotoIT();
    }

    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, puis rajoute une photo, puis la supprime
     */
    @Test
    public void testSuppressionPhotoModificationAnnonce() {
        testAjoutPhotoIT();
        //Clique sur le bouton supprimer la photo
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[3]/div[2]/div/div[1]/div/div/div[2]/div[1]/div/div/a/div")).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Suppression effectuée !"));
        Assert.assertTrue(checkUntilModifOK);
    }



    private void testAjoutPhotoIT() {
        driver.findElement(By.linkText("Modifier votre annonce")).click();
        WebElement checkUntilAfficheModifPage = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("html.ie.ie9 body div.main_wrapper div.content_wrapper div.container div.content_block.no-sidebar.row div.fl-container.span12 div.row div.posts-block.span12 div.contentarea div.row-fluid div.span10.module_cont.module_text_area.module_small_padding div div.row-fluid div.span12 div.bg_title h4.headInModule")));
        Assert.assertNotNull(checkUntilAfficheModifPage);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("img/castor.jpg").getFile());

        WebElement El = driver.findElement(By.id("photoField"));
        El.sendKeys(file.getAbsolutePath());
        driver.findElement(By.id("envoyerPhotos")).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Photo(s) rajoutée(s) avec succés"));
        Assert.assertTrue(checkUntilModifOK);
    }
}