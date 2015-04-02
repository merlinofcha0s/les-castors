package fr.batimen.web.client.extend.nouveau.devis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.SousCategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;
import fr.batimen.web.client.validator.FileUploadValidator;

/**
 * Form de l'etape 3 de création d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape3AnnonceForm extends Form<CreationAnnonceDTO> {

    private static final long serialVersionUID = 6521295805432818556L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Etape3AnnonceForm.class);

    private final CreationAnnonceDTO nouvelleAnnonce;

    private final Collection<FileUpload> photos = new ArrayList<FileUpload>();

    public Etape3AnnonceForm(String id, IModel<CreationAnnonceDTO> model) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        setMultiPart(true);
        setFileMaxSize(Bytes.megabytes(10));

        setMarkupId("formEtape3");

        nouvelleAnnonce = model.getObject();

        DropDownChoice<SousCategorieMetierDTO> sousCategorieSelect = new DropDownChoice<SousCategorieMetierDTO>(
                "sousCategorie") {

            private static final long serialVersionUID = -4258418495065575690L;

            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload() instanceof CategorieEvent) {
                    CategorieEvent eventCategorie = (CategorieEvent) event.getPayload();
                    // On maj la liste des sous categorie quand on passe dans //
                    // l'etape 3
                    this.setChoices(eventCategorie.getCategorieChoisie().getSousCategories());
                    eventCategorie.getTarget().add(this);
                }
            }

        };
        sousCategorieSelect.setMarkupId("sousCategorieSelect");
        sousCategorieSelect.setRequired(true);
        sousCategorieSelect.add(new RequiredBorderBehaviour());
        sousCategorieSelect.add(new ErrorHighlightBehavior());

        TextArea<String> descriptionDevisField = new TextArea<String>("description");
        descriptionDevisField.setRequired(true);
        descriptionDevisField.add(StringValidator.lengthBetween(ValidatorConstant.ANNONCE_DESCRIPTION_MIN,
                ValidatorConstant.ANNONCE_DESCRIPTION_MAX));
        descriptionDevisField.setMarkupId("descriptionDevisField");
        descriptionDevisField.add(new ErrorHighlightBehavior());
        descriptionDevisField.add(new RequiredBorderBehaviour());

        DropDownChoice<TypeContact> typeContactField = new DropDownChoice<TypeContact>("typeContact",
                Arrays.asList(TypeContact.values()));
        typeContactField.setRequired(true);
        typeContactField.setMarkupId("typeContactField");
        typeContactField.add(new ErrorHighlightBehavior());
        typeContactField.add(new RequiredBorderBehaviour());

        DropDownChoice<DelaiIntervention> delaiInterventionField = new DropDownChoice<DelaiIntervention>(
                "delaiIntervention", Arrays.asList(DelaiIntervention.values()));
        delaiInterventionField.setRequired(true);
        delaiInterventionField.setMarkupId("delaiInterventionField");
        delaiInterventionField.add(new ErrorHighlightBehavior());
        delaiInterventionField.add(new RequiredBorderBehaviour());

        RadioGroup<TypeTravaux> typeTravaux = new RadioGroup<TypeTravaux>("typeTravaux");
        Radio<TypeTravaux> neuf = new Radio<TypeTravaux>("typeTravaux.neuf", new Model<TypeTravaux>(TypeTravaux.NEUF));
        Radio<TypeTravaux> renovation = new Radio<TypeTravaux>("typeTravaux.renovation", new Model<TypeTravaux>(
                TypeTravaux.RENOVATION));
        typeTravaux.add(neuf);
        typeTravaux.add(renovation);
        typeTravaux.setRequired(true);
        typeTravaux.add(new RequiredBorderBehaviour());
        typeTravaux.setMarkupId("typeTravaux");

        MultiFileUploadField photoField = new MultiFileUploadField("photos", new PropertyModel<Collection<FileUpload>>(
                this, "photos"), 5, true);
        photoField.setMarkupId("photoField");
        photoField.add(new FileUploadValidator());

        TextField<String> adresseField = new TextField<String>("adresse");
        adresseField.setRequired(true);
        adresseField.setMarkupId("adresseField");
        adresseField.add(StringValidator.lengthBetween(ValidatorConstant.ADRESSE_MIN, ValidatorConstant.ADRESSE_MAX));
        adresseField.add(new ErrorHighlightBehavior());
        adresseField.add(new RequiredBorderBehaviour());

        TextField<String> adresseComplementField = new TextField<String>("complementAdresse");
        adresseComplementField.setMarkupId("adresseComplementField");
        adresseComplementField.add(StringValidator.maximumLength(ValidatorConstant.COMPLEMENT_ADRESSE_MAX));
        adresseComplementField.add(new ErrorHighlightBehavior());
        adresseComplementField.add(new RequiredBorderBehaviour());

        TextField<String> codePostalField = new TextField<String>("codePostal");
        codePostalField.setRequired(true);
        codePostalField.setMarkupId("codePostalField");
        codePostalField.add(new PatternValidator(ValidatorConstant.CODE_POSTAL_REGEX));
        codePostalField.add(new ErrorHighlightBehavior());
        codePostalField.add(new RequiredBorderBehaviour());

        TextField<String> villeField = new TextField<String>("ville");
        villeField.setRequired(true);
        villeField.setMarkupId("villeField");
        villeField.add(StringValidator.maximumLength(ValidatorConstant.VILLE_MAX));
        villeField.add(new ErrorHighlightBehavior());
        villeField.add(new RequiredBorderBehaviour());

        AjaxSubmitLink validateQualification = new AjaxSubmitLink("validateQualification") {

            private static final long serialVersionUID = -4417031301033032959L;

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onSubmit
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                nouvelleAnnonce.getPhotos().clear();
                for (FileUpload photo : photos) {
                    try {
                        nouvelleAnnonce.getPhotos().add(photo.writeToTempFile());
                    } catch (IOException e) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("Problème durant l'ecriture de la photo sur le disque", e);
                        }
                    }
                }

                nouvelleAnnonce.setNumeroEtape(4);
                ChangementEtapeClientEvent changementEtapeEventClient = new ChangementEtapeClientEvent(target,
                        nouvelleAnnonce);
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO.setTypeCompte(TypeCompte.CLIENT);
                nouvelleAnnonce.getClient().getPermissions().add(permissionDTO);
                this.send(target.getPage(), Broadcast.BREADTH, changementEtapeEventClient);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onError
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm());
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

        };
        validateQualification.setMarkupId("validateQualification");

        AjaxLink<Void> etapePrecedente3 = new AjaxLink<Void>("etapePrecedente3") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_3.ordinal() + 1);
            }
        };

        etapePrecedente3.setOutputMarkupId(true);
        etapePrecedente3.setMarkupId("etapePrecedente3");

        this.add(sousCategorieSelect, descriptionDevisField, typeContactField, delaiInterventionField, photoField,
                adresseField, adresseComplementField, codePostalField, villeField, validateQualification, typeTravaux,
                etapePrecedente3);
    }

    /**
     * @return the photos
     */
    public Collection<FileUpload> getPhotos() {
        return photos;
    }

}