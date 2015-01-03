package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.nouveau.devis.Etape4InscriptionForm;
import fr.batimen.web.client.master.MasterPage;

public class ModifierMonProfil extends MasterPage {

    private static final long serialVersionUID = -8907846225033024753L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierMonProfil.class);

    private CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce;

    public ModifierMonProfil() {
        super("Modifier mon profil", "lol", "Modifier mon profil", true, "img/bg_title1.jpg");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page de modification de mon profil");
        }

        initData();
        initComposant();
    }

    public void initComposant() {
        Profil profil = new Profil("profil");

        Etape4InscriptionForm inscriptionForm = new Etape4InscriptionForm("formInscription",
                propertyModelNouvelleAnnonce, Boolean.TRUE);

        this.add(profil);
        this.add(inscriptionForm);
    }

    private void initData() {
        Authentication authentication = new Authentication();
        // Pour les besoins du form etape 4 qu'on reutilise ici, on instancie sa
        // DTO mais on ne rempli que les informations du client
        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        // Rempli avec le client dto présent en session.
        creationAnnonceDTO.setClient(authentication.getCurrentUserInfo());
        propertyModelNouvelleAnnonce = new CompoundPropertyModel<CreationAnnonceDTO>(creationAnnonceDTO);
    }
}
