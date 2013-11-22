package fr.batimen.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.web.app.BatimenApplication;
import fr.batimen.web.client.panel.authentification.Authentification;

/**
 * Simple test using the WicketTester
 */
public class TestAuthentificationPage {
	private WicketTester tester;

	@Before
	public void setUp() {
		tester = new WicketTester(new BatimenApplication());
	}

	@Test
	public void authentificationRendersSuccessfully() {
		// start and render the test page
		tester.startPage(Authentification.class);
		// assert rendered page class
		tester.assertRenderedPage(Authentification.class);
	}
}
