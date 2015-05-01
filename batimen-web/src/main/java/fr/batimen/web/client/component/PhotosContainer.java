package fr.batimen.web.client.component;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.enums.PropertiesFileGeneral;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AjoutPhotoDTO;
import fr.batimen.dto.aggregate.SuppressionPhotoDTO;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import fr.batimen.web.client.event.ClearFeedbackPanelEvent;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
    private WebMarkupContainer aucunePhotoContainer;
    private WebMarkupContainer transparentMarkupForPhotosAjax;
    private ListView<ImageDTO> imagesView;

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

        photosContainer = new WebMarkupContainer("photosContainer") {

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

        imagesView = new ListView<ImageDTO>("imagesView", images) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<ImageDTO> item) {
                final ImageDTO imageDTO = item.getModelObject();
                WebMarkupContainer imageWithOptions = new WebMarkupContainer("imageWithOptions") {

                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        if (canAdd) {
                            tag.put("style", "height:165px;");
                        } else {
                            tag.remove("style");
                        }
                    }
                };

                WebMarkupContainer imageOptions = new WebMarkupContainer("imageOptions") {
                    @Override
                    public boolean isVisible() {
                        return canAdd;
                    }
                };

                AjaxLink<Void> supprimerImage = new AjaxLink<Void>("supprimerImage") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        String loginDemandeur = authentication.getCurrentUserInfo().getLogin();

                        SuppressionPhotoDTO suppressionPhotoDTO = new SuppressionPhotoDTO();
                        suppressionPhotoDTO.setHashID(idAnnonce);
                        suppressionPhotoDTO.setLoginDemandeur(loginDemandeur);
                        suppressionPhotoDTO.setImageASupprimer(item.getModelObject());

                        Integer codeRetour = annonceServiceREST.suppressionPhoto(suppressionPhotoDTO);

                        if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                            updatePhotoContainer(loginDemandeur, target);
                            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Suppression effectuée !", FeedbackMessageLevel.SUCCESS));
                        } else {
                            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant la suppression de la photo sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
                        }
                    }

                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("style", "overflow:visible;");
                    }
                };

                ExternalLink linkOnPhoto = new ExternalLink("thumbnails", imageDTO.getUrl());
                Image imageHtml = new Image("photo", new Model<String>(imageDTO.getUrl()));
                imageHtml.add(new AttributeModifier("src", imageDTO.getUrl()));

                imageOptions.add(supprimerImage);
                linkOnPhoto.add(imageHtml);
                imageWithOptions.add(linkOnPhoto, imageOptions);
                item.add(imageWithOptions);
            }
        };

        photosContainer.add(imagesView, titleContainer);

        aucunePhotoContainer = new WebMarkupContainer("aucunePhotoContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return images.isEmpty();
            }
        };

        aucunePhotoContainer.setOutputMarkupId(true);

        Label aucunePhoto = new Label("aucunePhoto", "Aucune photo du chantier pour le moment :(");
        aucunePhotoContainer.add(aucunePhoto);

        transparentMarkupForPhotosAjax = new WebMarkupContainer("transparentMarkupForPhotosAjax");
        transparentMarkupForPhotosAjax.setOutputMarkupId(true);
        transparentMarkupForPhotosAjax.add(photosContainer, aucunePhotoContainer);
        add(transparentMarkupForPhotosAjax);
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
        photoField.setOutputMarkupId(true);
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
                    Integer codeRetour = annonceServiceREST.ajouterPhoto(ajoutImageDTO);

                    if (codeRetour.equals(CodeRetourService.ANNONCE_RETOUR_TROP_DE_PHOTOS)) {
                        StringBuilder sbErrorTropPhoto = new StringBuilder("Vous dépassez le nombre de photos autorisées par annonce, veuillez en supprimer avant d'en rajouter ! (Pour rappel la limite est de ");
                        sbErrorTropPhoto.append(PropertiesFileGeneral.GENERAL.getProperties().getProperty("gen.max.number.file.annonce")).append(" photos par annonce)");
                        target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, sbErrorTropPhoto.toString(), FeedbackMessageLevel.ERROR));
                    } else if (codeRetour.equals(CodeRetourService.RETOUR_KO)) {
                        target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Problème durant le chargement des photos sur le serveur, veuillez réessayer ultérieurement", FeedbackMessageLevel.ERROR));
                    } else {
                        updatePhotoContainer(loginDemandeur, target);

                        //Mise a jour des champs
                        ajoutImageDTO.getImages().clear();
                        photoField.getFileUploads().clear();

                        target.getPage().send(target.getPage(), Broadcast.BREADTH, new ClearFeedbackPanelEvent(target));
                    }

                    target.add(photoField);
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

    private void updatePhotoContainer(String loginDemandeur, AjaxRequestTarget target) {
        //Récuperation des nouvelles urls des photos.
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(idAnnonce);
        demandeAnnonceDTO.setLoginDemandeur(loginDemandeur);
        images = annonceServiceREST.getPhotos(demandeAnnonceDTO);
        imagesView.setList(images);
        target.add(transparentMarkupForPhotosAjax);
    }
}