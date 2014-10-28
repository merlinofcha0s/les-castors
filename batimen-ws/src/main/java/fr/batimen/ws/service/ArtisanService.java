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

import fr.batimen.core.constant.Constant;
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

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private EntrepriseDAO entrepriseDAO;

    public Integer activateAccount(Artisan artisanByKey) {
        artisanByKey.setIsActive(Boolean.TRUE);
        artisanDAO.update(artisanByKey);
        return Constant.CODE_SERVICE_RETOUR_OK;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Entreprise checkEntrepriseExiste(String siret) {
        // On check que l'entreprise n'existe pas dans notre BDD
        Entreprise entrepriseExiste = entrepriseDAO.getEntrepriseBySiret(siret);

        if (entrepriseExiste.getNomComplet().isEmpty()) {
            return null;
        }

        return entrepriseExiste;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Artisan checkArtisanExiste(String email) {
        Artisan artisanExiste = artisanDAO.getArtisanByEmail(email);

        // On check que l'artisan n'existe pas déjà
        if (artisanExiste.getEmail().isEmpty()) {
            return null;
        }

        return artisanExiste;
    }

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
