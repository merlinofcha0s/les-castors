package fr.batimen.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.web.app.BatimenApplication;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.extend.QuiSommeNous;
import fr.batimen.web.client.extend.authentification.Authentification;
import fr.batimen.web.client.extend.nouveaudevis.NouveauDevis;

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
	public void cguRendersSuccessfully() {
		// start and render the test page
		tester.startPage(CGU.class);
		// assert rendered page class
		tester.assertRenderedPage(CGU.class);
	}

	@Test
	public void quiSommesNousRendersSuccessfully() {
		// start and render the test page
		tester.startPage(QuiSommeNous.class);
		// assert rendered page class
		tester.assertRenderedPage(QuiSommeNous.class);
	}

	@Test
	public void nouveauDevisRendersSuccessfully() {
		// start and render the test page
		tester.startPage(NouveauDevis.class);
		// assert rendered page class
		tester.assertRenderedPage(NouveauDevis.class);
	}

}
