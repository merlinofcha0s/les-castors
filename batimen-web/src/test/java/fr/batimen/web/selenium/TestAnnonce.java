package fr.batimen.web.selenium;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ADRESSE_DATA;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ANNONCE_ARTISAN;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ANNONCE_DATA;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ANNONCE_IMAGE;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ARTISAN_DATA;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ARTISAN_PERMISSION;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_ENTREPRISE_DATA;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_NOTATION_DATA;
import static fr.batimen.web.selenium.dataset.AnnonceDataset.INSERT_NOTIFICATION_DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

import fr.batimen.core.constant.UrlPage;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.web.selenium.common.AbstractITTest;

/**
 * Test d'intégration pour la page d'affichage d'une annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class TestAnnonce extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION, INSERT_ADRESSE_DATA,
                INSERT_ENTREPRISE_DATA, INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_NOTATION_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN, INSERT_ANNONCE_IMAGE);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, il doit y arriver
     * 
     */
    @Test
    public void testAnnonceAffichageWithClient() {
        connectAndGoToAnnonce(TypeCompte.CLIENT, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);

        assertEquals(
                "ENTREPRISES QUI SOUHAITENT VOUS CONTACTER",
                driver.findElement(
                        By.cssSelector("#containerEnteprisesInscrites > div.row-fluid > div.span12 > div.bg_title > h2.headInModule"))
                        .getText());
        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("containerContactMaster")));
        assertNotNull(checkConditionAnnoncePresent);
        assertEquals("Modifier votre annonce", driver.findElement(By.linkText("Modifier votre annonce")).getText());
        assertEquals("Supprimer l'annonce", driver.findElement(By.id("supprimerAnnonce")).getText());
        assertEquals("Aucune photo du chantier pour le moment :(", driver.findElement(By.id("aucunePhoto")).getText());

    }

    /**
     * Cas de test : Le client accede a son annonce en se connectant a les
     * castors, il doit y arriver
     * 
     */
    @Test
    public void testAnnonceAffichageWithClientWithImage() {
        connectAndGoToAnnonce(TypeCompte.CLIENT, "lolmdr");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("containerContactMaster")));
        WebElement checkConditionImageAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[3]/div/div/div[2]/div[1]/a")));
        assertNotNull(checkConditionAnnoncePresent);
        assertNotNull(checkConditionImageAnnoncePresent);
        assertEquals("Modifier votre annonce", driver.findElement(By.linkText("Modifier votre annonce")).getText());
        assertEquals("Supprimer l'annonce", driver.findElement(By.id("supprimerAnnonce")).getText());

    }

    /**
     * Cas de test : L'artisan essaye d'acceder a une annonce en se connectant a
     * les castors, il doit y arriver mais l'encar avec les entreprises deja
     * inscrite ne doit pas s'afficher
     * 
     */
    @Test(expected = NoSuchElementException.class)
    public void testAnnonceAffichageWithArtisan() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        connectAndGoToAnnonce(TypeCompte.ARTISAN, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);

        assertEquals("1", driver.findElement(By.xpath("//div[@id='containerInformationsAnnonce']/div[8]/div[3]"))
                .getText());

        // Les entreprises qui vous proposent des devis
        Assert.assertFalse(driver
                .findElement(
                        By.cssSelector("#containerEnteprisesInscrites > div.row-fluid > div.span12 > div.bg_title > h2.headInModule"))
                .isDisplayed());
        assertEquals("Aucune photo du chantier pour le moment :(", driver.findElement(By.id("aucunePhoto")).getText());
    }

    /**
     * Cas de test : L'admin essaye d'acceder a une annonce en se connectant a
     * les castors, il doit y arriver et tout doit s'afficher
     * 
     */
    @Test
    public void testAnnonceAffichageWithAdmin() {
        connectAndGoToAnnonce(TypeCompte.ADMINISTRATEUR, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        assertEquals(
                "ENTREPRISES QUI SOUHAITENT VOUS CONTACTER",
                driver.findElement(
                        By.cssSelector("#containerEnteprisesInscrites > div.row-fluid > div.span12 > div.bg_title > h2.headInModule"))
                        .getText());
        assertEquals("Modifier votre annonce", driver.findElement(By.linkText("Modifier votre annonce")).getText());
        assertEquals("Supprimer l'annonce", driver.findElement(By.id("supprimerAnnonce")).getText());

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("containerContactMaster")));
        assertNotNull(checkConditionAnnoncePresent);
        assertEquals(
                "ENTREPRISES QUI SOUHAITENT VOUS CONTACTER",
                driver.findElement(
                        By.cssSelector("#containerEnteprisesInscrites > div.row-fluid > div.span12 > div.bg_title > h2.headInModule"))
                        .getText());
        assertEquals("Aucune photo du chantier pour le moment :(", driver.findElement(By.id("aucunePhoto")).getText());
    }

    /**
     * Cas de test : L'utilisateur essaye d'acceder à l'annonce mais n'y arrive
     * pas car il n'est pas connecté.
     */
    @Test
    public void testAnnonceAffichageRefuseAccessCauseNotConnected() {
        StringBuilder appUrlAnnonce = new StringBuilder(appUrl);
        appUrlAnnonce.append(UrlPage.ANNONCE).append("?idAnnonce=").append("toto");
        driver.get(appUrlAnnonce.toString());
        assertEquals("OOPS! VOUS N'AVEZ PAS LE DROIT D'ETRE ICI :(",
                driver.findElement(By.cssSelector("h1.titleAccessDenied")).getText());
    }

    /**
     * Cas de test : L'utilisateur essaye d'acceder à l'annonce mais n'y arrive
     * pas car il n'est pas connecté.
     */
    @Test
    public void testAnnonceAffichageRefuseAccessCauseNotOwner() {
        driver.get(appUrl);
        connexionApplication("xavier", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        StringBuilder appUrlAnnonce = new StringBuilder(appUrl);
        appUrlAnnonce.append(UrlPage.ANNONCE).append("?idAnnonce=").append("toto");
        driver.get(appUrlAnnonce.toString());
        assertEquals("OOPS! VOUS N'AVEZ PAS LE DROIT D'ETRE ICI :(",
                driver.findElement(By.cssSelector("h1.titleAccessDenied")).getText());
    }

    /**
     * Cas de test : L'utilisateur se rend sur son annonce puis la supprime : la
     * suppression doit se passer correctement
     */
    @Test
    public void testSuppressionAnnonceByClient() {
        connectAndGoToAnnonce(TypeCompte.CLIENT, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        driver.findElement(By.id("supprimerAnnonce")).click();

        WebElement yesModalSuppression = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("yes")));

        driver.findElement(By.id("yes")).click();
        assertEquals("Votre annonce a bien été supprimée", driver.findElement(By.cssSelector("span.box_type4"))
                .getText());

    }

    /**
     * Cas de test : L'admin se rend sur l'annonce puis la supprime : la
     * suppression doit se passer correctement
     */
    @Test
    public void testSuppressionAnnonceByAdmin() {
        connectAndGoToAnnonce(TypeCompte.ADMINISTRATEUR, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        driver.findElement(By.id("supprimerAnnonce")).click();

        WebElement yesModalSuppression = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("yes")));

        driver.findElement(By.id("yes")).click();
        // TODO Verifier que l'on a bien redirigé l'admin sur la bonne page
        // quand la page d'accueil admin sera faites
    }

    /**
     * Cas de test : L'utilisateur choisi un entreprise qui s'est inscrit a son
     * annonce.
     */
    @Test
    public void testChoixEntrepriseAnnonceByClient() throws InterruptedException {
        testSelectionEntreprise(TypeCompte.CLIENT);
    }

    /**
     * Cas de test : L'utilisateur choisi un entreprise qui s'est inscrit a son
     * annonce.
     */
    @Test
    public void testChoixEntrepriseAnnonceByAdmin() throws InterruptedException {
        testSelectionEntreprise(TypeCompte.ADMINISTRATEUR);
    }

    /**
     * Cas de test : Un artisan s'inscrit à l'annonce, tout se passe comme prévu
     */
    @Test
    public void testInscriptionArtisanAnnonce() {
        connectAndGoToAnnonce(TypeCompte.ARTISAN, "lolxd");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        driver.findElement(By.id("inscrireAnnonce")).click();

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By
                        .cssSelector("#inscriptionModal > div.modal-header > #myModalLabel")));
        assertNotNull(checkConditionAnnoncePresent);

        // Lien inscription artisan sur la page de l'annonce.
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[5]/div/div[2]/a[1]"))
                .click();

        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre inscription a été prise en compte avec succés"));
        assertTrue(checkCondition);

        WebElement checkElementEnvoyerDevisPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Envoyer votre devis")));
        assertNotNull(checkElementEnvoyerDevisPresent);

    }

    /**
     * Cas de test : Un client se connecte sur le site, va sur son annonce et
     * décide de supprimer une entreprise, tout se passe comme prévu
     */
    @Test
    public void testDesInscriptionArtisanByClientAnnonce() {
        testDesinscriptionEntreprise(TypeCompte.CLIENT);
    }

    /**
     * Cas de test : Un admin se connecte sur le site, va sur une annonce et
     * décide de supprimer une entreprise de cette dernière, tout se passe comme
     * prévu
     */
    @Test
    public void testDesInscriptionArtisanByAdminAnnonce() {
        testDesinscriptionEntreprise(TypeCompte.ADMINISTRATEUR);
    }

    /**
     * Cas de test : Un client se connecte sur le site, va sur une annonce et et
     * note l'artisan qu'il a selectionné auparavant, tout se passe comme prévu
     * 
     * @throws InterruptedException
     */
    @Test
    public void testNotationArtisanByClient() throws InterruptedException {
        testNotation(TypeCompte.CLIENT);
    }

    /**
     * Cas de test : Un administrateur se connecte sur le site, va sur une
     * annonce et note l'artisan qui a été selectionné auparavant par le client,
     * tout se passe comme prévu
     * 
     * @throws InterruptedException
     */
    @Test
    public void testNotationArtisanByAdmin() throws InterruptedException {
        testNotation(TypeCompte.ADMINISTRATEUR);
    }

    private void testNotation(TypeCompte typeCompte) throws InterruptedException {
        connectAndGoToAnnonce(typeCompte, "lolmdrxD");
        assertCoreInformationOfAnnonce(EtatAnnonce.A_NOTER);

        // Lien "notez artisan"
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[4]/div/div/div/div[2]/div[2]/div/div[3]/a"))
                .click();

        WebElement checkElementEnvoyerDevisPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("notationArtisanModal")));
        assertNotNull(checkElementEnvoyerDevisPresent);

        // Clique sur la première étoile
        driver.findElement(By.className("rating-symbol")).click();

        driver.findElement(By.id("textAreaCommentaireNotation")).clear();
        driver.findElement(By.id("textAreaCommentaireNotation")).sendKeys("moyen moyen le pébron !!");

        // Envoyer la notation
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[6]/div/div/div[2]/form/div[4]/div/a"))
                .click();

        WebElement checkFeedbackPanelNotationOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.box_type4")));
        assertNotNull(checkFeedbackPanelNotationOK);

        assertEquals(EtatAnnonce.TERMINER.getType(), driver.findElement(By.id("etatAnnonce")).getText());
    }

    public void connectAndGoToAnnonce(TypeCompte typeCompteWanted, String idAnnonce) {
        driver.get(appUrl);
        // On s'authentifie à l'application
        if (typeCompteWanted.equals(TypeCompte.CLIENT)) {
            connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        } else if (typeCompteWanted.equals(TypeCompte.ARTISAN)) {
            connexionApplication("pebron", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        } else {
            connexionApplication("admin", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        }
        driver.findElement(By.id("connexionlbl")).click();

        // On calcul l'url d'accées direct à l'annonce.
        StringBuilder appUrlAnnonce = new StringBuilder(appUrl);
        appUrlAnnonce.append(UrlPage.ANNONCE).append("?idAnnonce=").append(idAnnonce);
        driver.get(appUrlAnnonce.toString());
    }

    /**
     * Vérifie les informations principales de l'annonce
     */
    public void assertCoreInformationOfAnnonce(EtatAnnonce etatAnnonce) {
        Boolean checkSousCategoriePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("sousCategorieLabel"),
                        "Installation électrique"));
        assertTrue(checkSousCategoriePresent);

        assertEquals("Construction compliqué qui necessite des connaissance en geologie",
                driver.findElement(By.cssSelector("div.span7 > div")).getText());
        assertEquals("Electricité", driver.findElement(By.cssSelector("div.labelAnnonce")).getText());
        assertEquals("Neuf", driver.findElement(By.cssSelector("div.informationAnnonce")).getText());
        assertEquals("Le plus rapidement possible",
                driver.findElement(By.xpath("//div[@id='containerInformationsAnnonce']/div[4]/div[3]")).getText());
        assertEquals("Email", driver.findElement(By.xpath("//div[@id='containerInformationsAnnonce']/div[5]/div[3]"))
                .getText());
        assertEquals(etatAnnonce.getType(),
                driver.findElement(By.xpath("//div[@id='containerInformationsAnnonce']/div[6]/div[3]")).getText());
        assertEquals("10/01/2014",
                driver.findElement(By.xpath("//div[@id='containerInformationsAnnonce']/div[7]/div[3]")).getText());
    }

    private void testDesinscriptionEntreprise(TypeCompte typeCompte) {
        connectAndGoToAnnonce(typeCompte, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        // Le lien de selection de la premiere entreprise
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[4]/div/div/div/div[2]/div[2]/div[3]/a[2]"))
                .click();

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.visibilityOfElementLocated(By
                        .cssSelector("#desincriptionArtisanModal > div.modal-header > #myModalLabel")));
        assertNotNull(checkConditionAnnoncePresent);

        // Le bouton oui de la modal
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[7]/div/div[2]/a[1]"))
                .click();

        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "L'entreprise a été desinscrite avec succés"));

        assertTrue(checkCondition);
    }

    private void testSelectionEntreprise(TypeCompte typeCompte) throws InterruptedException {
        connectAndGoToAnnonce(typeCompte, "toto");
        assertCoreInformationOfAnnonce(EtatAnnonce.ACTIVE);
        // Le lien de selection de la premiere entreprise
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[2]/div[4]/div/div/div/div[2]/div[2]/div[3]/a[1]"))
                .click();

        WebElement checkConditionAnnoncePresent = new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX)
                .until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[6]/div/div[2]/a[1]")));
        assertNotNull(checkConditionAnnoncePresent);

        // Le bouton oui de la modal
        driver.findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[6]/div/div[2]/a[1]"))
                .click();

        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "L'entreprise Entreprise de toto a été selectionnée avec succés"));

        assertTrue(checkCondition);
        assertEquals(
                "ENTREPRISE QUE VOUS AVEZ CHOISI",
                driver.findElement(
                        By.cssSelector("#containerEntrepriseSelectionnee > div.row-fluid > div.span12 > div.bg_title > h2.headInModule"))
                        .getText());
    }

}