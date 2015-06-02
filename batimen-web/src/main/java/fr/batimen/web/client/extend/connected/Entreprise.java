package fr.batimen.web.client.extend.connected;

import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by Casaucau on 02/06/2015.
 */
public class Entreprise extends MasterPage {

    //SIRET
    private String idEntreprise;

    public Entreprise() {
        super("", "", "Entreprise", true, "img/bg_title1.jpg");
    }

    public Entreprise(PageParameters params) {
        this();
        idEntreprise = params.get(ParamsConstant.ID_ENTREPRISE_PARAM).toString();
        loadEntrepriseData();
    }

    private void loadEntrepriseData(){

    }
}
