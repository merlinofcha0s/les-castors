package fr.batimen.web.client.extend.connected;

import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

/**
 * Page de recherche des annonce, destinée au Artisan
 *
 * @author Casaucau Cyril
 */
public class RechercheAnnonce extends MasterPage{

    @Inject
    private Authentication authentication;

    public RechercheAnnonce() {
        super("", "", "Recherche d'annonce", true, "img/bg_title1.jpg");
        initComposants();
    }

    public RechercheAnnonce(PageParameters params) {

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", authentication.getEntrepriseUserInfo().getIsVerifier());
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        add(profil, contactezNous, commentaire);
    }

}