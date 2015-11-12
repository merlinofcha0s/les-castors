package fr.castor.web.selenium.statique;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Test d'intégration de la page nos objectifs
 */
public class TestNosObjectifs extends AbstractITTest {
    @Override
    public void prepareDB() throws Exception {

    }

    @Test
    public void testNominalNosObjectifs(){
        driver.get(appUrl + UrlPage.ACCUEIL_URL);
        findElement(By.linkText("Nos Objectifs")).click();
        Boolean checkCondition = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1.title"),
                        "NOS OBJECTIFS"));
        Assert.assertTrue(checkCondition);
    }
}