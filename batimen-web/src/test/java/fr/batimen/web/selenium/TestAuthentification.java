package fr.batimen.web.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Classe de test selenium pour la popup d'authentification client.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestAuthentification extends AbstractSeleniumTest {

	@Test
	public void testAuthentificationSuccess() throws Exception {
		driver.get(appUrl);
		connexionApplication(AbstractSeleniumTest.BON_MOT_DE_PASSE);
		Boolean checkCondition = (new WebDriverWait(driver, AbstractSeleniumTest.TEMPS_ATTENTE_AJAX))
		        .until(ExpectedConditions.textToBePresentInElementLocated(By.id("connexionlbl"), "Mon Compte"));
		assertTrue(checkCondition);
	}

	@Test
	public void testAuthentificationFailed() throws Exception {
		driver.get(appUrl);
		connexionApplication(AbstractSeleniumTest.MAUVAIS_MOT_DE_PASSE);
		Boolean checkCondition = (new WebDriverWait(driver, AbstractSeleniumTest.TEMPS_ATTENTE_AJAX))
		        .until(ExpectedConditions.textToBePresentInElementLocated(By.id("errorLogin"),
		                "Erreur dans la saisie ou identifiants inconnues, veuillez recommencer"));
		assertTrue(checkCondition);
	}
}
