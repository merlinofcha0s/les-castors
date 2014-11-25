package fr.batimen.web.client.extend;

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
        super("Page d'accueil de batimen.fr", "lol", "Accueil batimen", false, "img/bg_title1.jpg");
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        this.add(contactezNous);
        initLinks();
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

        Link<String> contactLink = new Link<String>("contactLink") {

            private static final long serialVersionUID = 9089145391867475874L;

            @Override
            public void onClick() {
                this.setResponsePage(Contact.class);
            }
        };

        this.add(nouveauDevis);
        this.add(nouveauDevis1);
        this.add(nouveauDevis2);
        this.add(rejoignezNousLink);
        this.add(contactLink);
    }

}
