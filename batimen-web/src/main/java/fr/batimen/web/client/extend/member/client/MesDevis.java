package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;

import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.CastorMenu;
import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */

public final class MesDevis extends MasterPage {

    private static final long serialVersionUID = 1902734649854998120L;

    public MesDevis() {
        super("Page accueil de batimen", "lol", "Bienvenue sur lescastors.fr", true, "img/bg_title1.jpg");
        Authentication authentication = new Authentication();
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

        CastorMenu menu = new CastorMenu("menu");
        this.add(menu);

        this.add(logout);
        this.setOutputMarkupId(true);
    }

}
