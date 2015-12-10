package fr.castor.web.client.component.link;

import fr.castor.dto.ClientDTO;
import fr.castor.dto.EntrepriseDTO;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.constants.ParamsConstant;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.client.component.LinkLabel;
import fr.castor.web.client.extend.connected.Entreprise;
import fr.castor.web.client.extend.member.client.MonProfil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

/**
 * Lien permettant d'accéder a la page mon profil
 *
 * @author Casaucau Cyril
 */
public class MonProfilLink extends LinkLabel {

    @Inject
    private Authentication authentication;
    @Inject
    private RolesUtils rolesUtils;

    public MonProfilLink(String id, IModel<String> modelString) {
        super(id, modelString);
    }

    @Override
    public void onClick() {
        if (rolesUtils.checkRoles(TypeCompte.ARTISAN)) {
            PageParameters parameters = new PageParameters();
            EntrepriseDTO entreprise = authentication.getEntrepriseUserInfo();
            parameters.add(ParamsConstant.ID_ENTREPRISE_PARAM, entreprise.getSiret());
            this.setResponsePage(Entreprise.class, parameters);
        } else if (rolesUtils.checkRoles(TypeCompte.CLIENT)) {
            PageParameters parameters = new PageParameters();
            ClientDTO client = authentication.getCurrentUserInfo();
            parameters.add("login", client.getLogin());
            this.setResponsePage(MonProfil.class, parameters);
        }
    }
}
