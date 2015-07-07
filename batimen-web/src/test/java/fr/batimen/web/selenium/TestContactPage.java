package fr.batimen.web.selenium;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertTrue;

public class TestContactPage extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_DATA, INSERT_USER_PERMISSION);
        DbSetup dbSetup = new DbSetup(getDriverManagerDestination(), operation);
        dbSetup.launch();
    }

    @Test
    public void contactSuccess() throws Exception {
        driver.get(appUrl + "/contact");
        driver.findElement(By.name("p::name")).clear();
        driver.findElement(By.name("p::name")).sendKeys("Cyril");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("raiden0610@hotmail.fr");
        driver.findElement(By.name("subject")).clear();
        driver.findElement(By.name("subject")).sendKeys("Object");
        driver.findElement(By.name("message")).clear();
        driver.findElement(By.name("message")).sendKeys("Messajation");
        driver.findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre message a été transmis correctement. Nous vous répondrons dans les plus brefs délais."));
        assertTrue(checkCondition);
    }

    @Test
    public void contactSuccessWhenLoggedIn() throws Exception {
        driver.get(appUrl);
        connexionApplication("raiden", BON_MOT_DE_PASSE, true);

        driver.get(appUrl + "/contact");
        driver.findElement(By.name("subject")).clear();
        driver.findElement(By.name("subject")).sendKeys("Object");
        driver.findElement(By.name("message")).clear();
        driver.findElement(By.name("message")).sendKeys("Messajation");
        driver.findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre message a été transmis correctement. Nous vous répondrons dans les plus brefs délais."));
        assertTrue(checkCondition);
    }

    @Test
    public void contactFail() throws Exception {
        driver.get(appUrl + "/contact");
        driver.findElement(By.name("p::name")).clear();
        driver.findElement(By.name("p::name")).sendKeys("Adnane");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("adnane.tellou@hotmail.es");
        driver.findElement(By.name("subject")).clear();
        driver.findElement(By.name("subject")).sendKeys("Hey ho");
        driver.findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type6"),
                        "Veuillez entrer votre message !"));
        assertTrue(checkCondition);
    }
}
