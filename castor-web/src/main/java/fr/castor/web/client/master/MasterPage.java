package fr.castor.web.client.master;

import fr.castor.core.enums.PropertiesFileGeneral;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.app.enums.PropertiesFileWeb;
import fr.castor.web.client.behaviour.LoginDialogBehaviour;
import fr.castor.web.client.component.CastorFeedbackPanel;
import fr.castor.web.client.component.LinkLabel;
import fr.castor.web.client.component.WaiterModal;
import fr.castor.web.client.component.breadcrumb.BreadCrumbCastor;
import fr.castor.web.client.event.*;
import fr.castor.web.client.extend.*;
import fr.castor.web.client.extend.connected.RechercheAnnonce;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.castor.web.client.extend.nouveau.devis.NouveauDevis;
import fr.castor.web.client.modal.AuthentificationModal;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page principal de l'application dans laquelle tous les autres panels seront
 * contenues
 *
 * @author Casaucau Cyril
 */
public abstract class MasterPage extends WebPage {

    // Général
    private static final long serialVersionUID = 6955108821767948992L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MasterPage.class);
    // Controle le tag HTML
    private final TransparentWebMarkupContainer htmlTag;
    // Feedback panel général
    protected CastorFeedbackPanel feedBackPanelGeneral;
    // waiter modal can be accessed from every child view
    protected WaiterModal waiterModal;
    private String metaDescription = "";
    private String metaKeywords = "";
    private AuthentificationModal authentificationPanel;
    private Label connexionlbl;
    private LoginDialogBehaviour loginDialogBehaviour;
    private WebMarkupContainer menu;
    private boolean useBreadcrumb;

    /**
     * Constructeur par defaut, initialise les composants de base de la page
     */
    public MasterPage() {
        super();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Début instantiation de la master page.....");
        }

        // Fix wicket : prends en charge les <!--[if IE 7 ]><html
        // class="ie ie7" lang="en"> <![endif]--> du template de maniere
        // programmatic
        htmlTag = new TransparentWebMarkupContainer("htmlTag");
        this.add(htmlTag);
        htmlTag.add(AttributeAppender.replace("class", getCSSHtmlTagClass()));

        // Instantiation du composant qui permet d'afficher des messages aux
        // utilisateur de maniere centralisé
        feedBackPanelGeneral = new CastorFeedbackPanel("feedBackPanelGeneral");
        feedBackPanelGeneral.setMarkupId("feedBackPanelGeneral");
        feedBackPanelGeneral.setOutputMarkupId(true);
        htmlTag.add(feedBackPanelGeneral);

        this.add(getLoginDialog());
    }

    /**
     * Constructeur a utiliser obligatoirement quand on instantie une page
     *
     * @param metaDescription       rempli le contenue de la balise meta content de la page
     * @param metaKeywords          Rempli le contenu de la balise meta keyword
     * @param title                 Utilisé pour remplir la balise title et le titre principal de
     *                              la page
     * @param isPageWithTitleHeader est ce que la page doit avoir un bandeau en dessous du menu
     *                              avec le titre de la page ?
     */
    public MasterPage(String metaDescription, String metaKeywords, String title, boolean isPageWithTitleHeader,
                      String adresseImgBackground) {
        this();
        this.metaDescription = metaDescription;
        this.metaKeywords = metaKeywords;

        StringBuilder titleInHeader = new StringBuilder(title);
        titleInHeader.append(" - lesCastors.fr");

        // Le titre qui se trouve dans la balise title (et donc qui s'affiche
        // dans l'onglet du navigateur)
        Label titleLbl = new Label("title", new Model<>(titleInHeader.toString()));
        this.add(titleLbl);

        // inserting the waitermodal at the bottom of the page
        waiterModal = new WaiterModal("waiterModal");
        this.add(waiterModal);

        initComponentConnexion();
        initBreadcrumb();
        initTitleHeader(isPageWithTitleHeader, title, adresseImgBackground);
        initLinkFooter();
        initSocialFooterLink();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Instantiation de la master page.....OK");
        }
    }

    public MasterPage(String metaDescription, String metaKeywords, String title, boolean isPageWithTitleHeader,
                      String adresseImgBackground, boolean useBreadcrumb) {
        this(metaDescription, metaKeywords, title, isPageWithTitleHeader, adresseImgBackground);

        this.useBreadcrumb = useBreadcrumb;
    }

    private void initBreadcrumb() {
        BreadCrumbCastor breadCrumbCastor = new BreadCrumbCastor("breadCrumbCastor", getPage().getPageClass()) {
            @Override
            public boolean isVisible() {
                return useBreadcrumb;
            }
        };
        add(breadCrumbCastor);
    }


    public static void triggerEventFeedBackPanel(AjaxRequestTarget target, String message, FeedbackMessageLevel levelMessage) {
        FeedBackPanelEvent feedbackPanelEvent = new FeedBackPanelEvent(target);
        feedbackPanelEvent.setMessage(message);
        feedbackPanelEvent.setMessageLevel(levelMessage);
        target.getPage().send(target.getPage(), Broadcast.EXACT, feedbackPanelEvent);
    }

    private String getJSForClickListenerOnConnexion(CharSequence callbackString) {
        return "$('.connexionLink').on('click',function(){" + callbackString + "});";
    }

    /**
     * Permet d'afficher un titre et une image de fond dynamiquement sur chaque
     * page
     *
     * @param isPageWithTitleHeader Est ce qu'on veut un titre avec un fond ?
     * @param title                 Le titre de la page
     * @param adresseImgBackGround  L'image de fond que l'on veut afficher
     */
    private void initTitleHeader(final boolean isPageWithTitleHeader, String title, String adresseImgBackGround) {
        WebMarkupContainer containerTitleHeader = new WebMarkupContainer("containerTitleHeader") {

            private static final long serialVersionUID = -8794910421721268035L;

            @Override
            public boolean isVisible() {
                return isPageWithTitleHeader;
            }

        };

        htmlTag.add(containerTitleHeader);

        Label titleHeader = new Label("titleHeader", new Model<>(title));
        containerTitleHeader.add(titleHeader);

        // On prends l'url complete de la page d'accueil avec l'image de fonds
        /*StringBuilder generatedAdresseForImage = new StringBuilder();
        generatedAdresseForImage.append(RequestCycle.get().getUrlRenderer()
                .renderFullUrl(Url.parse(urlFor(Accueil.class, new PageParameters()).toString())));
        generatedAdresseForImage.append("/");
        generatedAdresseForImage.append(adresseImgBackGround);

        // On efface 'accueil' pour que le chemin vers l'image soit correct
        generatedAdresseForImage.delete(generatedAdresseForImage.indexOf("accueil"),
                generatedAdresseForImage.indexOf("accueil") + 8);*/

        // On charge l'image de fond du titre qui a été passé en parametre
        /*StringBuilder bgImageAdresseCSS = new StringBuilder();
        bgImageAdresseCSS.append("background:url(");
        bgImageAdresseCSS.append(generatedAdresseForImage.toString());
        bgImageAdresseCSS.append(") no-repeat center top;");

        containerTitleHeader.add(new AttributeModifier("style", bgImageAdresseCSS.toString()));*/
    }

    private void computeConnexionLbl() {


        if (SecurityUtils.getSubject().isAuthenticated()) {
            connexionlbl.setDefaultModelObject("Mon Compte");
        } else {
            connexionlbl.setDefaultModelObject("Connexion à l’espace membre");
        }
    }

    private void initComponentConnexion() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du composant de connexion.....");
        }
        menu = new WebMarkupContainer("menu");
        menu.setOutputMarkupId(true);
        // add behaviour to page
        loginDialogBehaviour = new LoginDialogBehaviour();
        this.add(loginDialogBehaviour);

        // link to show authentication panel
        connexionlbl = new Label("connexionlbl", new Model<String>());
        connexionlbl.setOutputMarkupId(true);
        computeConnexionLbl();

        menu.add(connexionlbl);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du composant de connexion.....OK");
        }

        final LinkLabel demandeDevis = new LinkLabel("demandeDevis", new Model<>("Obtenir des devis gratuitement")) {

            private static final long serialVersionUID = 737917087176303983L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }

        };

        demandeDevis.setMarkupId("demandeDevis");

        WebMarkupContainer demandeDevisContainer = new WebMarkupContainer("demandeDevisContainer") {

            private static final long serialVersionUID = 5440920791930232343L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    if (SecurityUtils.getSubject().hasRole(TypeCompte.ARTISAN.getRole())) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
                } else {
                    return Boolean.TRUE;
                }
            }

        };

        demandeDevisContainer.setOutputMarkupId(true);
        demandeDevisContainer.add(demandeDevis);

        final LinkLabel devenirPartenaire = new LinkLabel("devenirPartenaire", new Model<>("Devenir une entreprise partenaire")) {

            private static final long serialVersionUID = 3349463856140732172L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauArtisan.class);
            }

        };

        devenirPartenaire.setMarkupId("devenirPartenaire");

        WebMarkupContainer devenirPartenaireContainer = new WebMarkupContainer("devenirPartenaireContainer") {

            private static final long serialVersionUID = 5440920791930232343L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }

        };

        devenirPartenaireContainer.setOutputMarkupId(true);
        devenirPartenaireContainer.add(devenirPartenaire);

        WebMarkupContainer menuConnected = new WebMarkupContainer("menuConnected") {

            private static final long serialVersionUID = -8141527173339087230L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return SecurityUtils.getSubject().isAuthenticated();
            }

        };

        Link<Void> logout = new Link<Void>("logout") {

            private static final long serialVersionUID = 963494446704131066L;

            @Override
            public void onClick() {
                SecurityUtils.getSubject().logout();
                Session.get().invalidate();
                this.setResponsePage(Accueil.class);
            }
        };

        logout.setMarkupId("logout");
        logout.setOutputMarkupId(true);

        menuConnected.add(logout);

        final LinkLabel rechercheAnnonce = new LinkLabel("rechercheAnnonce", new Model<>("Rechercher des annonces")) {

            private static final long serialVersionUID = 3349463856140732172L;

            @Override
            public void onClick() {
                this.setResponsePage(RechercheAnnonce.class);
            }

        };

        rechercheAnnonce.setMarkupId("rechercheAnnonce");

        WebMarkupContainer rechercheAnnonceContainer = new WebMarkupContainer("rechercheAnnonceContainer") {

            private static final long serialVersionUID = 3345463156840732172L;

            /*
             * (non-Javadoc)
             *
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (SecurityUtils.getSubject().isAuthenticated() && (SecurityUtils.getSubject().hasRole(TypeCompte.ARTISAN.getRole()) || SecurityUtils.getSubject().hasRole(TypeCompte.ADMINISTRATEUR.getRole()))) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        };

        rechercheAnnonceContainer.setOutputMarkupId(true);
        rechercheAnnonceContainer.add(rechercheAnnonce);

        menu.add(devenirPartenaireContainer, demandeDevisContainer, menuConnected, rechercheAnnonceContainer);

        add(menu);

    }

    /**
     * Fix wicket : prends en charge les <!--[if IE 7 ]><html class="ie ie7"
     * lang="en"> <![endif]--> du template de maniere programmatic
     *
     * @return les classes CSS qui vont bien avec le navigateur
     */
    private IModel<String> getCSSHtmlTagClass() {
        return new LoadableDetachableModel<String>() {

            private static final long serialVersionUID = 1502969877304945599L;

            @Override
            protected String load() {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Définition du navigateur.....");
                }
                StringBuilder sb = new StringBuilder("");
                WebSession session = (WebSession) getSession();

                ClientProperties clientProperties = session.getClientInfo().getProperties();
                int majorVersion = clientProperties.getBrowserVersionMajor();

                if (clientProperties.isBrowserInternetExplorer()) {
                    sb.append("ie ").append("ie").append(majorVersion);
                } else {
                    sb.append("ie ").append("ie9");
                }

                return sb.toString();
            }
        };
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.
     * IHeaderResponse)
     */
    @Override
    public void renderHead(IHeaderResponse response) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Ajout des resources dans le header.....");
        }

        // Balise meta
        response.render(addStringToMetaResourcesToHeader("text/html; charset=utf-8", "content-type", ""));
        response.render(addStringToMetaResourcesToHeader(metaDescription, "", "description"));
        response.render(addStringToMetaResourcesToHeader(metaKeywords, "", "keywords"));
        response.render(OnDomReadyHeaderItem.forScript(getJSForClickListenerOnConnexion(loginDialogBehaviour
                .getCallbackScript())));

        JavaScriptUrlReferenceHeaderItem bootstrapJS = JavaScriptHeaderItem.forUrl("js/bootstrap.js");
        JavaScriptUrlReferenceHeaderItem bootstrapModalManager = JavaScriptHeaderItem.forUrl("js/components/bootstrap-modal/bootstrap-modalmanager.js");
        JavaScriptUrlReferenceHeaderItem bootstrapModal = JavaScriptHeaderItem.forUrl("js/components/bootstrap-modal/bootstrap-modal.js");
        JavaScriptUrlReferenceHeaderItem theme = JavaScriptHeaderItem.forUrl("js/theme.js");

        response.render(bootstrapJS);
        response.render(bootstrapModalManager);
        response.render(bootstrapModal);
        response.render(theme);

        response.render(CssHeaderItem.forUrl("css/google_font.css"));
        response.render(CssHeaderItem.forUrl("css/bootstrap.css"));
        response.render(CssHeaderItem.forUrl("css/bootstrap-override.css"));
        response.render(CssHeaderItem.forUrl("css/bootstrap-responsive.css"));
        response.render(CssHeaderItem.forUrl("css/plugins.css"));
        response.render(CssHeaderItem.forUrl("css/theme.css"));
        response.render(CssHeaderItem.forUrl("css/responsive.css"));
        response.render(CssHeaderItem.forUrl("css/theme_settings.css"));
        response.render(CssHeaderItem.forUrl("css/color_theme.css"));
        response.render(CssHeaderItem.forUrl("css/soliglyphs.css"));
        response.render(CssHeaderItem.forUrl("css/page.css"));
        response.render(CssHeaderItem.forUrl("css/widgets.css"));

        if (Boolean.valueOf(PropertiesFileWeb.APP.getProperties().getProperty("app.activate.analytics"))) {
            StringBuilder googleAnalytics = new StringBuilder("(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){");
            googleAnalytics.append("(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),");
            googleAnalytics.append("m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)");
            googleAnalytics.append("})(window,document,'script','//www.google-analytics.com/analytics.js','ga');");
            googleAnalytics.append(" ga('create', 'UA-68443139-1', 'auto');");
            googleAnalytics.append(" ga('send', 'pageview');");
            response.render(JavaScriptHeaderItem.forScript(googleAnalytics.toString(), "googleAnalytics"));
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Ajout des resources dans le header.....OK");
        }
    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        String version = PropertiesFileWeb.APP.getProperties().getProperty("app.web.version");

        response.setHeader("ETag", version);
        response.enableCaching(Duration.hours(2), WebResponse.CacheScope.PUBLIC);
    }

    /**
     * Génére une balise meta de maniere custom.
     *
     * @param content   Le type de contenu (en general text/html)
     * @param httpEquiv
     * @param name      Type de la balise meta
     * @return objet wicket qui permet de generer la balise link
     */
    private StringHeaderItem addStringToMetaResourcesToHeader(String content, String httpEquiv, String name) {

        // On crée la balise de type : <meta content="text/html; charset=utf-8"
        // http-equiv="content-type" />
        StringBuilder balise = new StringBuilder("<meta content=\"");
        balise.append(content);
        balise.append("\"");
        balise.append(" ");
        if (!"".equals(httpEquiv)) {
            balise.append(generateAttribute("http-equiv", httpEquiv));
        }
        if (!"".equals(name)) {
            balise.append(generateAttribute("name", name));
        } else if (!"".equals(httpEquiv) && !"".equals(name)) {
            balise.append(" ");
            balise.append(generateAttribute("name", name));
        }
        balise.append("/>");

        // On dit à wicket de la placer dans le header de la master page
        return StringHeaderItem.forString(balise.toString());
    }

    /**
     * Génére une balise meta propre a opengraph (Facebook SEO)
     *
     * @param property Le type de balise (og:url, etc)
     * @param content  Le contenu que l'on veut mettre
     * @return objet wicket qui permet de generer la balise link
     */
    protected StringHeaderItem addOpenGraphMetaResourcesToHeader(String property, String content) {

        // On crée la balise de type : <meta property="text/html; charset=utf-8"
        // http-equiv="property-type" />
        StringBuilder balise = new StringBuilder("<meta property=\"");
        balise.append(property);
        balise.append("\"");
        balise.append(" ");
        if (!content.isEmpty()) {
            balise.append(generateAttribute("content", content));
        }
        balise.append("/>");

        // On dit à wicket de la placer dans le header de la master page
        return StringHeaderItem.forString(balise.toString());
    }

    private String generateAttribute(String paramName, String value) {
        StringBuilder attribute = new StringBuilder(paramName);
        attribute.append("=\"");
        attribute.append(value);
        attribute.append("\"");
        return attribute.toString();
    }

    protected final AuthentificationModal getLoginDialog() {
        if (authentificationPanel == null) {
            authentificationPanel = new AuthentificationModal("authenticationModal");
        }
        return authentificationPanel;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof FeedBackPanelEvent) {
            FeedBackPanelEvent feedBackPanelUpdate = (FeedBackPanelEvent) event.getPayload();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Affichage message dans le feedBackPanel");
            }

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            if (!feedBackPanelUpdate.getMessage().isEmpty()) {
                if (feedBackPanelUpdate.isGoToTop()) {
                    feedBackPanelGeneral.sendMessageAndGoToTop(feedBackPanelUpdate.getMessage(),
                            feedBackPanelUpdate.getMessageLevel(), feedBackPanelUpdate.getTarget());
                } else {
                    feedBackPanelGeneral.sendMessage(feedBackPanelUpdate.getMessage(),
                            feedBackPanelUpdate.getMessageLevel());
                }
            }

            feedBackPanelUpdate.getTarget().add(feedBackPanelGeneral);
        } else if (event.getPayload() instanceof LoginDialogEvent) {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                setResponsePage(MesAnnonces.class);
            } else {
                getLoginDialog().open(((Event) event.getPayload()).getTarget());
            }
        } else if (event.getPayload() instanceof LoginEvent) {
            Event update = (Event) event.getPayload();

            computeConnexionLbl();
            update.getTarget().add(menu);
            getLoginDialog().close(update.getTarget());
        } else if (event.getPayload() instanceof ClearFeedbackPanelEvent) {

            Event update = (Event) event.getPayload();

            if (feedBackPanelGeneral.hasFeedbackMessage()) {
                feedBackPanelGeneral.getFeedbackMessages().clear();
            }

            update.getTarget().add(feedBackPanelGeneral);
        }

    }

    private void initSocialFooterLink() {
        String linkFacebookCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.fb");
        String linkTwitterCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.twitter");
        String linkGooglePlusCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.google.plus");

        Link<String> footerFBMaster = new Link<String>("footerFBMaster") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkFacebookCastor));
            }
        };

        Link<String> footerGPlusMaster = new Link<String>("footerGPlusMaster") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkGooglePlusCastor));
            }
        };

        Link<String> footerTwitterMaster = new Link<String>("footerTwitterMaster") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkTwitterCastor));
            }
        };

        add(footerFBMaster, footerGPlusMaster, footerTwitterMaster);
    }

    private void initLinkFooter() {
        Link<String> footerAccueil = new Link<String>("footerAccueil") {
            @Override
            public void onClick() {
                this.setResponsePage(Accueil.class);
            }
        };

        Link<String> footerQuiSommesNous = new Link<String>("footerQuiSommesNous") {
            @Override
            public void onClick() {
                this.setResponsePage(QuiSommeNous.class);
            }
        };

        Link<String> footerFAQ = new Link<String>("footerFAQ") {
            @Override
            public void onClick() {
                this.setResponsePage(FAQ.class);
            }
        };

        Link<String> footerCGU = new Link<String>("footerCGU") {
            @Override
            public void onClick() {
                this.setResponsePage(CGU.class);
            }
        };

        Link<String> footerNosObjectifs = new Link<String>("footerNosObjectifs") {
            @Override
            public void onClick() {
                this.setResponsePage(Objectif.class);
            }
        };

        Link<String> footerNosEngagements = new Link<String>("footerNosEngagements") {
            @Override
            public void onClick() {
                this.setResponsePage(NosEngagements.class);
            }
        };

        add(footerAccueil, footerQuiSommesNous, footerCGU, footerFAQ, footerNosObjectifs, footerNosEngagements);
    }
}