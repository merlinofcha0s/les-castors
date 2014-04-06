package fr.batimen.ws;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.ws.client.WsConnector;

@RunWith(Arquillian.class)
@ApplyScriptBefore(value = "datasets/cleanup/before.sql")
public abstract class AbstractBatimenWsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBatimenWsTest.class);

	@Deployment
	public static WebArchive createTestArchive() {

		MavenResolverSystem resolver = Maven.resolver();

		// Creation de l'archive
		WebArchive batimenWsTest = ShrinkWrap.create(WebArchive.class, "batimen-ws-test.war");

		// Ajout des dépendences
		batimenWsTest.addPackages(true, "fr/batimen/ws").addAsLibraries(
		        resolver.loadPomFromFile("pom.xml")
		                .resolve("fr.batimen.app:batimen-dto:0.1.0-SNAPSHOT",
		                        "fr.batimen.app:batimen-core:0.1.0-SNAPSHOT",
		                        "fr.batimen.app:batimen-client:0.1.0-SNAPSHOT",
		                        "org.hibernate:hibernate-core:4.2.5.Final",
		                        "org.hibernate:hibernate-entitymanager:4.2.5.Final",
		                        "com.sun.jersey:jersey-server:1.17", "com.sun.jersey:jersey-servlet:1.17",
		                        "com.sun.jersey:jersey-json:1.17", "org.modelmapper:modelmapper:0.6.1")
		                .withTransitivity().asFile());

		// Ajout des ressources
		batimenWsTest.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
		        .addAsWebInfResource("logback-test.xml", "classes/logback.xml")
		        .addAsWebInfResource("glassfish-web.xml", "glassfish-web.xml").setWebXML("web.xml")
		        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");

		return batimenWsTest;
	}

	@Rule
	public TestRule watchman = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			LOGGER.info("Début Test : " + description.getDisplayName());
			WsConnector.getInstance().setTest(true);
		}

		@Override
		protected void finished(Description description) {
			LOGGER.info("Fin Test : " + description.getDisplayName());
		};
	};

}
