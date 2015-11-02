package fr.batimen.web.selenium.client;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.core.constant.UrlPage;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertNotNull;

public class TestActivationCompte extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur essaie d'activer un compte avec une clé
     * erronée, l'application lui affiche un message d'erreur.
     */
    @Test
    public void testActivationWithNoValidKey() {
        driver.get(appUrl + UrlPage.ACTIVATION_URL + "?key=lolmdrxd06");

        WebElement checkConditionActivationImageKO = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.confirmationactivationko")));
        assertNotNull(checkConditionActivationImageKO);

        // On s'assure que le container de premiere ligne est présent
        WebElement checkConditionActivationlabel1 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[1]")));
        assertNotNull(checkConditionActivationlabel1);

        // On s'assure que le texte est correct pour la premiere ligne
        Assert.assertEquals("CLÉ D'ACTIVATION INCORRECTE", checkConditionActivationlabel1.getText());

        // On s'assure que le container de la deuxieme ligne est présent
        WebElement checkConditionActivationlabel2 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[2]")));
        assertNotNull(checkConditionActivationlabel2);

        // On s'assure que le texte est correct pour la deuxieme ligne
        Assert.assertEquals("VEUILLEZ SUIVRE LE LIEN CONTENU DANS LE MAIL DE CONFIRMATION D'INSCRIPTION",
                checkConditionActivationlabel2.getText());
    }

    /**
     * Cas de test : L'utilisateur essaie d'activer plusieurs fois son compte,
     * l'application doit lui afficher un message d'erreur
     */
    @Test
    public void testActivationTwoTimes() {
        driver.get(appUrl + UrlPage.ACTIVATION_URL + "?key=lolmdr06");
        driver.get(appUrl + UrlPage.ACTIVATION_URL + "?key=lolmdr06");

        WebElement checkConditionActivationImageKO = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.confirmationactivationko")));
        assertNotNull(checkConditionActivationImageKO);

        // On s'assure que le container de premiere ligne est présent
        WebElement checkConditionActivationlabel1 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[1]")));
        assertNotNull(checkConditionActivationlabel1);

        // On s'assure que le texte est correct pour la premiere ligne
        Assert.assertEquals("VOTRE COMPTE EST DÉJÀ ACTIF", checkConditionActivationlabel1.getText());

    }

    /**
     * Cas de test : L'utilisateur clique sur le lien qui se trouve dans l'email
     * envoyé et active son compte avec succés
     */
    @Test
    public void testActivationOK() {
        driver.get(appUrl + UrlPage.ACTIVATION_URL + "?key=lolmdr201");

        WebElement checkConditionActivationImageOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.confirmationactivationok")));
        assertNotNull(checkConditionActivationImageOK);

        // On s'assure que le texte est correct pour la premiere ligne
        WebElement checkConditionActivationlabel1 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[1]")));
        assertNotNull(checkConditionActivationlabel1);

        // On s'assure que le texte est correct pour la premiere ligne
        Assert.assertEquals("VOTRE COMPTE EST ACTIVÉ !!", checkConditionActivationlabel1.getText());

        WebElement checkConditionActivationlabel2 = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='activation']/h3[2]")));
        assertNotNull(checkConditionActivationlabel2);

        // On s'assure que le texte est correct pour la premiere ligne
        Assert.assertEquals("VOUS POUVEZ MAINTENANT VOUS CONNECTER À NOTRE ESPACE MEMBRE",
                checkConditionActivationlabel2.getText());

    }
}
