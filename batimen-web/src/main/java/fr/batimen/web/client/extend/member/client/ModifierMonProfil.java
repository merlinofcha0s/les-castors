package fr.batimen.web.client.extend.member.client;

import javax.inject.Inject;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.web.client.extend.nouveau.artisan.Etape3Entreprise;
import fr.batimen.web.client.extend.nouveau.artisan.Etape3EntrepriseForm;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.nouveau.devis.Etape4InscriptionForm;
import fr.batimen.web.client.master.MasterPage;

import java.io.Serializable;

/**
 * Page de modification des informations utilisateurs
 * 
 * @author Casaucau Cyril
 * 
 */
public class ModifierMonProfil extends MasterPage {

    private static final long serialVersionUID = -8907846225033024753L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierMonProfil.class);

    @Inject
    private Authentication authentication;

    private CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce;

    public ModifierMonProfil() {
        super("Modifier mon profil", "lol", "Modifier mon profil", true, "img/bg_title1.jpg");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page de modification de mon profil");
        }

        initData();
        initComposant();
    }

    private void initComposant() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des composants de la page de modification de mon profil");
        }
        Profil profil = new Profil("profil");

        Etape4InscriptionForm inscriptionForm = new Etape4InscriptionForm("formInscription",
                propertyModelNouvelleAnnonce, Boolean.TRUE);

        Etape3Entreprise entrepriseModif = new Etape3Entreprise("etape3InformationsEntreprise",
                new Model<>(),
                new CreationPartenaireDTO(), true);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, inscriptionForm, entrepriseModif, contactezNous, commentaire);
    }

    private void initData() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des données de la page de modification de mon profil");
        }
        // Pour les besoins du form etape 4 qu'on reutilise ici, on instancie sa
        // DTO mais on ne rempli que les informations du client
        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        // Rempli avec le client dto présent en session.
        // On la copie pour eviter que les données en session soit directement
        // modifier par le form
        ClientDTO client = ClientDTO.copy(authentication.getCurrentUserInfo());
        creationAnnonceDTO.setClient(client);
        propertyModelNouvelleAnnonce = new CompoundPropertyModel<>(creationAnnonceDTO);
    }
}