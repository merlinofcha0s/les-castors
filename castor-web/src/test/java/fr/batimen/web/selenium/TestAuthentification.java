package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

import fr.batimen.web.selenium.common.AbstractITTest;

/**
 * Classe de test selenium pour la popup d'authentification client.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestAuthentification extends AbstractITTest {

    private final String messageErreur = "Erreur dans la saisie / identifiants inconnues / compte pas activé, veuillez recommencer";

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Test
    public void testAuthentificationAndDisconnectSuccess() throws Exception {
        driver.get(appUrl);
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean checkConditionMonCompte = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "MON COMPTE"));
        assertTrue(checkConditionMonCompte);

        WebElement iconConnected = driver.findElement(By.cssSelector("#iconConnected"));
        WebElement logout = driver.findElement(By.id("logout"));
        Actions builder = new Actions(driver);
        builder.click(iconConnected).moveToElement(logout).click().build().perform();

        Boolean checkConditionEspaceMembre = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "ESPACE MEMBRE"));
        assertTrue(checkConditionEspaceMembre);

    }

    @Test
    public void testAuthentificationFailed() throws Exception {
        driver.get(appUrl);
        connexionApplication("raiden", AbstractITTest.MAUVAIS_MOT_DE_PASSE, Boolean.FALSE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("errorLogin"), messageErreur));
        assertTrue(checkCondition);
    }

    @Test
    public void testAuthentificationFailedBecauseNotActivated() throws Exception {
        driver.get(appUrl);
        connexionApplication("xdlol", AbstractITTest.BON_MOT_DE_PASSE, Boolean.FALSE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("errorLogin"), messageErreur));
        assertTrue(checkCondition);
    }

}
