package fr.castor.ws.service;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.dto.AvisDTO;
import fr.castor.ws.dao.AnnonceDAO;
import fr.castor.ws.dao.NotationDAO;
import fr.castor.ws.entity.Annonce;
import fr.castor.ws.entity.Artisan;
import fr.castor.ws.entity.Avis;
import fr.castor.ws.entity.Client;
import fr.castor.ws.utils.ClientUtils;
import org.modelmapper.ModelMapper;

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

    @Inject
    private NotationDAO notationDAO;

    @Inject
    private AnnonceDAO annonceDAO;

    /**
     * Crée une entité Avis et la persiste dans la BDD
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

        Avis nouvelleAvis = new Avis();
        nouvelleAvis.setAnnonce(annonce);
        nouvelleAvis.setArtisan(artisan);
        nouvelleAvis.setCommentaire(commentaire);
        nouvelleAvis.setScore(score);
        nouvelleAvis.setDateAvis(new Date());

        nouvelleAvis = notationDAO.createMandatory(nouvelleAvis);

        annonce.setAvis(nouvelleAvis);
        annonceDAO.update(annonce);

        if (nouvelleAvis != null) {
            return CodeRetourService.RETOUR_OK;
        } else {
            return CodeRetourService.RETOUR_KO;
        }
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public List<AvisDTO> getNotationBySiret(String siret, int maxResult){
        List<AvisDTO> notationsDTO = new ArrayList<>();

        List<Avis> listAvis = notationDAO.getNotationByEntrepriseSiret(siret, maxResult);

        ModelMapper mapper = new ModelMapper();

        for(Avis avis : listAvis){
            AvisDTO notationDTO = mapper.map(avis, AvisDTO.class);
            notationDTO.setNomEntreprise(avis.getArtisan().getEntreprise().getNomComplet());

            Client client = avis.getAnnonce().getDemandeur();
            StringBuilder nomClient = new StringBuilder();
            ClientUtils.chooseNomClient(client.getNom(), client.getPrenom(), client.getLogin(), nomClient);

            notationDTO.setNomPrenomOrLoginClient(nomClient.toString());

            notationsDTO.add(notationDTO);
        }
        return notationsDTO;
    }
}