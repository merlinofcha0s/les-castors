package fr.batimen.web.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.batimen.web.selenium.common.AbstractITTest;

public class TestContactPage extends AbstractITTest {

    @Override
    public void prepareDB() throws Exception {
        // pas d'init BD
    }

    @Test
    public void contactSuccess() throws Exception {
        driver.get(appUrl + "/contact");
        driver.findElement(By.name("p::name")).clear();
        driver.findElement(By.name("p::name")).sendKeys("Adnane");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("adnane.tellou@hotmail.es");
        driver.findElement(By.name("subject")).clear();
        driver.findElement(By.name("subject")).sendKeys("Object");
        driver.findElement(By.name("message")).clear();
        driver.findElement(By.name("message")).sendKeys("Messajation");
        driver.findElement(By.id("submit2")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Votre message a été transmis correctement. Vous obtiendrez une réponse sur votre email de contact indiqué."));
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
                        "Le champ 'message' est obligatoire."));
        assertTrue(checkCondition);
    }
}
