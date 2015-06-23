package fr.batimen.web.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Casaucau on 23/06/2015.
 */
public class UtilsSelenium {

    public static void etape1(WebDriver driver) {
        driver.findElement(By.id("codePostal")).clear();
        driver.findElement(By.id("codePostal")).sendKeys("06700");
        driver.findElement(By.id("valideCodePostal")).click();
    }
}
