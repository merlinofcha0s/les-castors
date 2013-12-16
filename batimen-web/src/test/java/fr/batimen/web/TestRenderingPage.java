package fr.batimen.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.web.app.BatimenApplication;
import fr.batimen.web.client.panel.Accueil;
import fr.batimen.web.client.panel.Contact;
import fr.batimen.web.client.panel.QuiSommeNous;
import fr.batimen.web.client.panel.authentification.Authentification;

public class TestRenderingPage {

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

	@Test
	public void accueilRendersSuccessfully() {
		// start and render the test page
		tester.startPage(Accueil.class);
		// assert rendered page class
		tester.assertRenderedPage(Accueil.class);
	}

	@Test
	public void contactRendersSuccessfully() {
		// start and render the test page
		tester.startPage(Contact.class);
		// assert rendered page class
		tester.assertRenderedPage(Contact.class);
	}

	@Test
	public void quiSommesNousRendersSuccessfully() {
		// start and render the test page
		tester.startPage(QuiSommeNous.class);
		// assert rendered page class
		tester.assertRenderedPage(QuiSommeNous.class);
	}

}
