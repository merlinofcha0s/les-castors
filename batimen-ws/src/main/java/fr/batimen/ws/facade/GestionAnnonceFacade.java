package fr.batimen.ws.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.ws.dao.AdresseDAO;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

/**
 * Facade REST de gestion des annonces.
 * 
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionAnnonceFacade")
@LocalBean
@Path(WsPath.GESTION_ANNONCE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionAnnonceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionAnnonceFacade.class);

    @Inject
    private AnnonceDAO annonceDAO;

    @Inject
    private AdresseDAO adresseDAO;

    @Inject
    private ClientDAO clientDAO;

    /**
     * Permet la creation d'une nouvelle annonce par le client ainsi que le
     * compte de ce dernier
     * 
     * @see Constant
     * 
     * @param nouvelleAnnonceDTO
     *            L'objet provenant du frontend qui permet la creation de
     *            l'annonce.
     * @return CODE_SERVICE_RETOUR_KO ou CODE_SERVICE_RETOUR_OK voir la classe
     *         Constant
     */
    @POST
    @Path(WsPath.GESTION_ANNONCE_SERVICE_CREATION_ANNONCE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) {

        try {
            Annonce nouvelleAnnonce = remplirAnnonce(nouvelleAnnonceDTO);
            annonceDAO.saveAnnonce(nouvelleAnnonce);
        } catch (BackendException | DuplicateEntityException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur lors de l'enregistrement de l'annonce", e);
            }
            // L'annonce est deja présente dans le BDD
            if (e instanceof DuplicateEntityException) {
                return Constant.CODE_SERVICE_RETOUR_DUPLICATE;
            }
            // Erreur pendant la creation du service de l'annonce.
            return Constant.CODE_SERVICE_RETOUR_KO;
        }

        return Constant.CODE_SERVICE_RETOUR_OK;
    }

    /**
     * Crée une entité annonce a partir d'une DTO CreationAnnonce.
     * 
     * @param nouvelleAnnonceDTO
     *            L'objet provenant du backend contenant les infos de l'annonce.
     * @return Entité Annonce
     * @throws BackendException
     */
    private Annonce remplirAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) throws BackendException,
            DuplicateEntityException {

        Annonce nouvelleAnnonce = new Annonce();

        // On rempli, on persiste et on bind l'adresse à l'annonce.
        nouvelleAnnonce.setAdresseChantier(remplirAndPersistAdresse(nouvelleAnnonceDTO));

        if (nouvelleAnnonceDTO.getIsSignedUp()) {
            isSignedUp(nouvelleAnnonceDTO, nouvelleAnnonce);
        } else {
            isNotSignedUp(nouvelleAnnonceDTO, nouvelleAnnonce);
        }

        // On rempli l'entité annonce grace à la DTO
        nouvelleAnnonce.setDateCreation(nouvelleAnnonceDTO.getDateInscription());
        nouvelleAnnonce.setDateMAJ(new Date());
        nouvelleAnnonce.setDelaiIntervention(nouvelleAnnonceDTO.getDelaiIntervention());
        nouvelleAnnonce.setDescription(nouvelleAnnonceDTO.getDescription());

        nouvelleAnnonce.setMetier(nouvelleAnnonceDTO.getMetier());
        nouvelleAnnonce.setNbConsultation(0);
        nouvelleAnnonce.setNbDevis(nouvelleAnnonceDTO.getNbDevis());
        nouvelleAnnonce.setTitre(nouvelleAnnonceDTO.getTitre());
        nouvelleAnnonce.setTypeContact(nouvelleAnnonceDTO.getTypeContact());

        return nouvelleAnnonce;
    }

    /**
     * Cette méthode à sert aller chercher un client qui est deja inscit pour le
     * binder à l'annonce.
     * 
     * @param nouvelleAnnonceDTO
     *            objet provenant du front
     * @param nouvelleAnnonce
     *            entité qui sera persisté
     */
    private void isSignedUp(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) throws BackendException,
            DuplicateEntityException {
        Client client = clientDAO.getClientByLoginName(nouvelleAnnonceDTO.getLogin());
        if (client != null) {
            nouvelleAnnonce.setDemandeur(client);
            client.getDevisDemandes().add(nouvelleAnnonce);
            clientDAO.saveClient(client);
            nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
        } else {
            throw new BackendException("Impossible de retrouver le client : " + nouvelleAnnonceDTO.getLogin());
        }
    }

    /**
     * Methode qui permet de populer l'entité client grace à la
     * CreationAnnonceDTO et a la persister dans la BDD. Dans le cas d'une
     * inscription.
     * 
     * @param nouvelleAnnonceDTO
     *            objet provenant du front
     * @param nouvelleAnnonce
     *            entité qui sera persisté
     * @throws BackendException
     *             en cas de probleme de sauvegarde dans la BDD
     */
    private void isNotSignedUp(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) throws BackendException,
            DuplicateEntityException {
        Client nouveauClient = remplirClient(nouvelleAnnonceDTO, nouvelleAnnonce);
        clientDAO.saveNewClient(nouveauClient);
        if (nouveauClient != null) {
            nouvelleAnnonce.setDemandeur(nouveauClient);
            nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.EN_ATTENTE);
        }
    }

    /**
     * Rempli une entité Adresse grace à la DTO de création de l'annonce, puis
     * la persiste
     * 
     * @param nouvelleAnnonce
     * @return
     * @throws BackendException
     */
    private Adresse remplirAndPersistAdresse(CreationAnnonceDTO nouvelleAnnonceDTO) throws BackendException {

        // On crée la nouvelle adresse qui sera rattaché a l'annonce.
        Adresse adresseAnnonce = new Adresse();
        adresseAnnonce.setAdresse(nouvelleAnnonceDTO.getAdresse());
        adresseAnnonce.setComplementAdresse(nouvelleAnnonceDTO.getComplementAdresse());
        adresseAnnonce.setCodePostal(nouvelleAnnonceDTO.getCodePostal());
        adresseAnnonce.setVille(nouvelleAnnonceDTO.getVille());
        adresseAnnonce.setDepartement(nouvelleAnnonceDTO.getDepartement());

        adresseDAO.saveAdresse(adresseAnnonce);

        return adresseAnnonce;

    }

    /**
     * Rempli une entité Client grace à la DTO de création de l'annonce
     * 
     * @param nouvelleAnnonceDTO
     * @param nouvelleAnnonce
     * @return
     */
    private Client remplirClient(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) {

        // On crée la nouveau client qui vient de poster la nouvelle annonce.
        Client nouveauClient = new Client();
        nouveauClient.setCivilite(nouvelleAnnonceDTO.getCivilite());
        nouveauClient.setDateInscription(new Date());

        // On crée la liste des annonces.
        List<Annonce> annoncesNouveauClient = new ArrayList<Annonce>();
        annoncesNouveauClient.add(nouvelleAnnonce);
        // On bind le client à son annonce.
        nouveauClient.setDevisDemandes(annoncesNouveauClient);
        // On enregistre les infos du client dans l'entité client
        nouveauClient.setNom(nouvelleAnnonceDTO.getNom());
        nouveauClient.setPrenom(nouvelleAnnonceDTO.getPrenom());
        nouveauClient.setLogin(nouvelleAnnonceDTO.getLogin());
        nouveauClient.setPassword(nouvelleAnnonceDTO.getPassword());
        nouveauClient.setEmail(nouvelleAnnonceDTO.getEmail());
        nouveauClient.setNumeroTel(nouvelleAnnonceDTO.getNumeroTel());
        nouveauClient.setIsArtisan(false);

        return nouveauClient;
    }
}
