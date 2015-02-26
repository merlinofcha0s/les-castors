package fr.batimen.test.ws;

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
        batimenWsTest.addPackages(true, "fr/batimen/ws").addPackages(true, "fr/batimen/test/ws")
                .addAsLibraries(resolver.loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies()
                // Seul dependence a specifier car elle ne fait pas partie du
                // pom ws
                        .resolve("fr.batimen.app:batimen-client").withTransitivity().asFile());

        // Ajout des ressources
        batimenWsTest.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("logback-test.xml", "classes/logback.xml")
                .addAsWebInfResource("glassfish-web.xml", "glassfish-web.xml").setWebXML("web.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("email-test.properties", "email.properties")
                .addAsResource("url.properties", "url.properties")
                .addAsResource("castor.properties", "castor.properties").addAsResource("jobs.xml", "jobs.xml")
                .addAsResource("quartz.properties", "quartz.properties");

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
