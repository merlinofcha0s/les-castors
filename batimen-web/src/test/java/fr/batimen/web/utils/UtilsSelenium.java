package fr.batimen.web.utils;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UtilsSelenium {

    public static void selectionDepartement(WebDriver driver) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#mapFrance > svg")));
        } catch (Throwable error) {
            assertFalse("Timeout waiting for Page Load Request to complete.", true);
        }

        WebElement carteFrance = driver.findElement(By.cssSelector("#mapFrance > svg"));

        Actions builder = new Actions(driver);
        builder.clickAndHold(carteFrance).release(carteFrance).build().perform();
    }

}
