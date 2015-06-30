package fr.batimen.web.client.extend.member.client.util;

import fr.batimen.core.enums.PropertiesFileGeneral;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AjoutPhotoDTO;
import fr.batimen.dto.aggregate.SuppressionPhotoDTO;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import fr.batimen.web.client.event.AjoutPhotoEvent;
import fr.batimen.web.client.event.ClearFeedbackPanelEvent;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.SuppressionPhotoEvent;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Casaucau on 30/06/2015.
 */
@Named("photoUtils")
public class PhotoServiceAjaxLogic implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceAjaxLogic.class);

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    public void suppressionPhoto(IEvent<?> event) {
        SuppressionPhotoEvent suppressionPhotoEvent = ((SuppressionPhotoEvent) event.getPayload());
        AjaxRequestTarget target = suppressionPhotoEvent.getTarget();
        List<ImageDTO> imageDTOs = suppressionPhotoEvent.getImages();
        String loginDemandeur = suppressionPhotoEvent.getLogin();
        String idAnnonce = suppressionPhotoEvent.getIdAnnonce();

        SuppressionPhotoDTO suppressionPhotoDTO = new SuppressionPhotoDTO();
        suppressionPhotoDTO.setHashID(idAnnonce);
        suppressionPhotoDTO.setLoginDemandeur(loginDemandeur);
        suppressionPhotoDTO.setImageASupprimer(suppressionPhotoEvent.getImageASupprimer());

        imageDTOs.clear();
        imageDTOs.addAll(annonceServiceREST.suppressionPhoto(suppressionPhotoDTO));

        if (suppressionPhotoEvent.getNbImagesAvant() != imageDTOs.size()) {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Suppression effectuée !", FeedbackMessageLevel.SUCCESS));
        } else {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant la suppression de la photo sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
        }
    }

    public void ajoutPhoto(IEvent<?> event) {
        AjoutPhotoEvent ajoutPhotoEvent = ((AjoutPhotoEvent) event.getPayload());
        AjaxRequestTarget target = ajoutPhotoEvent.getTarget();

        String loginDemandeur = ajoutPhotoEvent.getLoginDemandeur();
        String idAnnonce = ajoutPhotoEvent.getIdAnnonce();
        FileUploadField photoField = ajoutPhotoEvent.getPhotoField();
        FileFieldValidatorAndLoaderBehaviour fileFieldValidatorBehaviour = ajoutPhotoEvent.getFileFieldValidatorBehaviour();
        List<ImageDTO> images = ajoutPhotoEvent.getImageDTOs();

        final AjoutPhotoDTO ajoutImageDTO = new AjoutPhotoDTO();
        for (FileUpload photo : photoField.getFileUploads()) {
            try {
                ajoutImageDTO.getImages().add(photo.writeToTempFile());
            } catch (IOException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Problème durant l'écriture de la photo sur le disque", e);
                }
            }
        }

        if (!fileFieldValidatorBehaviour.isValidationOK()) {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Validation erronnée de vos photos, veuillez corriger", FeedbackMessageLevel.ERROR));
        } else {

            //Ajout des photos
            ajoutImageDTO.setHashID(idAnnonce);
            ajoutImageDTO.setLoginDemandeur(loginDemandeur);
            int sizeBefore = ajoutPhotoEvent.getNbImages();
            images.clear();
            images.addAll(annonceServiceREST.ajouterPhoto(ajoutImageDTO));

            if (sizeBefore == images.size()) {
                StringBuilder sbErrorTropPhoto = new StringBuilder("Vous dépassez le nombre de photos autorisées par annonce, veuillez en supprimer avant d'en rajouter ! (Pour rappel la limite est de ");
                sbErrorTropPhoto.append(PropertiesFileGeneral.GENERAL.getProperties().getProperty("gen.max.number.file.annonce")).append(" photos par annonce)");
                target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, sbErrorTropPhoto.toString(), FeedbackMessageLevel.ERROR));
            } else if (images.isEmpty()) {
                target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant le chargement des photos sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
            } else {
                //Mise a jour des champs
                ajoutImageDTO.getImages().clear();
                photoField.getFileUploads().clear();

                target.getPage().send(target.getPage(), Broadcast.BREADTH, new ClearFeedbackPanelEvent(target));
                target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Photo(s) rajoutée(s) avec succés", FeedbackMessageLevel.SUCCESS));
            }

            target.add(photoField);
        }
    }
}
