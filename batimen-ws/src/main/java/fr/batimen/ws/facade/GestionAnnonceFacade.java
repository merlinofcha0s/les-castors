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

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.core.exception.BackendException;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

@Stateless(name = "GestionAnnonceFacade")
@LocalBean
@Path(WsPath.GESTION_ANNONCE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionAnnonceFacade {

	@Inject
	private AnnonceDAO annonceDAO;

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

		Annonce nouvelleAnnonce = remplirAnnonce(nouvelleAnnonceDTO);

		try {
			annonceDAO.saveAnnonce(nouvelleAnnonce);
		} catch (BackendException e) {
			// Annonce deja existante en BDD
			return Constant.CODE_SERVICE_RETOUR_KO;
		}

		return Constant.CODE_SERVICE_RETOUR_OK;
	}

	private Annonce remplirAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) {

		Annonce nouvelleAnnonce = new Annonce();

		// On la bind a l'annonce
		nouvelleAnnonce.setAdresseChantier(remplirAdresse(nouvelleAnnonceDTO, nouvelleAnnonce));
		nouvelleAnnonce.setDemandeur(remplirClient(nouvelleAnnonceDTO, nouvelleAnnonce));

		// On rempli l'entité annonce grace à la DTO
		nouvelleAnnonce.setDateCreation(new Date());
		nouvelleAnnonce.setDateMAJ(new Date());
		nouvelleAnnonce.setDelaiIntervention(nouvelleAnnonceDTO.getDelaiIntervention());
		nouvelleAnnonce.setDescription(nouvelleAnnonceDTO.getDescription());

		// On attendra que le client confirme son email avant d'activer
		// l'annonce.
		if (nouvelleAnnonceDTO.getIsSignedUp()) {
			nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
		} else {
			nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.EN_ATTENTE);
		}

		nouvelleAnnonce.setMetier(nouvelleAnnonceDTO.getMetier());
		nouvelleAnnonce.setNbConsultation(0);
		nouvelleAnnonce.setNbDevis(nouvelleAnnonceDTO.getNbDevis());
		nouvelleAnnonce.setTitre(nouvelleAnnonceDTO.getTitre());
		nouvelleAnnonce.setTypeContact(nouvelleAnnonceDTO.getTypeContact());

		return nouvelleAnnonce;
	}

	private Adresse remplirAdresse(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) {

		// On crée la nouvelle adresse qui sera rattaché a l'annonce.
		Adresse adresseAnnonce = new Adresse();
		adresseAnnonce.setAdresse(nouvelleAnnonceDTO.getAdresse());
		adresseAnnonce.setComplementAdresse(nouvelleAnnonceDTO.getComplementAdresse());
		adresseAnnonce.setCodePostal(nouvelleAnnonceDTO.getCodePostal());
		adresseAnnonce.setVille(nouvelleAnnonceDTO.getVille());
		adresseAnnonce.setDepartement(nouvelleAnnonceDTO.getDepartement());
		adresseAnnonce.setAnnonce(nouvelleAnnonce);

		return adresseAnnonce;

	}

	private Client remplirClient(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) {

		// On crée la nouveau client qui vient de poster la nouvelle annonce.
		Client nouveauClient = new Client();
		nouveauClient.setCivilite(nouvelleAnnonceDTO.getCivilite());
		nouveauClient.setDateInscription(new Date());

		// On crée la liste des annonces.
		List<Annonce> annoncesNouveauClient = new ArrayList<Annonce>();
		annoncesNouveauClient.add(nouvelleAnnonce);
		// On bind le client a son annonce.
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
