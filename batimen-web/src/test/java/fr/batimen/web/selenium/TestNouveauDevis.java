package fr.batimen.web.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Classe de test selenium pour la creation de nouveau devis par le client
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestNouveauDevis extends AbstractSeleniumTest {

	private String nouveauDevisDepartementURL = "/nouveaudevis/departement/41";

	/**
	 * Cas de test : L'utilisateur rempli son devis en s'étant authentifié juste
	 * avant. Le devis doit se créer correctement.
	 * 
	 * Remarque : On saute la selection du département avec selenium, car
	 * Raphael n'est visiblement pas compatible avec selenium.
	 * 
	 */
	@Test
	public void testCreationNouveauDevisAuthenticatedSucceed() {
		driver.get(appUrl + nouveauDevisDepartementURL);
		// On s'authentifie à l'application
		connexionApplication();
		// On passe l'etape 2
		etape2();
		// On vérifie que le label est correcte
		assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
				.findElement(By.cssSelector("h5")).getText());

	}

	/**
	 * Cas de test : L'utilisateur crée un nouveau devis alors qu'il n'est pas
	 * inscrit (et donc pas authentifié). Le devis doit se créer correctement.
	 * 
	 * Remarque : On saute la selection du département avec selenium, car
	 * Raphael n'est visiblement pas compatible avec selenium.
	 */
	@Test
	public void testCreationNouveauDevisNonAuthenticatedSucceed() {
		driver.get(appUrl + nouveauDevisDepartementURL);
		// On remplit l'étape 2
		etape2();
		// Etape 3
		new Select(driver.findElement(By.id("civilite"))).selectByVisibleText("Monsieur");
		driver.findElement(By.id("nom")).clear();
		driver.findElement(By.id("nom")).sendKeys("Selenium");
		driver.findElement(By.id("prenom")).clear();
		driver.findElement(By.id("prenom")).sendKeys("Test");
		driver.findElement(By.id("numeroTel")).clear();
		driver.findElement(By.id("numeroTel")).sendKeys("0614528796");
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("test@selenium.fr");
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("selenium");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("mdrlollol");
		driver.findElement(By.id("confirmPassword")).clear();
		driver.findElement(By.id("confirmPassword")).sendKeys("mdrlollol");
		driver.findElement(By.id("validateInscription")).click();
		assertEquals(
				"Votre compte a bien été créé, un e-mail vous a été envoyé, Cliquez sur le lien présent dans celui-ci pour l'activer",
				driver.findElement(By.cssSelector("h5")).getText());

	}

	/**
	 * Cas de test : L'utilisateur crée un nouveau devis alors qu'il est inscrit
	 * mais pas authentifié, il s'authentifie lors de l'étape 3. Le devis doit
	 * se créer correctement.
	 * 
	 * Remarque : On saute la selection du département avec selenium, car
	 * Raphael n'est visiblement pas compatible avec selenium.
	 */
	@Test
	public void testCreationNouveauDevisSubscribeNotAuthenticatedSucceed() {
		driver.get(appUrl + nouveauDevisDepartementURL);
		// On remplit l'étape 2
		etape2();
		// Etape 3
		driver.findElement(By.id("connexionEtape3")).click();
		Boolean checkCondition = (new WebDriverWait(driver, 5)).until(ExpectedConditions
				.textToBePresentInElementLocated(By.id("ui-id-1"), "Connexion à l'espace client / artisan"));
		assertTrue(checkCondition);
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]")).click();
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]")).clear();
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]"))
				.sendKeys("raiden");
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).click();
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).sendKeys("lollollol");
		driver.findElement(By.id("signInButton")).click();

		assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
				.findElement(By.cssSelector("h5")).getText());

	}

	private void etape2() {
		new Select(driver.findElement(By.id("corpsMetierSelect"))).selectByVisibleText("Electricien");
		driver.findElement(By.id("objetDevisField")).clear();
		driver.findElement(By.id("objetDevisField")).sendKeys("Travaux electrique");
		driver.findElement(By.id("descriptionDevisField")).clear();
		driver.findElement(By.id("descriptionDevisField")).sendKeys("Refonte complete de l'electricite dans la maison");
		new Select(driver.findElement(By.id("typeContactField"))).selectByVisibleText("Email");
		new Select(driver.findElement(By.id("delaiInterventionField")))
				.selectByVisibleText("Le plus rapidement possible");
		driver.findElement(By.id("nbDevisField")).clear();
		driver.findElement(By.id("nbDevisField")).sendKeys("5");
		driver.findElement(By.id("adresseField")).clear();
		driver.findElement(By.id("adresseField")).sendKeys("106 avenue du test selenium");
		driver.findElement(By.id("adresseComplementField")).clear();
		driver.findElement(By.id("adresseComplementField")).sendKeys("complement du test");
		driver.findElement(By.id("codePostalField")).clear();
		driver.findElement(By.id("codePostalField")).sendKeys("04200");
		driver.findElement(By.id("villeField")).clear();
		driver.findElement(By.id("villeField")).sendKeys("selenium city");
		driver.findElement(By.id("validateQualification")).click();
	}

}
