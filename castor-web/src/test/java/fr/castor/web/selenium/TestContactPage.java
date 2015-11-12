package fr.castor.web.selenium;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;
import fr.castor.web.selenium.common.AbstractITTest;
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
        findElement(By.name("formContainerContact:name")).clear();
        findElement(By.name("formContainerContact:name")).sendKeys("Cyril");
        findElement(By.name("formContainerContact:email")).clear();
        findElement(By.name("formContainerContact:email")).sendKeys("raiden0610@hotmail.fr");
        findElement(By.name("formContainerContact:subject")).clear();
        findElement(By.name("formContainerContact:subject")).sendKeys("Object");
        findElement(By.name("formContainerContact:message")).clear();
        findElement(By.name("formContainerContact:message")).sendKeys("Messajation");
        findElement(By.id("submit2")).click();
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
        findElement(By.name("formContainerContact:subject")).clear();
        findElement(By.name("formContainerContact:subject")).sendKeys("Object");
        findElement(By.name("formContainerContact:message")).clear();
        findElement(By.name("formContainerContact:message")).sendKeys("Messajation");
        findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre message a été transmis correctement. Nous vous répondrons dans les plus brefs délais."));
        assertTrue(checkCondition);
    }

    @Test
    public void contactFail() throws Exception {
        driver.get(appUrl + "/contact");
        findElement(By.name("formContainerContact:name")).clear();
        findElement(By.name("formContainerContact:name")).sendKeys("Adnane");
        findElement(By.name("formContainerContact:email")).clear();
        findElement(By.name("formContainerContact:email")).sendKeys("adnane.tellou@hotmail.es");
        findElement(By.name("formContainerContact:subject")).clear();
        findElement(By.name("formContainerContact:subject")).sendKeys("Hey ho");
        findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type6"),
                        "Veuillez entrer votre message !"));
        assertTrue(checkCondition);
    }
}
