package fr.castor.web.selenium;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.enums.TypeNotification;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.web.selenium.common.AbstractITTest;
import fr.castor.web.selenium.dataset.MesAnnoncesDataset;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test Selenium concernant la page mes annonces.
 *
 * @author Casaucau Cyril.
 */
public class TestMesAnnonces extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = Operations.sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                MesAnnoncesDataset.INSERT_ADRESSE_DATA, MesAnnoncesDataset.INSERT_ENTREPRISE_DATA,
                MesAnnoncesDataset.INSERT_ARTISAN_DATA, MesAnnoncesDataset.INSERT_ARTISAN_PERMISSION, MesAnnoncesDataset.INSERT_AVIS_DATA,
                MesAnnoncesDataset.INSERT_ANNONCE_DATA, MesAnnoncesDataset.INSERT_NOTIFICATION_DATA, MesAnnoncesDataset.INSERT_ANNONCE_ARTISAN, MesAnnoncesDataset.INSERT_ANNONCE_MOT_CLE, MesAnnoncesDataset.INSERT_CATEGORIE_METIER);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : l'utilisateur se rend sur le site, se connecte et va dans
     * la rubrique mon compte. La, il tombe sur ses notifications ainsi que sur
     * ces annonces postées. Enfin il essaye d'accéder à une de ces annonces via
     * cette page
     */
    @Test
    public void testAccessToMesAnnonceByClient() {
        testMesAnnonces("raiden", TypeNotification.INSCRIT_A_ANNONCE, TypeCompte.CLIENT, 4, 4, 1);

        findElement(
                By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div/div[2]/div[3]/div/div[1]/table/tbody/tr[1]/td[2]/a[2]"))
                .click();

        Boolean checkConditionAccessToAnnonceViaNotif = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1.title"),
                        "ANNONCE PARTICULIER"));
        assertTrue(checkConditionAccessToAnnonceViaNotif);

    }

    /**
     * Cas de test : l'artisan se rend sur le site, se connecte et va dans
     * la rubrique mon compte. La, il tombe sur ses notifications ainsi que sur
     * ces annonces où il s'est inscrit. Enfin il essaye d'accéder à une de ces annonces via
     * cette page
     */
    @Test
    public void testAccessToMesAnnonceByArtisan() {
        testMesAnnonces("pebron", TypeNotification.A_CHOISI_ENTREPRISE, TypeCompte.ARTISAN, 1, 1, 4);
    }

    private void testMesAnnonces(String login, TypeNotification typeNotification, TypeCompte typeCompte, int nbAnnoncePagine, int nbAnnonceTotale, int nbTotalNotification) {
        driver.get(appUrl);
        // On s'authentifie à l'application
        connexionApplication(login, AbstractITTest.BON_MOT_DE_PASSE, Boolean.FALSE);

        findElement(By.id("connexionlbl")).click();

        Boolean checkConditionNotificationPresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//td[2]/span"),
                        typeNotification.getAffichage()));
        assertTrue(checkConditionNotificationPresent);

        Boolean checkConditionAnnonceDescription = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(
                        By.xpath("//div[2]/table/tbody/tr/td[1]/span"), "Construction compliqué qui nec..."));
        assertTrue(checkConditionAnnonceDescription);

        WebElement checkConditionAnnoncePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h4.headInModule")));
        assertNotNull(checkConditionAnnoncePresent);

        //S'il y a assez de notification pour activer la pagination
        if (nbTotalNotification > MesAnnonces.NB_NOTIFICATION_PAR_PAGE) {
            //Vérification de la pagination des notifications
            findElement(By.id("afficherAnciennesNotifications")).click();
            WebElement checkConditionNotificationPaginePresent = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='notificationsContainer']/tbody/tr[4]/td[2]")));
            assertNotNull(checkConditionNotificationPaginePresent);
        }

        //S'il y a assez d'annonce pour activer la pagination
        if (nbAnnonceTotale > MesAnnonces.NB_ANNONCE_PAR_PAGE) {
            //Vérification de la pagination des annonces
            findElement(By.id("afficherAnciennesAnnonces")).click();

            StringBuilder texteVerificationPagination = new StringBuilder();
            texteVerificationPagination.append(nbAnnoncePagine);
            texteVerificationPagination.append(" annonce(s) affichée(s) sur ").append(nbAnnonceTotale);

            Boolean checkPagination = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                    .until(ExpectedConditions.textToBePresentInElementLocated(By.id("infosNbAnnonce"),
                            texteVerificationPagination.toString()));
            assertTrue(checkPagination);
        }


        if (typeCompte.equals(TypeCompte.CLIENT)) {
            findElement(
                    By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div/div[2]/div[3]/div/div[2]/table/tbody/tr[1]"))
                    .click();
        } else if (typeCompte.equals(TypeCompte.ARTISAN)) {
            findElement(
                    By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div/div[2]/div[3]/div/div[2]/table/tbody/tr"))
                    .click();
        }


        WebElement checkConditionAccessToAnnonceViaList = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.title")));
        assertNotNull(checkConditionAccessToAnnonceViaList);

        findElement(By.id("connexionlbl")).click();
    }

}
