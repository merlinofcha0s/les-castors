package fr.castor.web.client.extend;

import fr.castor.core.constant.UrlPage;
import fr.castor.core.enums.PropertiesFileGeneral;
import fr.castor.web.app.constants.JSConstant;
import fr.castor.web.client.component.ContactezNous;
import fr.castor.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.castor.web.client.extend.nouveau.devis.NouveauDevis;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptUrlReferenceHeaderItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;

/**
 * Premiere page visible quand l'utilisateur arrivera sur le site, elle definira
 * notre offre
 *
 * @author Casaucau Cyril
 */
public class Accueil extends MasterPage {

    private static final long serialVersionUID = -690817359101639588L;

    public Accueil() {
        super("Page d'accueil de lescastors.fr", "lescastors renovation devis neuf", "Accueil lescastors", false, "img/bg_title1.jpg");
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        this.add(contactezNous);
        initLinks();
        initSocialLink();
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("css/font_icons8.css"));
        response.render(CssHeaderItem.forUrl("css/accueil.css"));
        response.render(CssHeaderItem.forUrl("css/rs-settings.css"));
        response.render(CssHeaderItem.forUrl("css/rs-settings-override.css"));
        response.render(JSConstant.fontAwesome);

        JavaScriptUrlReferenceHeaderItem themePunchPlugin = JavaScriptHeaderItem.forUrl("js/jquery.themepunch.plugins.min.js");
        JavaScriptUrlReferenceHeaderItem themePunchPluginRevolution = JavaScriptHeaderItem.forUrl("js/jquery.themepunch.revolution.min.js");

        response.render(themePunchPlugin);
        response.render(themePunchPluginRevolution);

        StringBuilder adresseAccueil = new StringBuilder();
        adresseAccueil.append("https://lescastors.fr").append(UrlPage.ACCUEIL_URL);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseAccueil.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Bienvenue sur le site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Des artisans de qualités prés de chez vous"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", UrlPage.LOGO));
    }

    private void initLinks() {
        Link<String> rejoignezNousLink = new Link<String>("rejoignezNousLink") {

            private static final long serialVersionUID = -5557301617442136725L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauArtisan.class);
            }
        };

        Link<String> faireUnDevisSlide1 = new Link<String>("faireUnDevisSlide1") {

            private static final long serialVersionUID = 9089145391867475874L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }
        };

        Link<String> faireUnDevisSlide2 = new Link<String>("faireUnDevisSlide2") {

            private static final long serialVersionUID = 9089145391867475874L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }
        };

        Link<String> faireUnDevisSlide3 = new Link<String>("faireUnDevisSlide3") {

            private static final long serialVersionUID = 9089145391867475874L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }
        };

        add(rejoignezNousLink, faireUnDevisSlide1, faireUnDevisSlide2, faireUnDevisSlide3);
    }

    private void initSocialLink() {
        String linkFacebookCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.fb");
        String linkTwitterCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.twitter");
        String linkGooglePlusCastor = PropertiesFileGeneral.URL.getProperties().getProperty("url.google.plus");

        Link<String> footerFB = new Link<String>("footerFB") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkFacebookCastor));
            }
        };

        Link<String> footerGPlus = new Link<String>("footerGPlus") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkGooglePlusCastor));
            }
        };

        Link<String> footerTwitter = new Link<String>("footerTwitter") {
            @Override
            public void onClick() {
                this.setResponsePage(new RedirectPage(linkTwitterCastor));
            }
        };

        add(footerFB, footerGPlus, footerTwitter);
    }
}