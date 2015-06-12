package fr.batimen.ws.service;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.NotationDTO;
import fr.batimen.ws.dao.NotationDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.entity.Notation;
import fr.batimen.ws.utils.ClientUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<NotationDTO> getNotationBySiret(String siret, int maxResult){
        List<NotationDTO> notationsDTO = new ArrayList<>();

        List<Notation> notations = notationDAO.getNotationByEntrepriseSiret(siret, maxResult);

        ModelMapper mapper = new ModelMapper();

        for(Notation notation : notations){
            NotationDTO notationDTO = mapper.map(notation, NotationDTO.class);
            notationDTO.setNomEntreprise(notation.getArtisan().getEntreprise().getNomComplet());

            Client client = notation.getAnnonce().getDemandeur();
            StringBuilder nomClient = new StringBuilder();
            ClientUtils.chooseNomClient(client.getNom(), client.getPrenom(), client.getLogin(), nomClient);

            notationDTO.setNomPrenomOrLoginClient(nomClient.toString());

            notationsDTO.add(notationDTO);
        }
        return notationsDTO;
    }
}