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
import fr.castor.web.client.session.CastorSession;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;
import org.wicketstuff.javaee.naming.global.ModuleJndiNamingStrategy;

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
        return CastorSession.class;
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

        configureCSRF();
        configureMarkupSettings();
        configureCacheAndCompression();
        configurePages();
        configureCDI();
        configureRenderingStategy();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la Web app.....OK");
        }
    }

    private void configureCSRF(){
        // Activation du CSRF (Cross site request forgery)
        /*
         * IRequestMapper cryptoMapper; cryptoMapper = new
         * CryptoMapper(getRootRequestMapper(), this);
         * setRootRequestMapper(cryptoMapper);
         */
    }

    private void configureMarkupSettings(){
        // Config HTML
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripWicketTags(setStripWicketTags);

        // Config Jquery
        // Pas de http pour éviter le content mix blocking dans le browser
        // (appel d'une url http dans une page https)
        getJavaScriptLibrarySettings().setJQueryReference(
                new UrlResourceReference(Url.parse("//code.jquery.com/jquery-1.11.2.min.js")));
    }

    private void configureCacheAndCompression(){
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
    }

    private  void configurePages(){
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
        mountPage(UrlPage.NOS_ENGAGEMENTS, NosEngagements.class);
        // Page d'erreur
        mountPage("/interdit", AccesInterdit.class);
        mountPage("/expiree", Expiree.class);
        mountPage("/404", NonTrouvee.class);

        // Config des pages d'erreurs
        getApplicationSettings().setInternalErrorPage(ErreurInterne.class);
        getApplicationSettings().setAccessDeniedPage(AccesInterdit.class);
        getApplicationSettings().setPageExpiredErrorPage(Expiree.class);
    }

    private void configureRenderingStategy() {
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
    }

    private void configureCDI(){
        // Init de CDI (Wicket)
        new CdiConfiguration().configure(this);
    }
}