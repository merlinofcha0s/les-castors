package fr.castor.web.app;

import fr.castor.core.constant.UrlPage;
import fr.castor.web.app.enums.PropertiesFileWeb;
import fr.castor.web.client.extend.*;
import fr.castor.web.client.extend.activation.Activation;
import fr.castor.web.client.extend.connected.Annonce;
import fr.castor.web.client.extend.connected.Entreprise;
import fr.castor.web.client.extend.connected.RechercheAnnonce;
import fr.castor.web.client.extend.error.AccesInterdit;
import fr.castor.web.client.extend.error.ErreurInterne;
import fr.castor.web.client.extend.error.Expiree;
import fr.castor.web.client.extend.error.NonTrouvee;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.web.client.extend.member.client.ModifierAnnonce;
import fr.castor.web.client.extend.member.client.ModifierMonProfil;
import fr.castor.web.client.extend.member.client.MonProfil;
import fr.castor.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.castor.web.client.extend.nouveau.devis.NouveauDevis;
import fr.castor.web.client.session.BatimenSession;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.cdi.ConversationPropagation;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;
import org.wicketstuff.javaee.naming.global.ModuleJndiNamingStrategy;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Classe principale de l'application
 * 
 * @author Casaucau Cyril
 */
public class CastorApplication extends AuthenticatedWebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastorApplication.class);
    private boolean setStripWicketTags;
    private boolean activateResourceCaching;

    private void getAppProperties() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Récuperation des properties....");
        }

        Properties appProperties = PropertiesFileWeb.APP.getProperties();
        setStripWicketTags = Boolean.valueOf(appProperties.getProperty("app.setStripWicketTags"));
        activateResourceCaching = Boolean.valueOf(appProperties.getProperty("app.activate.cache.resources"));
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return Accueil.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return BatimenSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return Accueil.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        getComponentInstantiationListeners().add(new JavaEEComponentInjector(this, new ModuleJndiNamingStrategy()));
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
                new UrlResourceReference(Url.parse("//code.jquery.com/jquery-1.11.2.min.js")));

        /*getResourceSettings().setCssCompressor(new CssUrlReplacer());
        getResourceSettings().setJavaScriptCompressor(new DefaultJavaScriptCompressor());
        getResourceSettings().setUseMinifiedResources(true);

        if (activateResourceCaching) {
            IResourceCachingStrategy strategy = new FilenameWithVersionResourceCachingStrategy(
                    new LastModifiedResourceVersion());
            getResourceSettings().setCachingStrategy(strategy);
            getResourceSettings().setDefaultCacheDuration(Duration.minutes(120));
        }*/

        /*getStoreSettings().setMaxSizePerSession(Bytes.megabytes(2));
        //Nombre de page max en mémoire
        getStoreSettings().setInmemoryCacheSize(50);*/

        // Cfg urls des pages principales
        mountPage(UrlPage.ACCUEIL_URL, Accueil.class);
        mountPage(UrlPage.MES_ANNONCES_URL, MesAnnonces.class);
        mountPage(UrlPage.QUI_SOMMES_NOUS_URL, QuiSommeNous.class);
        mountPage(UrlPage.CONTACT_URL, Contact.class);
        mountPage(UrlPage.CGU_URL, CGU.class);
        mountPage(UrlPage.ACTIVATION_URL, Activation.class);
        mountPage(UrlPage.PARTENAIRE_URL, NouveauArtisan.class);
        mountPage(UrlPage.NOUVEAU_DEVIS_URL, NouveauDevis.class);
        mountPage(UrlPage.MODIFIER_MON_PROFIL, ModifierMonProfil.class);
        mount(new MountedMapper(UrlPage.MON_PROFIL_URL, MonProfil.class, new UrlPathPageParametersEncoder()));
        mountPage(UrlPage.ANNONCE, Annonce.class);
        mount(new MountedMapper(UrlPage.MODIFIER_MON_ANNONCE, ModifierAnnonce.class, new UrlPathPageParametersEncoder()));
        mountPage(UrlPage.ENTREPRISE, Entreprise.class);
        mountPage(UrlPage.RECHERCHE_ANNONCE, RechercheAnnonce.class);
        mountPage(UrlPage.FAQ, FAQ.class);
        mountPage(UrlPage.NOS_OBJECTIFS, Objectif.class);
        // Page d'erreur
        mountPage("/interdit", AccesInterdit.class);
        mountPage("/expiree", Expiree.class);
        mountPage("/404", NonTrouvee.class);

        // Config des pages d'erreurs
        getApplicationSettings().setInternalErrorPage(ErreurInterne.class);
        getApplicationSettings().setAccessDeniedPage(AccesInterdit.class);
        getApplicationSettings().setPageExpiredErrorPage(Expiree.class);

        // Init de CDI (Wicket)
        BeanManager manager = null;
        try {
            // Recuperation du context via le serveur d'application
            manager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(
                        "Impossible de récuperer le context bean manager : java:comp/BeanManager pour l'init de CDI", e);
            }
        }

        // Configuration de CDI en enlevant le mode conversation
        new CdiConfiguration(manager).setPropagation(ConversationPropagation.NONE).configure(this);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la Web app.....OK");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.
     * wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new BatimenSession(request);
    }
}