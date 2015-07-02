package fr.batimen.web.client.event;

import fr.batimen.dto.ImageDTO;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

import java.util.List;

/**
 * Created by Casaucau on 30/06/2015.
 */
public class AjoutPhotoEvent extends AbstractEvent {

    private String loginDemandeur;
    private String id;
    private FileUploadField photoField;
    private FileFieldValidatorAndLoaderBehaviour fileFieldValidatorBehaviour;
    private List<ImageDTO> imageDTOs;
    private int nbImages;

    public AjoutPhotoEvent(AjaxRequestTarget target, String loginDemandeur, String idAnnonce, FileUploadField photoField
            , FileFieldValidatorAndLoaderBehaviour fileFieldValidatorBehaviour, List<ImageDTO> imageDTOs, int nbImages) {
        super(target);
        this.loginDemandeur = loginDemandeur;
        this.id = idAnnonce;
        this.photoField = photoField;
        this.fileFieldValidatorBehaviour = fileFieldValidatorBehaviour;
        this.imageDTOs = imageDTOs;
        this.nbImages = nbImages;
    }

    public String getLoginDemandeur() {
        return loginDemandeur;
    }

    public String getId() {
        return id;
    }

    public FileUploadField getPhotoField() {
        return photoField;
    }

    public FileFieldValidatorAndLoaderBehaviour getFileFieldValidatorBehaviour() {
        return fileFieldValidatorBehaviour;
    }

    public List<ImageDTO> getImageDTOs() {
        return imageDTOs;
    }

    public int getNbImages() {
        return nbImages;
    }
}