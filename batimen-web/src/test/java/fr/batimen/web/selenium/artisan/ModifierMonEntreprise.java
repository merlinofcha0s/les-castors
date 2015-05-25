package fr.batimen.web.selenium.artisan;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static fr.batimen.web.selenium.dataset.ModifierMonProfilDataset.*;
import static fr.batimen.web.selenium.dataset.ModifierMonProfilDataset.INSERT_ANNONCE_ARTISAN;

/**
 * Test d'integration de modification des données entreprise de l'artisan
 */
public class ModifierMonEntreprise extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION,
                INSERT_ADRESSE_DATA, INSERT_ENTREPRISE_DATA,
                INSERT_ARTISAN_DATA, INSERT_ARTISAN_PERMISSION, INSERT_NOTATION_DATA,
                INSERT_ANNONCE_DATA, INSERT_NOTIFICATION_DATA, INSERT_ANNONCE_ARTISAN);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Before
    public void goToModifierMonEntreprise(){
        driver.get(appUrl);
        connexionApplication("pebron", BON_MOT_DE_PASSE, Boolean.TRUE);
        driver.findElement(By.id("connexionlbl")).click();
        driver.findElement(By.linkText("Modifier mes informations")).click();
    }

    /**
     * Cas de test : L'artisan se rend dans son espace et modifie les informations de son entreprise.
     */
    @Test
    public void modifierInformationMonEntreprise() {
        (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX)).until(ExpectedConditions
                .elementToBeClickable(By.xpath("//label[@id='containerDecorationMaconnerie']/span")));
        driver.findElement(By.xpath("//label[@id='containerDecorationMaconnerie']/span")).click();

        driver.findElement(By.id("nbEmployeField")).clear();
        driver.findElement(By.id("nbEmployeField")).sendKeys("20");
        driver.findElement(By.id("dateCreationField")).click();
        driver.findElement(By.linkText("29")).click();
        driver.findElement(By.id("codePostalField")).clear();
        driver.findElement(By.id("codePostalField")).sendKeys("06600");
        driver.findElement(By.id("villeField")).clear();
        driver.findElement(By.id("villeField")).sendKeys("Antibes city");
    }
}