package fr.batimen.web.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestAuthentification extends AbstractSeleniumTest {

	@Test
	public void testAuthentificationSuccess() throws Exception {
		driver.get(appUrl);
		driver.findElement(By.id("connexionlbl")).click();
		driver.findElement(By.name("login")).click();
		driver.findElement(By.name("login")).clear();
		driver.findElement(By.name("login")).sendKeys("raiden");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("lollollol");
		driver.findElement(By.id("signInButton")).click();
		Boolean checkCondition = (new WebDriverWait(driver, 5)).until(ExpectedConditions
				.textToBePresentInElementLocated(By.id("connexionlbl"), "Mon Compte"));
		assertTrue(checkCondition);
	}
}
