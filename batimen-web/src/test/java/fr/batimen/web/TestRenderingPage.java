package fr.batimen.web;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.batimen.web.app.BatimenApplication;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.extend.QuiSommeNous;
import fr.batimen.web.client.extend.authentification.Authentification;
import fr.batimen.web.client.extend.error.AccesInterdit;
import fr.batimen.web.client.extend.error.ErreurInterne;
import fr.batimen.web.client.extend.error.Expiree;
import fr.batimen.web.client.extend.error.NonTrouvee;
import fr.batimen.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauDevis;

public class TestRenderingPage {

    private WicketTester tester;
    private ThreadState threadState;
    /**
     * The Mockito mock that will be returned by
     * {@link org.apache.shiro.SecurityUtils#getSubject()
     * SecurityUtils.getSubject()}. Use this mock in your tests to mock
     * authentication and authorization prerequisites.
     */
    protected Subject mockSubject;
    /**
     * The Mockito mock that will be returned by {@link Subject#getSession()
     * SecurityUtils.getSubject().getSession()}.
     */
    protected Session mockShiroSession;

    public static final String MOCK_PRINCIPAL = "Mr. Mock User";

    @Before
    public void attachSubject() {
        this.mockShiroSession = Mockito.mock(Session.class);
        this.mockSubject = Mockito.mock(Subject.class);
        Mockito.when(this.mockSubject.getSession()).thenReturn(this.mockShiroSession);
        this.threadState = new SubjectThreadState(this.mockSubject);
        this.threadState.bind();
        Mockito.when(this.mockSubject.isAuthenticated()).thenReturn(true);
        Mockito.when(this.mockSubject.getPrincipal()).thenReturn(MOCK_PRINCIPAL);
    }

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

    @Test
    public void nouveauPartenaireRendersSuccessfully() {
        // start and render the test page
        tester.startPage(NouveauArtisan.class);
        // assert rendered page class
        tester.assertRenderedPage(NouveauArtisan.class);
    }

    @Test
    public void erreur404Render() {
        // start and render the test page
        tester.startPage(NonTrouvee.class);
        // assert rendered page class
        tester.assertRenderedPage(NonTrouvee.class);
    }

    @Test
    public void erreur500Render() {
        // start and render the test page
        tester.startPage(ErreurInterne.class);
        // assert rendered page class
        tester.assertRenderedPage(ErreurInterne.class);
    }

    @Test
    public void erreurExpiredRender() {
        // start and render the test page
        tester.startPage(Expiree.class);
        // assert rendered page class
        tester.assertRenderedPage(Expiree.class);
    }

    @Test
    public void erreurAccesInterditRender() {
        // start and render the test page
        tester.startPage(AccesInterdit.class);
        // assert rendered page class
        tester.assertRenderedPage(AccesInterdit.class);
    }

}
