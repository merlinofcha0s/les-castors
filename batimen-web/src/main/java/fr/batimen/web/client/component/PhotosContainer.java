package fr.batimen.web.client.component;

import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.web.client.behaviour.FileFieldValidatorAndLoaderBehaviour;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 * Created by Casaucau on 26/04/2015.
 */
public class PhotosContainer extends Panel {

    private List<ImageDTO> images;
    private String title;
    private String baliseTypeTitle;
    private boolean canAdd;

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

        Label titleContainer = new Label("titleContainer", title){

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

    private void initAjoutPhotoSection(){

        Form<List<ImageDTO>> addPhotoForm = new Form<List<ImageDTO>>("addPhotoForm"){

            @Override
            public boolean isVisible() {
                return canAdd;
            }
        };

        final FileUploadField photoField = new FileUploadField("photoField");
        photoField.setMarkupId("photoField");
        FileFieldValidatorAndLoaderBehaviour fileFieldValidatorBehaviour = new FileFieldValidatorAndLoaderBehaviour();
        photoField.add(fileFieldValidatorBehaviour);

        AjaxSubmitLink envoyerPhotos = new AjaxSubmitLink("envoyerPhotos") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
            }
        };

        addPhotoForm.add(photoField, envoyerPhotos);
        add(addPhotoForm);
    }


}
