package fr.batimen.web.client.extend;

import org.apache.wicket.markup.html.link.Link;

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

        initLink();
    }

    private void initLink() {
        Link<String> nouveauDevis = new Link<String>("nouveauDevis") {

            private static final long serialVersionUID = -6716952676398723108L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauDevis.class);
            }

        };

        Link<String> nouveauPartenaire = new Link<String>("nouveauPartenaire") {

            /**
             * 
             */
            private static final long serialVersionUID = 1315035411772737764L;

            @Override
            public void onClick() {
                this.setResponsePage(NouveauArtisan.class);
            }

        };

        this.add(nouveauPartenaire);
        this.add(nouveauDevis);
    }

}
