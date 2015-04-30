package fr.batimen.web.client.component;

import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AjoutPhotoDTO;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by Casaucau on 26/04/2015.
 */
public class PhotosContainer extends Panel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotosContainer.class);

    private List<ImageDTO> images;
    private String title;
    private String baliseTypeTitle;
    private boolean canAdd;
    private String idAnnonce;
    private WebMarkupContainer photosContainer;

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    @Inject
    private Authentication authentication;

    private AjoutPhotoDTO ajoutImageDTO = new AjoutPhotoDTO();

    public PhotosContainer(String id, List<ImageDTO> images, String title, String baliseTypeTitle, boolean canAdd) {
        super(id);
        this.images = images;
        this.title = title;
        this.baliseTypeTitle = baliseTypeTitle;
        this.canAdd = canAdd;

        initComponent();
        initAjoutPhotoSection();
    }

    private void initComponent() {
        WebMarkupContainer photosContainer = new WebMarkupContainer("photosContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return !images.isEmpty();
            }
        };

        photosContainer.setOutputMarkupId(true);

        Label titleContainer = new Label("titleContainer", title) {

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.setName(baliseTypeTitle);
            }
        };

        ListView<ImageDTO> imagesView = new ListView<ImageDTO>("imagesView", images) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ImageDTO> item) {
                final ImageDTO imageDTO = item.getModelObject();
                ExternalLink linkOnPhoto = new ExternalLink("thumbnails", imageDTO.getUrl());
                Image imageHtml = new Image("photo", new Model<String>(imageDTO.getUrl()));
                imageHtml.add(new AttributeModifier("src", imageDTO.getUrl()));
                linkOnPhoto.add(imageHtml);
                item.add(linkOnPhoto);
            }
        };

        photosContainer.add(imagesView, titleContainer);

        WebMarkupContainer aucunePhotoContainer = new WebMarkupContainer("aucunePhotoContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return images.isEmpty();
            }
        };

        Label aucunePhoto = new Label("aucunePhoto", "Aucune photo du chantier pour le moment :(");
        aucunePhotoContainer.add(aucunePhoto);

        add(photosContainer, aucunePhotoContainer);
    }

    private void initAjoutPhotoSection() {

        WebMarkupContainer ajoutPhotoContainer = new WebMarkupContainer("ajoutPhotoContainer") {
            @Override
            public boolean isVisible() {
                return canAdd;
            }
        };

        Form<AjoutPhotoDTO> addPhotoForm = new Form<AjoutPhotoDTO>("addPhotoForm");

        final FileUploadField photoField = new FileUploadField("photoField");
        photoField.setMarkupId("photoField");
        final FileFieldValidatorAndLoaderBehaviour fileFieldValidatorBehaviour = new FileFieldValidatorAndLoaderBehaviour();
        photoField.add(fileFieldValidatorBehaviour);

        AjaxSubmitLink envoyerPhotos = new AjaxSubmitLink("envoyerPhotos") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
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
                    String loginDemandeur = authentication.getCurrentUserInfo().getLogin();

                    //Ajout des photos
                    ajoutImageDTO.setHashID(idAnnonce);
                    ajoutImageDTO.setLoginDemandeur(loginDemandeur);
                    annonceServiceREST.ajouterPhoto(ajoutImageDTO);

                    //Récuperation des nouvelles urls des photos.
                    DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
                    demandeAnnonceDTO.setHashID(idAnnonce);
                    demandeAnnonceDTO.setLoginDemandeur(loginDemandeur);
                    images = annonceServiceREST.getPhotos(demandeAnnonceDTO);
                    target.add(photosContainer);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm());
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }
        };

        addPhotoForm.add(photoField, envoyerPhotos);
        ajoutPhotoContainer.add(addPhotoForm);

        add(ajoutPhotoContainer);
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }
}