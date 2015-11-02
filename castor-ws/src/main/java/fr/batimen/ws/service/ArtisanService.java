package fr.batimen.ws.service;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.EntrepriseDAO;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.utils.RolesUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;

/**
 * Classe de gestion des artisans
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "ArtisanService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArtisanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanService.class);

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private EntrepriseDAO entrepriseDAO;

    @Inject
    private RolesUtils rolesUtils;

    /**
     * Active le compte d'un artisan
     * 
     * @param artisanByKey
     * @return
     */
    public Integer activateAccount(Artisan artisanByKey) {
        artisanByKey.setIsActive(Boolean.TRUE);
        artisanDAO.update(artisanByKey);
        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Regarde dans la BDD si l'entreprise est presente dans la BDD.
     * 
     * @param siret
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Entreprise checkEntrepriseExiste(String siret) {
        // On check que l'entreprise n'existe pas dans notre BDD
        Entreprise entrepriseExiste = entrepriseDAO.getEntrepriseBySiret(siret);

        if (entrepriseExiste.getNomComplet().isEmpty()) {
            return null;
        }

        return entrepriseExiste;
    }

    /**
     * Regarde dans la BDD si l'artisan est présent
     * 
     * @param email
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Artisan checkArtisanExiste(String email) {
        Artisan artisanExiste = artisanDAO.getArtisanByEmail(email);

        // On check que l'artisan n'existe pas déjà
        if (artisanExiste.getEmail().isEmpty()) {
            return null;
        }

        return artisanExiste;
    }

    /**
     * Methode qui transforme un client DTO en artisan en vue de le persister
     * dans la BDD.<br/>
     * 
     * 
     * @param artisan
     * @param mapper
     * @return
     */
    public Artisan constructionNouveauArtisan(ClientDTO artisan, ModelMapper mapper) {
        Artisan nouveauArtisan = new Artisan();

        // Remplissage automatique des champs commun
        mapper.map(artisan, nouveauArtisan);

        nouveauArtisan.setDateInscription(new Date());

        // Calcul de la clé d'activation du compte
        StringBuilder loginAndEmail = new StringBuilder(artisan.getLogin());
        loginAndEmail.append(artisan.getEmail());
        nouveauArtisan.setCleActivation(HashHelper.convertToBase64(HashHelper.hashSHA256(loginAndEmail.toString())));

        return nouveauArtisan;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Entreprise loadEntrepriseAndCheckRole(String rolesDemandeur, String id, String login){
        if (rolesUtils.checkIfAdminWithString(rolesDemandeur)) {
            return entrepriseDAO.getEntrepriseBySiret(id);
        } else if (rolesUtils.checkIfArtisanWithString(rolesDemandeur)) {
            Entreprise entreprise = entrepriseDAO.getEntrepriseBySiret(id);
            if (entreprise != null && entreprise.getArtisan().getLogin().equals(login)) {
                return entreprise;
            } else {
                return null;
            }
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("N'a pas les bons droits pour accéder à ce service !!!");
                LOGGER.error("Roles : {}", rolesDemandeur);
                LOGGER.error("Hash ID : {}", id);
            }
            return null;
        }
    }
}