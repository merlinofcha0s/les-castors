package fr.batimen.web.client.extend.member.client.util;

import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.SuppressionPhotoDTO;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.SuppressionPhotoEvent;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Casaucau on 30/06/2015.
 */
@Named("photoUtils")
public class PhotoUtils implements Serializable{

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    public void suppressionPhoto(IEvent<?> event, String loginDemandeur, String idAnnonce){
        SuppressionPhotoEvent suppressionPhotoEvent = ((SuppressionPhotoEvent) event.getPayload());
        AjaxRequestTarget target = suppressionPhotoEvent.getTarget();
       // String loginDemandeur = authentication.getCurrentUserInfo().getLogin();

        SuppressionPhotoDTO suppressionPhotoDTO = new SuppressionPhotoDTO();
        suppressionPhotoDTO.setHashID(idAnnonce);
        suppressionPhotoDTO.setLoginDemandeur(loginDemandeur);
        suppressionPhotoDTO.setImageASupprimer(suppressionPhotoEvent.getImageASupprimer());

        List<ImageDTO> images = annonceServiceREST.suppressionPhoto(suppressionPhotoDTO);

        if (suppressionPhotoEvent.getNbImagesAvant() != images.size()) {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Suppression effectuée !", FeedbackMessageLevel.SUCCESS));
        } else {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant la suppression de la photo sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
        }
    }
}
