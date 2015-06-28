package fr.batimen.web.client.extend;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;

import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.extend.nouveau.artisan.NouveauArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauDevis;
import fr.batimen.web.client.master.MasterPage;

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
        super("Page d'accueil de lescastors.fr", "lol", "Accueil lescastors", false, "img/bg_title1.jpg");
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        this.add(contactezNous);
        initLinks();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"));
    }

    private void initLinks() {
        Link<String> nouveauDevis = new Link<String>("nouveauDevisLink") {

            private static final long serialVersionUID = -6716952676398723108L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }

        };

        Link<String> nouveauDevis1 = new Link<String>("nouveauDevisLink1") {

            private static final long serialVersionUID = 1315035411772737764L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }

        };

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

        this.add(nouveauDevis, nouveauDevis1, nouveauDevis2, rejoignezNousLink,  faireUnDevisSlide1,
                faireUnDevisSlide2, faireUnDevisSlide3);
    }

}
