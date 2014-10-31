package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */

public final class MonCompte extends MasterPage {

    private static final long serialVersionUID = 1902734649854998120L;

    public MonCompte() {
        super("Page accueil de batimen", "lol", "Bienvenue sur batimen.fr", true, "img/bg_title1.jpg");
        add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        Authentication authentication = new Authentication();
        add(new Label("login", "Bonjour, " + authentication.getCurrentUserInfo().getNom()));
        Link<Void> logout = new Link<Void>("logout") {

            /**
             * 
             */
            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick() {
                Session.get().invalidate();
                this.setResponsePage(Accueil.class);
            }

        };

        this.add(logout);
        this.setOutputMarkupId(true);
    }

}
