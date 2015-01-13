package fr.batimen.web.client.master;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.web.client.behaviour.LoginDialogBehaviour;
import fr.batimen.web.client.component.BatimenFeedbackPanel;
import fr.batimen.web.client.component.LinkLabel;
import fr.batimen.web.client.component.WaiterModal;
import fr.batimen.web.client.event.Event;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.LoginDialogEvent;
import fr.batimen.web.client.event.LoginEvent;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.member.client.MesAnnonces;
import fr.batimen.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauDevis;
import fr.batimen.web.client.panel.AuthentificationPanel;

/**
 * Page principal de l'application dans laquelle tous les autres panels seront
 * contenues
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class MasterPage extends WebPage {

    // Général
    private static final long serialVersionUID = 6955108821767948992L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MasterPage.class);
    private String metaDescription = "";
    private String metaKeywords = "";

    // Controle le tag HTML
    private final TransparentWebMarkupContainer htmlTag;

    // Feedback panel général
    protected BatimenFeedbackPanel feedBackPanelGeneral;

    private AuthentificationPanel authentificationPanel;

    private Label connexionlbl;

    // waiter modal can be accessed from every child view
    protected WaiterModal waiterModal;
    private LoginDialogBehaviour loginDialogBehaviour;

    /**
     * Constructeur par defaut, initialise les composants de base de la page
     * 
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
        feedBackPanelGeneral = new BatimenFeedbackPanel("feedBackPanelGeneral");
        feedBackPanelGeneral.setOutputMarkupId(true);
        htmlTag.add(feedBackPanelGeneral);

        this.add(getLoginDialog());
    }

    /**
     * Constructeur a utiliser obligatoirement quand on instantie une page
     * 
     * @param metaDescription
     *            rempli le contenue de la balise meta content de la page
     * @param metaKeywords
     *            Rempli le contenu de la balise meta keyword
     * @param title
     *            Utilisé pour remplir la balise title et le titre principal de
     *            la page
     * @param isPageWithTitleHeader
     *            est ce que la page doit avoir un bandeau en dessous du menu
     *            avec le titre de la page ?
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
        Label titleLbl = new Label("title", new Model<String>(titleInHeader.toString()));
        this.add(titleLbl);

        // inserting the waitermodal at the bottom of the page
        waiterModal = new WaiterModal("waiterModal");
        this.add(waiterModal);

        initComponentConnexion();
        initTitleHeader(isPageWithTitleHeader, title, adresseImgBackground);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Instantiation de la master page.....OK");
        }
    }

    private String getJSForClickListenerOnConnexion(CharSequence callbackString) {
        String script = "$('.connexionLink').on('click',function(){" + callbackString + "});";
        return script;
    }

    /**
     * Permet d'afficher un titre et une image de fond dynamiquement sur chaque
     * page
     * 
     * @param isPageWithTitleHeader
     *            Est ce qu'on veut un titre avec un fond ?
     * @param title
     *            Le titre de la page
     * @param adresseImgBackGround
     *            L'image de fond que l'on veut afficher
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

        Label titleHeader = new Label("titleHeader", new Model<String>(title));
        containerTitleHeader.add(titleHeader);

        // On prends l'url complete de la page d'accueil avec l'image de fonds
        StringBuilder generatedAdresseForImage = new StringBuilder(RequestCycle.get().getUrlRenderer()
                .renderFullUrl(Url.parse(urlFor(Accueil.class, new PageParameters()).toString())));
        generatedAdresseForImage.append("/");
        generatedAdresseForImage.append(adresseImgBackGround);

        // On efface 'accueil' pour que le chemin vers l'image soit correct
        generatedAdresseForImage.delete(generatedAdresseForImage.indexOf("accueil"),
                generatedAdresseForImage.indexOf("accueil") + 8);

        // On charge l'image de fond du titre qui a été passé en parametre
        StringBuilder bgImageAdresseCSS = new StringBuilder("background:url(");
        bgImageAdresseCSS.append(generatedAdresseForImage.toString());
        bgImageAdresseCSS.append(") no-repeat center top;");

        containerTitleHeader.add(new AttributeModifier("style", bgImageAdresseCSS.toString()));
    }

    private void initComponentConnexion() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du composant de connexion.....");
        }
        final WebMarkupContainer menu = new WebMarkupContainer("menu");
        menu.setOutputMarkupId(Boolean.TRUE);
        // add behaviour to page
        loginDialogBehaviour = new LoginDialogBehaviour();
        this.add(loginDialogBehaviour);

        // link to show authentication panel
        connexionlbl = new Label("connexionlbl", new Model<String>());
        if (SecurityUtils.getSubject().isAuthenticated()) {
            connexionlbl.setDefaultModelObject("Mon Compte");
        } else {
            connexionlbl.setDefaultModelObject("Espace Membre");
        }

        connexionlbl.setOutputMarkupId(true);

        menu.add(connexionlbl);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du composant de connexion.....OK");
        }

        final LinkLabel demandeDevis = new LinkLabel("demandeDevis", new Model<String>("DEMANDE DE DEVIS")) {

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

        final LinkLabel devenirPartenaire = new LinkLabel("devenirPartenaire", new Model<String>("DEVENIR PARTENAIRE")) {

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
                    if (SecurityUtils.getSubject().hasRole(TypeCompte.CLIENT.getRole())) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
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
                Session.get().invalidate();
                this.setResponsePage(Accueil.class);
            }

        };

        logout.setMarkupId("logout");
        logout.setOutputMarkupId(true);

        menuConnected.add(logout);

        menu.add(devenirPartenaireContainer);
        menu.add(demandeDevisContainer);
        menu.add(menuConnected);

        this.add(menu);

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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Ajout des resources dans le header.....OK");
        }
    }

    /**
     * Génére une balise meta de maniere custom.
     * 
     * @param content
     *            Le type de contenu (en general text/html)
     * @param httpEquiv
     * @param name
     *            Type de la balise meta
     * @return objet wicket qui permet de generer la balise link
     */
    private StringHeaderItem addStringToMetaResourcesToHeader(String content, String httpEquiv, String name) {

        // On crée la balise de type : <meta content="text/html; charset=utf-8"
        // http-equiv="content-type" />
        StringBuilder balise = new StringBuilder("<meta content=\"");
        balise.append(content);
        balise.append("\"");
        if (!"".equals(httpEquiv)) {
            balise.append(generateAttribute("http-equiv", httpEquiv));
        }
        if (!"".equals(name)) {
            balise.append(generateAttribute("name", name));
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

    protected AuthentificationPanel getLoginDialog() {
        if (authentificationPanel == null) {
            authentificationPanel = new AuthentificationPanel("authentificationPanel");
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
            if (!feedBackPanelUpdate.getMessage().isEmpty()) {
                feedBackPanelGeneral.error(feedBackPanelUpdate.getMessage());
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
            connexionlbl.setDefaultModelObject("Mon Compte");

            update.getTarget().add(this);
            getLoginDialog().close(update.getTarget());
        }
    }

    public static void triggerEventFeedBackPanel(AjaxRequestTarget target, String message) {
        FeedBackPanelEvent feedbackPanelEvent = new FeedBackPanelEvent(target);
        feedbackPanelEvent.setMessage(message);
        target.getPage().send(target.getPage(), Broadcast.EXACT, feedbackPanelEvent);
    }

}
