package fr.batimen.web.client.component;


import fr.batimen.dto.NotationDTO;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Created by Casaucau on 04/06/2015.
 */
public class Rater extends Panel {

    private NotationDTO notationDTO;
    private boolean isPageForClient;

    public Rater(String id, NotationDTO notationDTO, boolean isPageForClient) {
        super(id);
        this.notationDTO = notationDTO;
        this.isPageForClient = isPageForClient;
        initComposant();
    }

    private void initComposant() {
        RaterCastor raterCastor = new RaterCastor("raterCastor", false);
        raterCastor.setNumberOfStars(notationDTO.getScore().intValue());
        raterCastor.setIsReadOnly(true);

        Model<String> nomEntrepriseOrClientModel = new Model<>();

        if (isPageForClient) {
            nomEntrepriseOrClientModel.setObject(notationDTO.getNomEntreprise());
        } else {
            nomEntrepriseOrClientModel.setObject(notationDTO.getNomPrenomOrLoginClient());
        }
        Label nomEntrepriseOrClient = new Label("nomEntreprise", nomEntrepriseOrClientModel);
        Label commentaireClient = new Label("commentaireClient", notationDTO.getCommentaire());

        add(raterCastor, nomEntrepriseOrClient, commentaireClient);
    }


}
