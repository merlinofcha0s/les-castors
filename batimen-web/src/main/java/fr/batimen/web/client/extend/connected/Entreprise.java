package fr.batimen.web.client.extend.connected;

import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by Casaucau on 02/06/2015.
 */
public class Entreprise extends MasterPage {

    //SIRET
    private String idEntreprise;

    private static final  String REFRESH_TOOLTIP_ON_CATEGORIE_TYPE = "$('.categorie-entreprise').tooltip()";

    public Entreprise() {
        super("", "", "Entreprise", true, "img/bg_title1.jpg");
    }

    public Entreprise(PageParameters params) {
        this();
        idEntreprise = params.get(ParamsConstant.ID_ENTREPRISE_PARAM).toString();
        loadEntrepriseData();
        initComposants();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(OnDomReadyHeaderItem.forScript(REFRESH_TOOLTIP_ON_CATEGORIE_TYPE));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", true);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, contactezNous, commentaire);
    }

    private void loadEntrepriseData(){

    }
}
