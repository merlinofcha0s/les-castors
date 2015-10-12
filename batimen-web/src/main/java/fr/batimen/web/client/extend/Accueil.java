package fr.batimen.web.client.extend;

import fr.batimen.core.constant.UrlPage;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauDevis;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.Link;

/**
 * Premiere page visible quand l'utilisateur arrivera sur le site, elle definira
 * notre offre
 * 
 * @author Casaucau Cyril
 * 
 */
public class Accueil extends MasterPage {

    private static final long serialVersionUID = -690817359101639588L;

    public Accueil() {
        super("Page d'accueil de lescastors.fr", "lescastors renovation devis neuf", "Accueil lescastors", false, "img/bg_title1.jpg");
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        this.add(contactezNous);
        initLinks();
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("css/font_icons8.css"));
        response.render(CssHeaderItem.forUrl("css/accueil.css"));
        response.render(CssHeaderItem.forUrl("css/rs-settings.css"));
        response.render(CssHeaderItem.forUrl("css/rs-settings-override.css"));
        response.render(CssHeaderItem.forUrl("//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"));

        response.render(JavaScriptHeaderItem.forUrl("js/jquery.themepunch.plugins.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("js/jquery.themepunch.revolution.min.js"));

        StringBuilder adresseAccueil = new StringBuilder();
        adresseAccueil.append("https://lescastors.fr").append(UrlPage.ACCUEIL_URL);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseAccueil.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Bienvenue sur le site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Des artisans de qualités prés de chez vous"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", "https://res.cloudinary.com/lescastors/image/upload/v1443971771/mail/logo-bleu2x.png"));
    }

    private void initLinks() {
        Link<String> nouveauDevis2 = new Link<String>("nouveauDevisLink2") {

            private static final long serialVersionUID = -9117623557888658144L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }

        };

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

        this.add(nouveauDevis2, rejoignezNousLink,  faireUnDevisSlide1,
                faireUnDevisSlide2, faireUnDevisSlide3);
    }

}
