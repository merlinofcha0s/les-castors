package fr.batimen.web.client.extend.error;

import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page qui avertis l'utilisateur que sa page a expirée.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Expiree extends MasterPage {

    private static final long serialVersionUID = 982370973946055446L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Expiree.class);

    public Expiree() {
        super("Page expirée", "page web expiration expirée", "Page expirée", false, "");

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Appel d'une page d'expiration");
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
