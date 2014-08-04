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
 * Classe de test selenium pour la popup d'authentification client.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestAuthentification extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Test
    public void testAuthentificationSuccess() throws Exception {
        driver.get(appUrl);
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE);
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "Mon Compte"));
        assertTrue(checkCondition);
    }

    @Test
    public void testAuthentificationFailed() throws Exception {
        driver.get(appUrl);
        connexionApplication("raiden", AbstractITTest.MAUVAIS_MOT_DE_PASSE);
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("errorLogin"),
                        "Erreur dans la saisie ou identifiants inconnues, veuillez recommencer"));
        assertTrue(checkCondition);
    }

    @Test
    public void testAuthentificationFailedBecauseNotActivated() throws Exception {
        driver.get(appUrl);
        connexionApplication("xavier", AbstractITTest.BON_MOT_DE_PASSE);
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("errorLogin"),
                        "Erreur dans la saisie / identifiants inconnues / compte pas activé, veuillez recommencer"));
        assertTrue(checkCondition);
    }

}
