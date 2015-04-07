package fr.batimen.ws.service;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.ws.dao.NotationDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Notation;

/**
 * Classe de gestion des notes
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "NotationService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotationService.class);

    @Inject
    private NotationDAO notationDAO;

    /**
     * Crée une entité Notation et la persiste dans la BDD
     * 
     * @param annonce
     *            L'annonce noté
     * @param artisan
     *            L'artisan qui va être noté
     * @param commentaire
     *            Le commentaire de la note
     * @param score
     *            La note
     * @return {@link CodeRetourService}
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public int noterArtisanService(Annonce annonce, Artisan artisan, String commentaire, Double score) {

        Notation nouvelleNotation = new Notation();
        nouvelleNotation.setAnnonce(annonce);
        nouvelleNotation.setArtisan(artisan);
        nouvelleNotation.setCommentaire(commentaire);
        nouvelleNotation.setScore(score);
        nouvelleNotation.setDateNotation(new Date());

        nouvelleNotation = notationDAO.createMandatory(nouvelleNotation);

        if (nouvelleNotation != null) {
            return CodeRetourService.RETOUR_OK;
        } else {
            return CodeRetourService.RETOUR_KO;
        }

    }
}