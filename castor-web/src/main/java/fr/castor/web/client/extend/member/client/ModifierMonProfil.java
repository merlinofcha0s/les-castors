package fr.castor.web.client.extend.member.client;

import fr.castor.dto.ClientDTO;
import fr.castor.dto.EntrepriseDTO;
import fr.castor.dto.aggregate.CreationAnnonceDTO;
import fr.castor.dto.aggregate.CreationPartenaireDTO;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.client.component.Commentaire;
import fr.castor.web.client.component.ContactezNous;
import fr.castor.web.client.component.PhotosContainer;
import fr.castor.web.client.component.Profil;
import fr.castor.web.client.event.AjoutPhotoEvent;
import fr.castor.web.client.event.SuppressionPhotoEvent;
import fr.castor.web.client.extend.member.client.util.PhotoServiceAjaxLogic;
import fr.castor.web.client.extend.nouveau.artisan.Etape3Entreprise;
import fr.castor.web.client.extend.nouveau.devis.Etape4InscriptionForm;
import fr.castor.web.client.master.MasterPage;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Page de modification des informations utilisateurs
 *
 * @author Casaucau Cyril
 */
public class ModifierMonProfil extends MasterPage {

    private static final long serialVersionUID = -8907846225033024753L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierMonProfil.class);

    @Inject
    private Authentication authentication;

    @Inject
    private RolesUtils rolesUtils;

    @Inject
    private PhotoServiceAjaxLogic photoServiceAjaxLogic;

    private EntrepriseDTO entreprise;

    private CompoundPropertyModel<CreationAnnonceDTO> propertyModelNouvelleAnnonce;
    private CompoundPropertyModel<CreationPartenaireDTO> propertyModelNouveauPartenaire;

    private PhotosContainer photoChantierTemoin;

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
        Profil profil = new Profil("profil", false);

        Etape4InscriptionForm inscriptionForm = new Etape4InscriptionForm("formInscription",
                propertyModelNouvelleAnnonce, Boolean.TRUE);

        Etape3Entreprise entrepriseModif = new Etape3Entreprise("etape3InformationsEntreprise",
                propertyModelNouveauPartenaire, propertyModelNouveauPartenaire.getObject()
                , true);

        photoChantierTemoin = new PhotosContainer("photoChantierTemoin", entreprise.getPhotosChantiersTemoins(), "Vos photos de chantiers témoins", "h5", true) {
            @Override
            public boolean isVisible() {
                return rolesUtils.checkRoles(TypeCompte.ARTISAN);
            }
        };

        WebMarkupContainer containerEntreprise = new WebMarkupContainer("containerEntreprise") {
            @Override
            public boolean isVisible() {
                return rolesUtils.checkRoles(TypeCompte.ARTISAN);
            }
        };

        containerEntreprise.add(entrepriseModif, photoChantierTemoin);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, inscriptionForm, containerEntreprise, contactezNous, commentaire);
    }

    private void initData() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des données de la page de modification de mon profil");
        }
        // Pour les besoins du form etape 4 qu'on reutilise ici, on instancie sa
        // DTO mais on ne rempli que les informations du client
        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        CreationPartenaireDTO creationPartenaireDTO = new CreationPartenaireDTO();
        // Rempli avec le client dto présent en session.
        // On la copie pour eviter que les données en session soit directement
        // modifier par le form
        ClientDTO client = ClientDTO.copy(authentication.getCurrentUserInfo());
        creationAnnonceDTO.setClient(client);
        propertyModelNouvelleAnnonce = new CompoundPropertyModel<>(creationAnnonceDTO);

        if (rolesUtils.checkRoles(TypeCompte.ARTISAN)) {
            entreprise = EntrepriseDTO.copy(authentication.getEntrepriseUserInfo());
            ModelMapper mapper = new ModelMapper();
            mapper.map(entreprise, creationPartenaireDTO.getEntreprise());
            mapper.map(entreprise.getAdresseEntreprise(), creationPartenaireDTO.getAdresse());
            creationPartenaireDTO.getEntreprise().getCategoriesMetier().addAll(entreprise.getCategoriesMetier());
        }else{
            entreprise = new EntrepriseDTO();
        }
        propertyModelNouveauPartenaire = new CompoundPropertyModel<>(creationPartenaireDTO);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        if (event.getPayload() instanceof SuppressionPhotoEvent) {
            photoServiceAjaxLogic.suppressionPhotoChantierTemoin(event);
            photoChantierTemoin.updatePhotoContainer(((SuppressionPhotoEvent) event.getPayload()).getTarget());
        }

        if (event.getPayload() instanceof AjoutPhotoEvent) {
            photoServiceAjaxLogic.ajoutPhotoChantierTemoin(event);
            photoChantierTemoin.updatePhotoContainer(((AjoutPhotoEvent) event.getPayload()).getTarget());
        }
    }
}