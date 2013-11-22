package fr.batimen.web.selenium;

import org.junit.Test;
import org.openqa.selenium.By;

public class TestAuthentification extends AbstractSeleniumTest {

	@Test
	public void testAuthentificationSuccess() throws Exception {
		driver.get(appUrl);
		driver.findElement(By.name("login")).click();
		driver.findElement(By.name("login")).clear();
		driver.findElement(By.name("login")).sendKeys("raiden");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("lollollol");
		driver.findElement(By.id("signIn2")).click();
	}

}
