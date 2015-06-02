package fr.batimen.ws.service;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.ClientDTO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.EntrepriseDAO;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;

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

}