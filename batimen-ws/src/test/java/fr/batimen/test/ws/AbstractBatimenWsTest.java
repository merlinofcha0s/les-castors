package fr.batimen.test.ws;

import javax.inject.Inject;

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

    @Inject
    private WsConnector wsConnector;

    @Deployment
    public static WebArchive createTestArchive() {

        MavenResolverSystem resolver = Maven.resolver();

        // Creation de l'archive
        WebArchive batimenWsTest = ShrinkWrap.create(WebArchive.class, "batimen-ws-test.war");

        // Ajout des dépendences
        batimenWsTest
                .addPackages(true, "fr/batimen/ws/dao")
                .addPackages(true, "fr/batimen/ws/entity")
                .addPackages(true, "fr/batimen/ws/enums")
                .addPackages(true, "fr/batimen/ws/facade")
                .addPackages(true, "fr/batimen/ws/helper")
                .addPackages(true, "fr/batimen/ws/interceptor")
                .addPackages(true, "fr/batimen/ws/quartz")
                .addPackages(true, "fr/batimen/ws/service")
                .addPackages(true, "fr/batimen/ws/utils")
                .addPackages(true, "fr/batimen/ws/quartz")
                .addPackages(true, "fr/batimen/test/ws")
                .addAsLibraries(
                        resolver.loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies()
                                .resolve("fr.batimen.app:batimen-client").withTransitivity().asFile());
        // Seul dependence a specifier car elle ne fait pas partie du
        // pom ws
        //

        // Ajout des ressources
        batimenWsTest.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("logback-test.xml", "classes/logback.xml")
                .addAsWebInfResource("glassfish-web.xml", "glassfish-web.xml").setWebXML("web.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("email-test.properties", "email.properties")
                .addAsResource("url.properties", "url.properties")
                .addAsResource("castor.properties", "castor.properties")
                .addAsResource("jobs.properties", "jobs.properties")
                .addAsResource("quartz.properties", "quartz.properties");

        return batimenWsTest;
    }

    @Rule
    public TestRule watchman = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOGGER.info("Début Test : " + description.getDisplayName());
            wsConnector.setTest(true);
        }

        @Override
        protected void finished(Description description) {
            LOGGER.info("Fin Test : " + description.getDisplayName());
        };
    };

}
