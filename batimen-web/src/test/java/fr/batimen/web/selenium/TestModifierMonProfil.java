package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * Test d'intégration pour la page de modification d'un profil utilisateur <br/>
 * L'utilisateur se connecte à l'application et se dirige vers la page de
 * modification qui se trouve dans son profil
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestModifierMonProfil extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur modifie son login grace au formulaire de
     * modification des informations.
     * 
     */
    @Test
    public void modifyInformationsProfilInitial() {
        driver.get(appUrl);
        connexionApplication("raiden", BON_MOT_DE_PASSE);
        driver.findElement(By.id("connexionlbl")).click();
        driver.findElement(By.linkText("Modifier le profil")).click();

        assertModificationPage();

        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("raiden06");
        driver.findElement(By.id("validateInscription")).click();
        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Vos données ont bien été mises à jour"));
        assertTrue(checkConditionModificationInformation);
    }

    /**
     * Cas de test : l'utilisateur modifie de maniere correcte son mot de passe
     */
    @Test
    public void modifyInformationsProfilPasswordInformation() {
        driver.get(appUrl);
        connexionApplication("raiden", BON_MOT_DE_PASSE);
        driver.findElement(By.id("connexionlbl")).click();
        driver.findElement(By.linkText("Modifier le profil")).click();
        assertModificationPage();
        modificationMotDePasse("lollollol");
        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Vos données ont bien été mises à jour"));
        assertTrue(checkConditionModificationInformation);
    }

    /**
     * Cas de test : l'utilisateur modifie de maniere incorrecte son mot de
     * passe, l'appli lui signale
     */
    @Test
    public void modifyInformationsProfilPasswordInformationWrong() {
        driver.get(appUrl);
        connexionApplication("raiden", BON_MOT_DE_PASSE);
        driver.findElement(By.id("connexionlbl")).click();
        driver.findElement(By.linkText("Modifier le profil")).click();
        assertModificationPage();
        modificationMotDePasse("lollol");
        Boolean checkConditionModificationInformation = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type6"),
                        "Ancien mot de passe invalide"));
        assertTrue(checkConditionModificationInformation);
    }

    private void modificationMotDePasse(String motDePasse) {
        driver.findElement(By.id("oldpassword")).clear();
        driver.findElement(By.id("oldpassword")).sendKeys(motDePasse);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("lollollol06");
        driver.findElement(By.id("confirmPassword")).clear();
        driver.findElement(By.id("confirmPassword")).sendKeys("lollollol06");
        driver.findElement(By.id("validateInscription")).click();
    }

    private void assertModificationPage() {
        Boolean checkConditionModifierMonProfilPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h4.headInModule"),
                        "MODIFIER MON PROFIL"));
        assertTrue(checkConditionModifierMonProfilPresent);
    }
}
