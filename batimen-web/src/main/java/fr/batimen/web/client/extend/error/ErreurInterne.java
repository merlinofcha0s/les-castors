package fr.batimen.web.client.extend.error;

import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page pour avertir l'utilisateur d'une erreur interne
 * 
 * @author Casaucau
 * 
 */
public class ErreurInterne extends MasterPage {

    private static final long serialVersionUID = 3662412773662684396L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ErreurInterne.class);

    public ErreurInterne() {
        super("Erreur interne", "erreur 500", "Erreur interne", false, "");

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Appel d'une page d'erreur 500");
        }

        Link<String> retourAccueil = new Link<String>("retourAccueil") {

            private static final long serialVersionUID = -3857522655685983676L;

            @Override
            public void onClick() {
                this.setResponsePage(Accueil.class);
            }
        };

        this.add(retourAccueil);
    }

}
