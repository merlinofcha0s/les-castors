package fr.batimen.ws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.ws.dao.AdresseDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.dao.PermissionDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.entity.Permission;

/**
 * Classe de gestion des annonces
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "AnnonceService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnnonceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceService.class);

    @Inject
    private AdresseDAO adresseDAO;

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private PermissionDAO permissionDAO;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * Crée une entité annonce a partir d'une DTO CreationAnnonce.
     * 
     * @param nouvelleAnnonceDTO
     *            L'objet provenant du backend contenant les infos de l'annonce.
     * @return Entité Annonce
     * @throws BackendException
     */
    public Annonce remplirAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) throws BackendException,
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
        nouvelleAnnonce.setDateCreation(new Date());
        nouvelleAnnonce.setDateMAJ(new Date());
        nouvelleAnnonce.setDelaiIntervention(nouvelleAnnonceDTO.getDelaiIntervention());
        nouvelleAnnonce.setDescription(nouvelleAnnonceDTO.getDescription());

        nouvelleAnnonce.setCategorieMetier(nouvelleAnnonceDTO.getCategorieMetier().getName());
        nouvelleAnnonce.setSousCategorieMetier(nouvelleAnnonceDTO.getSousCategorie().getName());
        nouvelleAnnonce.setNbConsultation(0);
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
        Client client = clientDAO.getClientByLoginName(nouvelleAnnonceDTO.getClient().getLogin());
        if (client != null) {
            nouvelleAnnonce.setDemandeur(client);
            client.getDevisDemandes().add(nouvelleAnnonce);
            clientDAO.saveClient(client);
            nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
        } else {
            throw new BackendException("Impossible de retrouver le client : "
                    + nouvelleAnnonceDTO.getClient().getLogin());
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
        // On set les permissions
        for (PermissionDTO permission : nouvelleAnnonceDTO.getClient().getPermissions()) {
            Permission nouvellePermission = new Permission();
            nouvellePermission.setTypeCompte(permission.getTypeCompte());
            nouvellePermission.setClient(nouveauClient);
            permissionDAO.creationPermission(nouvellePermission);
        }
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
        nouveauClient.setDateInscription(new Date());

        // On crée la liste des annonces.
        List<Annonce> annoncesNouveauClient = new ArrayList<Annonce>();
        annoncesNouveauClient.add(nouvelleAnnonce);

        // On bind le client à son annonce.
        nouveauClient.setDevisDemandes(annoncesNouveauClient);
        // On enregistre les infos du client dans l'entité client
        mapper.map(nouvelleAnnonceDTO.getClient(), nouveauClient);

        nouveauClient.setIsActive(false);

        StringBuilder loginAndEmail = new StringBuilder(nouvelleAnnonceDTO.getClient().getLogin());
        loginAndEmail.append(nouvelleAnnonceDTO.getClient().getEmail());

        nouveauClient.setCleActivation(HashHelper.convertToBase64(HashHelper.hashSHA256(loginAndEmail.toString())));

        return nouveauClient;
    }

}
