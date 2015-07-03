package fr.batimen.web.client.event;

import fr.batimen.dto.ImageDTO;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

/**
 * Created by Casaucau on 30/06/2015.
 */
public class SuppressionPhotoEvent  extends AbstractEvent {

    private final ImageDTO imageASupprimer;
    private final int nbImagesAvant;
    private List<ImageDTO> images;
    private String login;
    private String id;

    AjaxRequestTarget target;

    public SuppressionPhotoEvent(AjaxRequestTarget target, ImageDTO imageASupprimer, int nbImagesAvant, List<ImageDTO> images, String id, String login) {
        super(target);
        this.target = target;
        this.imageASupprimer = imageASupprimer;
        this.nbImagesAvant = nbImagesAvant;
        this.images = images;
        this.login = login;
        this.id = id;
    }

    public ImageDTO getImageASupprimer() {
        return imageASupprimer;
    }

    public int getNbImagesAvant() {
        return nbImagesAvant;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setId(String id) {
        this.id = id;
    }
}