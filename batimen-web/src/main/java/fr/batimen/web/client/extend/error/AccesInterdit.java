package fr.batimen.web.client.extend.error;

import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.extend.member.client.MonCompte;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page d'erreur pour les accés interdits
 * 
 * @author Casaucau Cyril
 * 
 */
public class AccesInterdit extends MasterPage {

    private static final long serialVersionUID = -2658476710165622872L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccesInterdit.class);

    public AccesInterdit() {
        super("Accès interdit", "acces interdit droit", "Accès interdit", false, "");

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Appel d'une page d'erreur d'accés");
        }

        Link<String> retourAccueil = new Link<String>("retourEspaceMembre") {

            private static final long serialVersionUID = -3857522655685983676L;

            @Override
            public void onClick() {
                this.setResponsePage(MonCompte.class);
            }
        };

        this.add(retourAccueil);
    }

}
