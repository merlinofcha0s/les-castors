package fr.batimen.web.selenium.artisan;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.batimen.web.selenium.dataset.ModifierMonProfilDataset.*;

/**
 * Classe de cas de test concernant la consultation de la page entreprise
 *
 * @author Casaucau Cyril
 */
public class TestEntreprise extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                INSERT_ADRESSE_DATA, INSERT_ENTREPRISE_DATA,
                INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_NOTATION_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    /**
     * Cas de test : L'utilisateur (artisan) crée son compte, l'operation doit
     * être un succés
     *
     * @throws InterruptedException
     */
    @Test
    public void testInscriptionNouveauPartenaireNominal() throws InterruptedException {
        connexionApplication("raiden", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
        driver.findElement(By.linkText("Entreprise de toto")).click();
    }
}