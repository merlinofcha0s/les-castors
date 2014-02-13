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
		connexionApplication();
		Boolean checkCondition = (new WebDriverWait(driver, 5)).until(ExpectedConditions
				.textToBePresentInElementLocated(By.id("connexionlbl"), "Mon Compte"));
		assertTrue(checkCondition);
	}
}
