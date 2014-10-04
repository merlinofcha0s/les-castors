package fr.batimen.ws.facade;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.EmailException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.core.utils.PropertiesUtils;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.ws.dao.AdresseDAO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.CategorieMetierDAO;
import fr.batimen.ws.dao.EntrepriseDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.CategorieMetier;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;
import fr.batimen.ws.service.EmailService;

/**
 * Facade REST de gestion des artisans
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionArtisanFacade")
@LocalBean
@Path(WsPath.GESTION_PARTENAIRE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
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
    private EmailService emailService;

    /**
     * Service de création d'un nouveau partenaire Artisan
     * 
     * @param nouveauPartenaireDTO
     *            l'objet contenant l'ensemble des informations.
     * @return voir la classe : {@link Constant}
     */
    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationArtisan(CreationPartenaireDTO nouveauPartenaireDTO) {
        ModelMapper mapper = new ModelMapper();

        Artisan artisanExiste = checkArtisanExiste(nouveauPartenaireDTO.getArtisan().getEmail());

        if (artisanExiste != null) {
            return Constant.CODE_SERVICE_RETOUR_KO;
        }

        Artisan nouveauArtisan = constructionNouveauArtisan(nouveauPartenaireDTO.getArtisan(), mapper);

        Entreprise entrepriseExiste = checkEntrepriseExiste(nouveauPartenaireDTO.getEntreprise().getSiret());

        if (entrepriseExiste != null) {
            return Constant.CODE_SERVICE_RETOUR_KO;
        }

        // On init l'entité et on la rempli avec les champs de la DTO
        Entreprise nouvelleEntreprise = new Entreprise();
        Adresse nouvelleAdresse = new Adresse();
        // Remplissage automatique des champs commun
        mapper.map(nouveauPartenaireDTO.getEntreprise(), nouvelleEntreprise);
        mapper.map(nouveauPartenaireDTO.getAdresse(), nouvelleAdresse);

        List<CategorieMetierDTO> categories = nouveauPartenaireDTO.getEntreprise().getCategoriesMetier();

        nouvelleEntreprise.setAdresse(nouvelleAdresse);
        nouveauArtisan.setEntreprise(nouvelleEntreprise);

        artisanDAO.saveArtisan(nouveauArtisan);
        entrepriseDAO.saveEntreprise(nouvelleEntreprise);

        for (CategorieMetierDTO categorieDTO : categories) {
            CategorieMetier nouvelleCategorieMetier = new CategorieMetier();
            nouvelleCategorieMetier.setCategorieMetier(categorieDTO.getCodeCategorieMetier());
            mapper.map(categorieDTO, nouvelleCategorieMetier);
            nouvelleCategorieMetier.setEntreprise(nouvelleEntreprise);
            categorieMetierDAO.persistCategorieMetier(nouvelleCategorieMetier);
        }

        try {
            adresseDAO.saveAdresse(nouvelleAdresse);
        } catch (BackendException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("L'adresse existe déjà dans la BDD ", e);
            }
            return Constant.CODE_SERVICE_RETOUR_KO;
        }
        // On recupere l'url du frontend
        Properties urlProperties = PropertiesUtils.loadPropertiesFile("url.properties");
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

        return Constant.CODE_SERVICE_RETOUR_OK;
    }

    private Entreprise checkEntrepriseExiste(String siret) {
        // On check que l'entreprise n'existe pas dans notre BDD
        Entreprise entrepriseExiste = entrepriseDAO.getEntrepriseBySiret(siret);

        if (entrepriseExiste.getNomComplet().isEmpty()) {
            return null;
        }

        return entrepriseExiste;
    }

    private Artisan checkArtisanExiste(String email) {
        Artisan artisanExiste = artisanDAO.getArtisanByEmail(email);

        // On check que l'artisan n'existe pas déjà
        if (artisanExiste.getEmail().isEmpty()) {
            return null;
        }

        return artisanExiste;
    }

    private Artisan constructionNouveauArtisan(ClientDTO artisan, ModelMapper mapper) {
        Artisan nouveauArtisan = new Artisan();

        // Remplissage automatique des champs commun
        mapper.map(artisan, nouveauArtisan);

        nouveauArtisan.setDateInscription(new Date());
        nouveauArtisan.setTypeCompte(TypeCompte.DEFAULT_ARTISAN);

        // Calcul de la clé d'activation du compte
        StringBuilder loginAndEmail = new StringBuilder(artisan.getLogin());
        loginAndEmail.append(artisan.getEmail());
        nouveauArtisan.setCleActivation(HashHelper.convertToBase64(HashHelper.hashSHA256(loginAndEmail.toString())));

        return nouveauArtisan;
    }
}
