package fr.batimen.web.client.component;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.app.security.RolesUtils;
import fr.batimen.web.client.extend.connected.Entreprise;
import fr.batimen.web.client.extend.member.client.ModifierMonProfil;
import fr.batimen.web.client.extend.member.client.MonProfil;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

/**
 * Panel permettant à l'utilisateur de voir sa photo de profil et d'acceder a son profil ou à la modification
 */
public class Profil extends Panel {

    private static final long serialVersionUID = -3775533895973607467L;

    @Inject
    private Authentication authentication;
    @Inject
    private RolesUtils rolesUtils;

    private LinkLabel monProfil;
    private LinkLabel modifierMonProfil;
    private Model<String> voirProfilModel;
    private Model<String> modifierProfilModel;

    public Profil(String id) {
        super(id);
        setModel();
        setLink();
    }

    private void setLink() {
        monProfil = new LinkLabel("monProfil", voirProfilModel) {

            private static final long serialVersionUID = 1L;

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
        };

        modifierMonProfil = new LinkLabel("modifierMonProfil", modifierProfilModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                this.setResponsePage(ModifierMonProfil.class);
            }
        };

        this.add(monProfil);
        this.add(modifierMonProfil);
    }

    private void setModel() {
        voirProfilModel = new Model<>();
        modifierProfilModel = new Model<>();
        if (rolesUtils.checkRoles(TypeCompte.ARTISAN)) {
            voirProfilModel.setObject("Voir mon entreprise");
            modifierProfilModel.setObject("Modifier mes informations");
        } else {
            voirProfilModel.setObject("Voir le profil");
            modifierProfilModel.setObject("Modifier le profil");
        }
    }
}
