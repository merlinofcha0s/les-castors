package fr.batimen.ws.facade;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.EmailException;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.AvisDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.*;
import fr.batimen.ws.entity.*;
import fr.batimen.ws.enums.PropertiesFileWS;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.ArtisanService;
import fr.batimen.ws.service.EmailService;
import fr.batimen.ws.service.EntrepriseService;
import fr.batimen.ws.service.NotationService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Facade REST de gestion des artisans
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "GestionArtisanFacade")
@LocalBean
@Path(WsPath.GESTION_PARTENAIRE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = {BatimenInterceptor.class})
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionArtisanFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionArtisanFacade.class);

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private EntrepriseDAO entrepriseDAO;

    @Inject
    private AdresseDAO adresseDAO;

    @Inject
    private CategorieMetierDAO categorieMetierDAO;

    @Inject
    private PermissionDAO permissionDAO;

    @Inject
    private EmailService emailService;

    @Inject
    private ArtisanService artisanService;

    @Inject
    private EntrepriseService entrepriseService;

    @Inject
    private NotationService notationService;

    /**
     * Service de création d'un nouveau partenaire Artisan
     *
     * @param nouveauPartenaireDTO l'objet contenant l'ensemble des informations.
     * @return voir la classe : {@link Constant}
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationArtisan(CreationPartenaireDTO nouveauPartenaireDTO) {
        ModelMapper mapper = new ModelMapper();

        Artisan artisanExiste = artisanService.checkArtisanExiste(nouveauPartenaireDTO.getArtisan().getEmail());

        if (artisanExiste != null) {
            return CodeRetourService.RETOUR_KO;
        }

        Artisan nouveauArtisan = artisanService.constructionNouveauArtisan(nouveauPartenaireDTO.getArtisan(), mapper);

        Entreprise entrepriseExiste = artisanService.checkEntrepriseExiste(nouveauPartenaireDTO.getEntreprise()
                .getSiret());

        if (entrepriseExiste != null) {
            return CodeRetourService.RETOUR_KO;
        }

        // On init l'entité et on la rempli avec les champs de la DTO
        Entreprise nouvelleEntreprise = new Entreprise();
        Adresse nouvelleAdresse = new Adresse();
        // Remplissage automatique des champs commun
        mapper.map(nouveauPartenaireDTO.getEntreprise(), nouvelleEntreprise);
        mapper.map(nouveauPartenaireDTO.getAdresse(), nouvelleAdresse);

        // On set les permissions
        Permission permission = new Permission();
        permission.setTypeCompte(TypeCompte.ARTISAN);
        permission.setArtisan(nouveauArtisan);
        nouveauArtisan.getPermission().add(permission);

        List<CategorieMetierDTO> categories = nouveauPartenaireDTO.getEntreprise().getCategoriesMetier();

        try {
            adresseDAO.saveAdresse(nouvelleAdresse);
        } catch (BackendException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("L'adresse existe déjà dans la BDD ", e);
            }
            return CodeRetourService.RETOUR_KO;
        }

        nouvelleEntreprise.setAdresse(nouvelleAdresse);
        nouvelleEntreprise.setIsVerifier(false);

        nouveauArtisan.setEntreprise(nouvelleEntreprise);
        artisanDAO.saveArtisan(nouveauArtisan);
        permissionDAO.creationPermission(permission);

        entrepriseDAO.saveEntreprise(nouvelleEntreprise);

        for (CategorieMetierDTO categorieDTO : categories) {
            CategorieMetier nouvelleCategorieMetier = new CategorieMetier();
            nouvelleCategorieMetier.setCategorieMetier(categorieDTO.getCodeCategorieMetier());
            mapper.map(categorieDTO, nouvelleCategorieMetier);
            nouvelleCategorieMetier.setEntreprise(nouvelleEntreprise);
            categorieMetierDAO.persistCategorieMetier(nouvelleCategorieMetier);
        }

        // On recupere l'url du frontend
        Properties urlProperties = PropertiesFileWS.URL.getProperties();
        String urlFrontend = urlProperties.getProperty("url.frontend.web");
        try {
            emailService.envoiMailActivationCompte(nouveauPartenaireDTO.getArtisan().getNom(), nouveauPartenaireDTO
                    .getArtisan().getPrenom(), nouveauPartenaireDTO.getArtisan().getLogin(), nouveauPartenaireDTO
                    .getArtisan().getEmail(), nouveauArtisan.getCleActivation(), urlFrontend);
        } catch (EmailException | MandrillApiError | IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Problème d'envoi de mail de confirmation pour un artisan", e);
            }
        }

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Permet de récuperer les informations d'une entreprise (Infos + adresse)
     *
     * @param login Le login de l'artisan dont on veut l'entreprise
     * @return Les informations de l'entreprise.
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_GET_ENTREPISE_INFORMATION_BY_LOGIN)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public EntrepriseDTO getEntrepriseInformationByArtisanLogin(String login) {
        Entreprise entreprise = entrepriseDAO.getEntrepriseByArtisan(DeserializeJsonHelper.parseString(login));
        return entrepriseService.rempliEntrepriseInformation(entreprise);
    }

    /**
     * Permet de récuperer les informations d'une entreprise (Infos + adresse)
     *
     * @param entrepriseDTO Les informations de l'entreprise que l'on doit modifier
     * @return CodeRetourService
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_SAVE_ENTREPRISE_INFORMATION)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer saveEntrepriseInformation(EntrepriseDTO entrepriseDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(entrepriseDTO.toString());
        }

        Entreprise entrepriseAMettreAJour = entrepriseDAO.getArtisanByNomCompletSatutSiretDepartement(entrepriseDTO.getNomComplet()
                , entrepriseDTO.getStatutJuridique(), entrepriseDTO.getSiret()
                , entrepriseDTO.getAdresseEntreprise().getDepartement());

        if (entrepriseAMettreAJour != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("L'entreprise a été trouvée, on effectue les modifs demandé par l'utilisateur.");
            }
            entrepriseAMettreAJour.setNbEmployees(entrepriseDTO.getNbEmployees());
            entrepriseAMettreAJour.setDateCreation(entrepriseDTO.getDateCreation());
            entrepriseAMettreAJour.setSpecialite(entrepriseDTO.getSpecialite());
            ModelMapper mapper = new ModelMapper();
            mapper.map(entrepriseDTO.getAdresseEntreprise(), entrepriseAMettreAJour.getAdresse());

            categorieMetierDAO.updateCategorieMetier(entrepriseDTO, entrepriseAMettreAJour);
            entrepriseDAO.update(entrepriseAMettreAJour);
            adresseDAO.update(entrepriseAMettreAJour.getAdresse());
            return CodeRetourService.RETOUR_OK;
        } else {
            return CodeRetourService.RETOUR_KO;
        }
    }

    /**
     * Permet de récuperer les informations d'une entreprise (Infos + adresse)
     *
     * @param siret Le siret de l'entreprise
     * @return Les informations de l'entreprise.
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_GET_ENTREPISE_INFORMATION_BY_SIRET)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public EntrepriseDTO getEntrepriseInformationBySiret(String siret) {
        String siretEscaped = DeserializeJsonHelper.parseString(siret);
        Entreprise entreprise = entrepriseDAO.getEntrepriseBySiret(DeserializeJsonHelper.parseString(siret));
        EntrepriseDTO entrepriseDTO = entrepriseService.rempliEntrepriseInformation(entreprise);
        entrepriseDTO.getNotationsDTO().addAll(notationService.getNotationBySiret(siretEscaped, 2));

        //Stats
        Double moyenneAvis = 0.0;
        int nbAvis = 0;
        for(AvisDTO notationDTO : entrepriseDTO.getNotationsDTO()){
            moyenneAvis += notationDTO.getScore();
            nbAvis++;
        }

        moyenneAvis = moyenneAvis / nbAvis;

        entrepriseDTO.setMoyenneAvis(moyenneAvis);
        entrepriseDTO.setNbAnnonce(entreprise.getAnnonceEntrepriseSelectionnee().size());

        return entrepriseDTO;
    }

    /**
     * Permet de récuperer tous les avis d'une entreprise par son SIRETs
     *
     * @param siret Le siret de l'entreprise
     * @return Les informations de l'entreprise.
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_GET_NOTATION_BY_SIRET)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<AvisDTO> getEntrepriseNotationBySiret(String siret) {
        String siretEscaped = DeserializeJsonHelper.parseString(siret);
        return notationService.getNotationBySiret(siretEscaped, 0);
    }
}