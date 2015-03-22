package fr.batimen.web.client.extend.nouveau.devis;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.SousCategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.CategorieEvent;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;

/**
 * Form de l'etape 3 de création d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape3AnnonceForm extends Form<CreationAnnonceDTO> {

    private static final long serialVersionUID = 6521295805432818556L;

    private final CreationAnnonceDTO nouvelleAnnonce;

    public Etape3AnnonceForm(String id, IModel<CreationAnnonceDTO> model) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        this.setMultiPart(true);
        this.setMarkupId("formEtape3");

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

        MultiFileUploadField photoField = new MultiFileUploadField("photos", 5);
        photoField.setMarkupId("photoField");

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

        this.add(sousCategorieSelect);
        this.add(descriptionDevisField);
        this.add(typeContactField);
        this.add(delaiInterventionField);
        this.add(photoField);
        this.add(adresseField);
        this.add(adresseComplementField);
        this.add(codePostalField);
        this.add(villeField);
        this.add(validateQualification);
        this.add(typeTravaux);
    }
}
