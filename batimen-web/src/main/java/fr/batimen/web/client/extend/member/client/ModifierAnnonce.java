package fr.batimen.web.client.extend.member.client;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.SousCategorieMetierDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.aggregate.ModificationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.PhotosContainer;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.ModificationAnnonceEvent;
import fr.batimen.web.client.extend.nouveau.devis.Etape3AnnonceForm;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Page permettant aux clients ou aux admins de modifier une annonce postée par un client
 * <p/>
 * Created by Casaucau on 17/04/2015.
 * <p/>
 * TODO : L'annonce doit etre active, en attente, ou quotas max sinon refus d'accés à la page
 *
 * @author Casaucau Cyril
 */
public class ModifierAnnonce extends MasterPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierAnnonce.class);

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    @Inject
    private Authentication authentication;

    private Etape3AnnonceForm etape3AnnonceForm;

    private AnnonceAffichageDTO annonceAffichageDTO;
    private CompoundPropertyModel propertyModelModificationAnnonce;

    private List<SousCategorieMetierDTO> sousCategorieMetierDTOList;
    private SousCategorieMetierDTO sousCategorieMetierDTO;

    private PhotosContainer photosContainer;

    private String idAnnonce;

    public ModifierAnnonce() {
        this((AnnonceAffichageDTO) null);
    }

    public ModifierAnnonce(AnnonceAffichageDTO annonceAffichageDTO) {
        super("Modifier mon annonce", "lol", "Modifier mon annonce", true, "img/bg_title1.jpg");
        this.annonceAffichageDTO = annonceAffichageDTO;

        if (annonceAffichageDTO == null
                || !annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.DESACTIVE)
                || !annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.A_NOTER)
                || !annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.SUPPRIMER)
                || !annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.TERMINER)) {
            this.setResponsePage(MesAnnonces.class);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page de modification de mon profil");
        }

        initData();
        initComposant();
    }

    public ModifierAnnonce(String idAnnonce, AnnonceAffichageDTO annonceAffichageDTO) {
        this(annonceAffichageDTO);
        this.idAnnonce = idAnnonce;
        photosContainer.setIdAnnonce(idAnnonce);
        etape3AnnonceForm.setIdAnnonce(idAnnonce);
    }

    public ModifierAnnonce(PageParameters params) {
        this((AnnonceAffichageDTO) null);
    }


    private void initComposant() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des composants de la page de modification de mon annonce");
        }
        Profil profil = new Profil("profil");
        etape3AnnonceForm = new Etape3AnnonceForm("formQualification", propertyModelModificationAnnonce, sousCategorieMetierDTOList, sousCategorieMetierDTO);

        photosContainer = new PhotosContainer("afficheurPhotos", annonceAffichageDTO.getImages(), "Les photos de votre annonce", "h4", true);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, etape3AnnonceForm, photosContainer, contactezNous, commentaire);
    }

    private void initData() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des datas de la page de modification de mon annonce");
        }

        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        ModelMapper mapper = new ModelMapper();
        mapper.map(annonceAffichageDTO.getAdresse(), creationAnnonceDTO);
        mapper.map(annonceAffichageDTO.getAnnonce(), creationAnnonceDTO);

        CategorieMetierDTO categorieMetierDTO = CategorieLoader.getCategorieByCode(annonceAffichageDTO.getAnnonce().getCategorieMetier());
        creationAnnonceDTO.setCategorieMetier(categorieMetierDTO);

        sousCategorieMetierDTOList = categorieMetierDTO.getSousCategories();

        for (SousCategorieMetierDTO sousCategorieMetierDTOPossible : sousCategorieMetierDTOList) {
            if (sousCategorieMetierDTOPossible.getName().equals(annonceAffichageDTO.getAnnonce().getSousCategorieMetier())) {
                sousCategorieMetierDTO = new SousCategorieMetierDTO(annonceAffichageDTO.getAnnonce().getSousCategorieMetier());
            }
        }
        propertyModelModificationAnnonce = new CompoundPropertyModel<CreationAnnonceDTO>(creationAnnonceDTO);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        if (event.getPayload() instanceof ModificationAnnonceEvent) {
            //Extraction des infos
            ModificationAnnonceEvent modificationAnnonceEvent = (ModificationAnnonceEvent) event.getPayload();
            CreationAnnonceDTO creationAnnonceDTO = modificationAnnonceEvent.getCreationAnnonceDTO();

            ModelMapper mapper = new ModelMapper();

            //Init des objets
            ModificationAnnonceDTO modificationAnnonceDTO = new ModificationAnnonceDTO();
            modificationAnnonceDTO.setAnnonce(new AnnonceDTO());
            modificationAnnonceDTO.setAdresse(new AdresseDTO());


            //Remplissage des DTOs
            mapper.map(creationAnnonceDTO, modificationAnnonceDTO.getAnnonce());
            mapper.map(creationAnnonceDTO, modificationAnnonceDTO.getAdresse());

            //Champs non mappés
            modificationAnnonceDTO.setLoginDemandeur(authentication.getCurrentUserInfo().getLogin());
            modificationAnnonceDTO.getAnnonce().setSousCategorieMetier(creationAnnonceDTO.getSousCategorie().getName());
            modificationAnnonceDTO.getAnnonce().setDateMAJ(new Date());
            modificationAnnonceDTO.getAnnonce().setEtatAnnonce(annonceAffichageDTO.getAnnonce().getEtatAnnonce());
            modificationAnnonceDTO.getAnnonce().setDateCreation(annonceAffichageDTO.getAnnonce().getDateCreation());
            modificationAnnonceDTO.getAnnonce().setHashID(idAnnonce);
            modificationAnnonceDTO.getAnnonce().setNbConsultation(annonceAffichageDTO.getAnnonce().getNbConsultation());
            modificationAnnonceDTO.getAnnonce().setNbDevis(annonceAffichageDTO.getAnnonce().getNbDevis());

            //Appel du service
            Integer codeRetourService = annonceServiceREST.modifierAnnonce(modificationAnnonceDTO);

            if (codeRetourService.equals(CodeRetourService.RETOUR_OK)) {
                feedBackPanelGeneral.sendMessage("Votre annonce a été modifiée avec succés !", FeedbackMessageLevel.SUCCESS);
            } else {
                feedBackPanelGeneral.sendMessage("Problème lors de l'enregistrement de l'annonce, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR);
            }

            modificationAnnonceEvent.getTarget().add(feedBackPanelGeneral);
        }
    }
}