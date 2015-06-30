package fr.batimen.web.client.event;

import fr.batimen.dto.ImageDTO;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by Casaucau on 30/06/2015.
 */
public class SuppressionPhotoEvent  extends AbstractEvent {

    private final ImageDTO imageASupprimer;
    private final int nbImagesAvant;

    public SuppressionPhotoEvent(AjaxRequestTarget target, ImageDTO imageASupprimer, int nbImagesAvant) {
        super(target);
        this.imageASupprimer = imageASupprimer;
        this.nbImagesAvant = nbImagesAvant;
    }

    public ImageDTO getImageASupprimer() {
        return imageASupprimer;
    }

    public int getNbImagesAvant() {
        return nbImagesAvant;
    }
}
