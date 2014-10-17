package fr.batimen.web.client.extend;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;

import fr.batimen.web.app.security.Authentication;
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
        AjaxLink<Void> logout = new AjaxLink<Void>("logout") {

            /**
             * 
             */
            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                SecurityUtils.getSubject().logout();
                target.add(this);
            }
        };

        this.add(logout);
        this.setOutputMarkupId(true);
    }

}
