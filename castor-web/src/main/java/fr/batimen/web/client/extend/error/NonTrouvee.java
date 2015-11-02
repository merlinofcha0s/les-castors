package fr.batimen.web.client.extend.error;

import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page qui avertit l'utilisateur d'une erreur 404
 * 
 * @author Casaucau Cyril
 * 
 */
public class NonTrouvee extends MasterPage {

    private static final long serialVersionUID = 5887544836053469315L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NonTrouvee.class);

    public NonTrouvee() {
        super("Page erreur 404", "page web non trouvée 404", "Page non trouvée", false, "");

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Appel d'une page d'erreur 404");
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
