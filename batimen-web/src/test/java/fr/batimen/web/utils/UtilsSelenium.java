package fr.batimen.web.utils;

import fr.batimen.web.selenium.client.TestModificationAnnonce;
import fr.batimen.web.selenium.common.AbstractITTest;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

/**
 * Created by Casaucau on 23/06/2015.
 */
public class UtilsSelenium {

    public static void etape1(WebDriver driver) {
        driver.findElement(By.id("codePostal")).clear();
        driver.findElement(By.id("codePostal")).sendKeys("06700");
        driver.findElement(By.id("valideCodePostal")).click();
    }

    public static void testAjoutPhotoIT(WebDriver driver, String browser, boolean isAnnonceModification) throws InterruptedException {
        if(isAnnonceModification){
            driver.findElement(By.linkText("Modifier votre annonce")).click();
        } else {
            driver.findElement(By.linkText("Modifier mes informations")).click();
        }


        ClassLoader classLoader = TestModificationAnnonce.class.getClassLoader();
        File file = new File(classLoader.getResource("img/castor.jpg").getFile());

        WebElement photoField = driver.findElement(By.id("photoField"));
        photoField.sendKeys(file.getAbsolutePath());

        if (browser.equals("ie")) {
            Thread.sleep(2000);
        }

        driver.findElement(By.id("envoyerPhotos")).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Photo(s) rajoutée(s) avec succés"));
        Assert.assertTrue(checkUntilModifOK);
    }

    public static void suppressionPhotoIT(WebDriver driver){
        //Clique sur le bouton supprimer la photo
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/div[1]/div[1]/div/div[3]/div[2]/div/div[1]/div/div/div[2]/div[1]/div/div/a/div")).click();

        Boolean checkUntilModifOK = (new WebDriverWait(driver, AbstractITTest.TEMPS_ATTENTE_AJAX))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("span.box_type4"),
                        "Suppression effectuée !"));
        Assert.assertTrue(checkUntilModifOK);
    }
}
