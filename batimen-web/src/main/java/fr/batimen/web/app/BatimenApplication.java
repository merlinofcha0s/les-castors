package fr.batimen.web.app;

import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.extend.MonCompte;
import fr.batimen.web.client.extend.QuiSommeNous;
import fr.batimen.web.client.extend.authentification.Authentification;
import fr.batimen.web.client.extend.error.AccesInterdit;
import fr.batimen.web.client.extend.error.ErreurInterne;
import fr.batimen.web.client.extend.error.Expiree;
import fr.batimen.web.client.extend.error.NonTrouvee;
import fr.batimen.web.client.extend.nouveaudevis.NouveauDevis;
import fr.batimen.web.client.session.BatimenSession;

/**
 * Classe principale de l'application
 * 
 * @author Casaucau Cyril
 */
public class BatimenApplication extends AuthenticatedWebApplication {

    private boolean setStripWicketTags;

    private static final Logger LOGGER = LoggerFactory.getLogger(BatimenApplication.class);

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return Accueil.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        // On récup les properties de conf de la web app
        getAppProperties();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut init de la Web app.....");
        }

        // Config HTML
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripWicketTags(setStripWicketTags);

        // Activation du CSRF (Cross site request forgery)
        /*
         * IRequestMapper cryptoMapper; cryptoMapper = new
         * CryptoMapper(getRootRequestMapper(), this);
         * setRootRequestMapper(cryptoMapper);
         */

        // Config Jquery
        // Pas de http pour éviter le content mix blocking dans le browser
        // (appel d'une url http dans une page https)
        getJavaScriptLibrarySettings().setJQueryReference(
                new UrlResourceReference(Url.parse("//code.jquery.com/jquery-1.9.1.min.js")));
        // Chargement de Jquery-ui avec le theme Smoothness
        addResourceReplacement(WiQueryCoreThemeResourceReference.get(), new WiQueryCoreThemeResourceReference(
                "smoothness"));

        // Cfg urls des pages principales
        mountPage(Constant.ACCUEIL_URL, Accueil.class);
        mountPage(Constant.AUTHENTIFICATION_URL, Authentification.class);
        mountPage(Constant.MON_COMPTE_URL, MonCompte.class);
        mountPage(Constant.QUI_SOMMES_NOUS_URL, QuiSommeNous.class);
        mountPage(Constant.CONTACT_URL, Contact.class);
        mountPage(Constant.CGU_URL, CGU.class);
        mountPage("/404", NonTrouvee.class);
        // Encode la page de cette maniere : nouveaudevis/departement/06
        mount(new MountedMapper(Constant.NOUVEAU_DEVIS_URL, NouveauDevis.class, new UrlPathPageParametersEncoder()));

        // Config des pages d'erreurs
        getApplicationSettings().setInternalErrorPage(ErreurInterne.class);
        getApplicationSettings().setAccessDeniedPage(AccesInterdit.class);
        getApplicationSettings().setPageExpiredErrorPage(Expiree.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la Web app.....OK");
        }

    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return Authentification.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return BatimenSession.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.authroles.authentication.AuthenticatedWebApplication
     * #newSession(org.apache.wicket.request.Request,
     * org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new BatimenSession(request);
    }

    private void getAppProperties() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation des properties....");
        }
        Properties appProperties = new Properties();
        try {
            appProperties.load(getClass().getClassLoader().getResourceAsStream("app.properties"));
            setStripWicketTags = Boolean.valueOf(appProperties.getProperty("app.setStripWicketTags"));
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur de récupération des properties de la web app", e);
            }
        }
    }

}
