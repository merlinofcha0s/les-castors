package fr.batimen.web.client.extend.member.client.util;

import fr.batimen.core.enums.PropertiesFileGeneral;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AjoutPhotoDTO;
import fr.batimen.dto.aggregate.SuppressionPhotoDTO;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import fr.batimen.web.client.event.AjoutPhotoEvent;
import fr.batimen.web.client.event.ClearFeedbackPanelEvent;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.event.SuppressionPhotoEvent;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import fr.batimen.ws.client.service.ArtisanServiceREST;
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
@Named("photoServiceAjaxLogic")
public class PhotoServiceAjaxLogic implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceAjaxLogic.class);

    private static final String CHANTIER_TEMOIN_PHOTO_SERVICE = "photoChantierTemoinService";
    private static final String ANNONCE_PHOTO_SERVICE = "annoncePhotoService";

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    @Inject
    private Authentication authentication;

    public void suppressionPhotoAnnonce(IEvent<?> event) {
        SuppressionPhotoEvent suppressionPhotoEvent = (SuppressionPhotoEvent) event.getPayload();
        suppressionPhoto(suppressionPhotoEvent, ANNONCE_PHOTO_SERVICE);
    }

    public void ajoutPhotoAnnonce(IEvent<?> event) {
        AjoutPhotoEvent ajoutPhotoEvent = (AjoutPhotoEvent) event.getPayload();
        ajoutPhoto(ajoutPhotoEvent, ANNONCE_PHOTO_SERVICE);
    }

    public void suppressionPhotoChantierTemoin(IEvent<?> event) {
        SuppressionPhotoEvent suppressionPhotoEvent = (SuppressionPhotoEvent) event.getPayload();
        suppressionPhotoEvent.setId(authentication.getEntrepriseUserInfo().getSiret());
        suppressionPhoto(suppressionPhotoEvent, CHANTIER_TEMOIN_PHOTO_SERVICE);
    }

    public void ajoutPhotoChantierTemoin(IEvent<?> event) {
        AjoutPhotoEvent ajoutPhotoEvent = (AjoutPhotoEvent) event.getPayload();
        ajoutPhotoEvent.setId(authentication.getEntrepriseUserInfo().getSiret());
        ajoutPhoto(ajoutPhotoEvent, CHANTIER_TEMOIN_PHOTO_SERVICE);
    }

    private void suppressionPhoto(SuppressionPhotoEvent suppressionPhotoEvent, String serviceName){
        AjaxRequestTarget target = suppressionPhotoEvent.getTarget();
        List<ImageDTO> imageDTOs = suppressionPhotoEvent.getImages();
        String loginDemandeur = suppressionPhotoEvent.getLogin();
        String id = suppressionPhotoEvent.getId();

        SuppressionPhotoDTO suppressionPhotoDTO = new SuppressionPhotoDTO();
        suppressionPhotoDTO.setId(id);
        suppressionPhotoDTO.setLoginDemandeur(loginDemandeur);
        suppressionPhotoDTO.setImageASupprimer(suppressionPhotoEvent.getImageASupprimer());

        imageDTOs.clear();
        switch (serviceName){
            case ANNONCE_PHOTO_SERVICE: imageDTOs.addAll(annonceServiceREST.suppressionPhoto(suppressionPhotoDTO));
                break;
            case CHANTIER_TEMOIN_PHOTO_SERVICE : imageDTOs.addAll(artisanServiceREST.suppressionPhotoChantierTemoin(suppressionPhotoDTO));
                break;
        }

        if (suppressionPhotoEvent.getNbImagesAvant() != imageDTOs.size()) {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Suppression effectuée !", FeedbackMessageLevel.SUCCESS));
        } else {
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant la suppression de la photo sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
        }
    }

    private void ajoutPhoto(AjoutPhotoEvent ajoutPhotoEvent, String serviceName) {
        AjaxRequestTarget target = ajoutPhotoEvent.getTarget();

        String loginDemandeur = ajoutPhotoEvent.getLoginDemandeur();
        String id = ajoutPhotoEvent.getId();
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
            ajoutImageDTO.setId(id);
            ajoutImageDTO.setLoginDemandeur(loginDemandeur);
            int sizeBefore = ajoutPhotoEvent.getNbImages();
            images.clear();
            switch (serviceName){
                case ANNONCE_PHOTO_SERVICE: images.addAll(annonceServiceREST.ajouterPhoto(ajoutImageDTO));
                    break;
                case CHANTIER_TEMOIN_PHOTO_SERVICE : images.addAll(artisanServiceREST.ajouterPhotosChantierTemoin(ajoutImageDTO));
                    break;
            }

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
