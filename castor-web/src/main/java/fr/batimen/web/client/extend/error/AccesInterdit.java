package fr.batimen.web.client.extend.error;

import fr.batimen.web.client.extend.member.client.MesAnnonces;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        super("Accès interdit", "acces interdit pas le droit", "Accès interdit", false, "");

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Appel d'une page d'erreur d'accés");
        }

        Link<String> retourAccueil = new Link<String>("retourEspaceMembre") {

            private static final long serialVersionUID = -3857522655685983676L;

            @Override
            public void onClick() {
                this.setResponsePage(MesAnnonces.class);
            }
        };

        this.add(retourAccueil);
    }

}
