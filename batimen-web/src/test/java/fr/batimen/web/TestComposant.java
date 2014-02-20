package fr.batimen.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.web.client.component.MapFrance;

public class TestComposant {

	private WicketTester tester;

	@Before
	public void init() {
		tester = new WicketTester();
	}

	@Test
	public void testMapFrance() {
		MapFrance france = new MapFrance("france");
		tester.startComponentInPage(france);
	}

}
