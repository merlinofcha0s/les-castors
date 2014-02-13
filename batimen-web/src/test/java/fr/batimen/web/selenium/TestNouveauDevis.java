package fr.batimen.web.selenium;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class TestNouveauDevis extends AbstractSeleniumTest {

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
		driver.get(appUrl + "/nouveaudevis/departement/41");
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
		driver.get(appUrl + "/nouveaudevis/departement/41");
		// On remplit l'étape 2
		etape2();

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
