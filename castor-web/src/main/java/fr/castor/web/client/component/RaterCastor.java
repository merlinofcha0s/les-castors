package fr.castor.web.client.component;


import fr.castor.dto.AvisDTO;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.text.SimpleDateFormat;

/**
 * Composant qui affiche l'avis + commentaire d'un artisan
 *
 * @author Cyril Casaucau
 */
public class RaterCastor extends Panel {

    private AvisDTO notationDTO;
    private boolean isPageForClient;

    public RaterCastor(String id, AvisDTO notationDTO, boolean isPageForClient) {
        super(id);
        this.notationDTO = notationDTO;
        this.isPageForClient = isPageForClient;
        initComposant();
    }

    private void initComposant() {
        RaterStarsCastor raterCastor = new RaterStarsCastor("raterCastor", false);
        raterCastor.setNumberOfStars(notationDTO.getScore().intValue());
        raterCastor.setIsReadOnly(true);

        Model<String> nomEntrepriseOrClientModel = new Model<>();

        if (isPageForClient) {
            nomEntrepriseOrClientModel.setObject(notationDTO.getNomEntreprise());
        } else {
            nomEntrepriseOrClientModel.setObject(notationDTO.getNomPrenomOrLoginClient());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Label dateAvis = new Label("dateAvis", new Model<>(sdf.format(notationDTO.getDateAvis())));
        Label nomEntrepriseOrClient = new Label("nomEntreprise", nomEntrepriseOrClientModel);
        Label commentaireClient = new Label("commentaireClient", notationDTO.getCommentaire());

        add(raterCastor, nomEntrepriseOrClient, commentaireClient, dateAvis);
    }


}
