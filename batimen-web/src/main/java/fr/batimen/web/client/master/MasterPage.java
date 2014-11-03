package fr.batimen.web.client.master;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogAnimateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.component.BatimenFeedbackPanel;
import fr.batimen.web.client.component.WaiterModal;
import fr.batimen.web.client.event.Event;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.LoginEvent;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.member.client.MesDevis;
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

    private Dialog loginDialog;
    private AuthentificationPanel authentificationPanel;
    
    // waiter modal can be accessed from every child view
	protected WaiterModal waiterModal;
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
        titleInHeader.append(" - Bati-men.fr");

        // Le titre qui se trouve dans la balise title (et donc qui s'affiche
        // dans l'onglet du navigateur)
        Label titleLbl = new Label("title", new Model<String>(titleInHeader.toString()));
        this.add(titleLbl);
        
        //inserting the waitermodal at the bottom of the page
        waiterModal = new WaiterModal("waiterModal");
        this.add(waiterModal);

        initComponentConnexion();
        initTitleHeader(isPageWithTitleHeader, title, adresseImgBackground);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Instantiation de la master page.....OK");
        }
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

        final Label connexionlbl = new Label("connexionlbl", new Model<String>());
        if (SecurityUtils.getSubject().isAuthenticated()) {
            connexionlbl.setDefaultModelObject("Mon Compte");
        } else {
            connexionlbl.setDefaultModelObject("Espace Membre");
        }

        connexionlbl.setOutputMarkupId(true);

        // Lien qui amene à la page de connexion : n'est visible que quand
        // l'utilisateur n'est pas encore loggé
        final AjaxLink<String> connexion = new AjaxLink<String>("connexion") {
            private static final long serialVersionUID = -5109878814704325528L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    setResponsePage(MesDevis.class);
                } else {
                    getLoginDialog().open(target);
                }

            }

            // On fait souscrire ce container a l'event loginEvent pour que le
            // panel Authentification puisse lui dire de se mettre a jour quand
            // l'utilisateur se connecte
            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload() instanceof LoginEvent) {

                    Event update = (Event) event.getPayload();
                    connexionlbl.setDefaultModelObject("Mon Compte");

                    update.getTarget().add(this);

                    getLoginDialog().close(update.getTarget());
                }
            }

        };

        connexion.setMarkupId("connexionLink");
        connexion.setOutputMarkupId(true);

        connexion.add(connexionlbl);

        this.add(connexion);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du composant de connexion.....OK");
        }

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

    protected final Dialog getLoginDialog() {

        if (loginDialog == null) {

            authentificationPanel = new AuthentificationPanel("authentificationPanel");
            authentificationPanel.setOutputMarkupId(true);

            loginDialog = new Dialog("loginDialog") {

                private static final long serialVersionUID = -1661769505673895284L;

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.odlabs.wiquery.ui.dialog.Dialog#open(org.apache.wicket
                 * .ajax.AjaxRequestTarget)
                 */
                @Override
                public void open(AjaxRequestTarget ajaxRequestTarget) {
                    authentificationPanel.resetLabelError();
                    ajaxRequestTarget.add(authentificationPanel);
                    super.open(ajaxRequestTarget);
                }

            };
            loginDialog.setModal(true);
            loginDialog.setTitle("Connexion à l\\'espace client / artisan");
            loginDialog.setResizable(false);
            loginDialog.setDraggable(false);
            loginDialog.setWidth(620);
            loginDialog.add(authentificationPanel);
            loginDialog.setShow(new DialogAnimateOption("fade"));
            loginDialog.setHide(new DialogAnimateOption("fade"));
        }

        return loginDialog;
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
        }
    }

    public static void triggerEventFeedBackPanel(AjaxRequestTarget target, String message) {
        FeedBackPanelEvent feedbackPanelEvent = new FeedBackPanelEvent(target);
        feedbackPanelEvent.setMessage(message);
        target.getPage().send(target.getPage(), Broadcast.EXACT, feedbackPanelEvent);
    }

}
