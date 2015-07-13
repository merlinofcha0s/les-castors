package fr.batimen.web.client.extend.connected;

import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.CastorDatePicker;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.DateValidator;

import javax.inject.Inject;
import java.util.Date;

/**
 * Page de recherche des annonce, destinée au Artisan
 *
 * @author Casaucau Cyril
 */
public class RechercheAnnonce extends MasterPage {

    @Inject
    private Authentication authentication;

    private CastorDatePicker castorDatePicker;

    public RechercheAnnonce() {
        super("", "", "Recherche d'annonce", true, "img/bg_title1.jpg");
        initComposants();
        initVosCriteres();
    }

    public RechercheAnnonce(PageParameters params) {
        this();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));

        //FuelUX Librairie
        response.render(JavaScriptHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/loader.min.js"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux.min.css"));
        response.render(CssContentHeaderItem.forUrl("//www.fuelcdn.com/fuelux/2.6.1/css/fuelux-responsive.css"));
        //Datepicker, on ne le mets pas dans le composant car probleme de conflit avec fuelux
        response.render(JavaScriptHeaderItem.forUrl("js/bootstrap-datepicker.js"));
        response.render(CssContentHeaderItem.forUrl("css/datepicker.css"));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", authentication.getEntrepriseUserInfo().getIsVerifier());
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        add(profil, contactezNous, commentaire);
    }

    private void initVosCriteres() {
        castorDatePicker = new CastorDatePicker("recherche.apartirde", "rechercheDate", true);
        castorDatePicker.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));

        Form searchForm = new Form("searchForm");
        searchForm.add(castorDatePicker);

        add(searchForm);
    }

}