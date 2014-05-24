package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * Classe de test selenium pour la creation de nouveau devis par le client
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestNouveauDevis extends AbstractITTest {

	private String nouveauDevisDepartementURL = "/nouveaudevis/departement/41";

	@Override
	public void prepareDB() throws Exception {
		Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA);
		DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
		dbSetup.launch();
	}

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
		connexionApplication(AbstractITTest.BON_MOT_DE_PASSE);
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
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCreationNouveauDevisNonAuthenticatedSucceed() throws InterruptedException {
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
		driver.findElement(By.name("cguConfirmation")).click();
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
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCreationNouveauDevisSubscribeNotAuthenticatedSucceed() throws InterruptedException {
		driver.get(appUrl + nouveauDevisDepartementURL);
		// On remplit l'étape 2
		etape2();
		etape3EnModeInscris();

		assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
		        .findElement(By.cssSelector("h5")).getText());

	}

	/**
	 * Cas de test : L'utilisateur crée deux devis, la deuxieme, l'application
	 * doit lui renvoyer un message d'erreur.
	 * 
	 * Remarque : On saute la selection du département avec selenium, car
	 * Raphael n'est visiblement pas compatible avec selenium.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCreationNouveauDevisDuplicate() throws InterruptedException {
		// Premiere saisie de l'annonce.
		driver.get(appUrl + nouveauDevisDepartementURL);
		etape2();
		etape3EnModeInscris();

		assertEquals("Votre devis a été mis en ligne, nous vous avons envoyé un mail récapitulatif", driver
		        .findElement(By.cssSelector("h5")).getText());

		// Deuxieme saisie de l'annonce.
		driver.get(appUrl + nouveauDevisDepartementURL);
		etape2();

		assertEquals("Problème pendant l'enregistrement de l'annonce, veuillez nous excuser pour la gène occasionnée.",
		        driver.findElement(By.cssSelector("h5")).getText());

	}

	private void etape3EnModeInscris() throws InterruptedException {
		// Etape 3
		driver.findElement(By.id("connexionEtape3")).click();
		waitForTheElement("ui-id-1");
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]")).click();
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]")).clear();
		driver.findElement(By.cssSelector("table.tableBatimenLogin > tbody > tr > td > input[name=\"login\"]"))
		        .sendKeys("raiden");
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).click();
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='password'])[2]")).sendKeys("lollollol");
		driver.findElement(By.id("signInButton")).click();
	}

	private void etape2() {
		new Select(driver.findElement(By.id("corpsMetierSelect"))).selectByVisibleText("Electricité");
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
