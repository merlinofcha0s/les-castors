package fr.castor.web.client.extend.nouveau.artisan;

import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.aggregate.CreationPartenaireDTO;
import fr.castor.dto.constant.ValidatorConstant;
import fr.castor.dto.enums.StatutJuridique;
import fr.castor.web.client.behaviour.ErrorHighlightBehavior;
import fr.castor.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.castor.web.client.component.CastorDatePicker;
import fr.castor.web.client.validator.SiretValidator;
import fr.castor.web.client.validator.VilleValidator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Form de l'etape 3 permettant au nouvel artisan de renseigner les informayions
 * de l'entreprise
 *
 * @author Casaucau Cyril
 */
public class Etape3EntrepriseForm extends Form<CreationPartenaireDTO> {

    private static final long serialVersionUID = 7654913676022607009L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Etape3EntrepriseForm.class);

    private WebMarkupContainer fieldContainer;

    @Inject
    private VilleValidator villeValidator;

    public Etape3EntrepriseForm(final String id, final IModel<CreationPartenaireDTO> model, final boolean isInModification) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        this.setMultiPart(true);
        this.setMarkupId("formEntrepriseEtape3");

        final CreationPartenaireDTO nouveauPartenaire = model.getObject();
        final List<CategorieMetierDTO> categorieSelectionnees = nouveauPartenaire.getEntreprise().getCategoriesMetier();

        TextField<String> specialite = new TextField<String>("entreprise.specialite");
        specialite.setMarkupId("specialite");
        specialite.add(StringValidator.lengthBetween(ValidatorConstant.ENTREPRISE_SPECIALITE_MIN,
                ValidatorConstant.ENTREPRISE_SPECIALITE_MAX));
        specialite.add(new ErrorHighlightBehavior());

        TextField<String> nomComplet = new TextField<String>("entreprise.nomComplet");
        nomComplet.setRequired(true);
        nomComplet.setMarkupId("nomComplet");
        nomComplet.add(StringValidator.lengthBetween(ValidatorConstant.ENTREPRISE_NOM_COMPLET_MIN,
                ValidatorConstant.ENTREPRISE_NOM_COMPLET_MAX));
        nomComplet.add(new ErrorHighlightBehavior());
        nomComplet.add(new RequiredBorderBehaviour());

        DropDownChoice<StatutJuridique> statutJuridique = new DropDownChoice<StatutJuridique>(
                "entreprise.statutJuridique", Arrays.asList(StatutJuridique.values()));
        statutJuridique.setRequired(true);
        statutJuridique.setMarkupId("statutJuridique");
        statutJuridique.add(new ErrorHighlightBehavior());
        statutJuridique.add(new RequiredBorderBehaviour());

        final TextField<Integer> nbEmployes = new TextField<Integer>("entreprise.nbEmployees");
        nbEmployes.setMarkupId("nbEmployeField");
        nbEmployes.add(new ErrorHighlightBehavior());

        // nbDevis.IConverter=Le champs nombre de devis doit contenir un nombre
        // !
        CastorDatePicker dateCreation = new CastorDatePicker("entreprise.dateCreation", "entreprisedateCreation", false);
        dateCreation.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));
        dateCreation.setRequired(true);
        dateCreation.add(new RequiredBorderBehaviour());
        dateCreation.add(new ErrorHighlightBehavior());

        TextField<String> siret = new TextField<String>("entreprise.siret");
        siret.setRequired(true);
        siret.setMarkupId("siretField");
        siret.add(new SiretValidator());
        siret.add(new ErrorHighlightBehavior());
        siret.add(new RequiredBorderBehaviour());

        FileUploadField logo = new FileUploadField("entreprise.logo");
        logo.setMarkupId("logoField");

        TextField<String> adresse = new TextField<String>("adresse.adresse");
        adresse.setRequired(true);
        adresse.setMarkupId("adresseField");
        adresse.add(StringValidator.lengthBetween(ValidatorConstant.ADRESSE_MIN, ValidatorConstant.ADRESSE_MAX));
        adresse.add(new ErrorHighlightBehavior());
        adresse.add(new RequiredBorderBehaviour());

        TextField<String> complementAdresse = new TextField<String>("adresse.complementAdresse");
        complementAdresse.setMarkupId("complementAdresseField");
        complementAdresse.add(StringValidator.maximumLength(ValidatorConstant.ADRESSE_MAX));
        complementAdresse.add(new ErrorHighlightBehavior());

        TextField<String> codePostalField = new TextField<String>("adresse.codePostal");
        codePostalField.setRequired(true);
        codePostalField.setMarkupId("codePostalField");
        codePostalField.add(new PatternValidator(ValidatorConstant.CODE_POSTAL_REGEX));
        codePostalField.add(new ErrorHighlightBehavior());
        codePostalField.add(new RequiredBorderBehaviour());

        TextField<String> villeField = new TextField<String>("adresse.ville");
        villeField.setRequired(true);
        villeField.setMarkupId("villeField");
        villeField.add(StringValidator.maximumLength(ValidatorConstant.VILLE_MAX));
        villeField.add(new ErrorHighlightBehavior());
        villeField.add(new RequiredBorderBehaviour());
        villeValidator.setCodepostalField(codePostalField);
        villeField.add(villeValidator);

        TextField<Integer> departementField = new TextField<>("adresse.departement");
        departementField.setRequired(true);
        departementField.setMarkupId("departementField");
        departementField.add(RangeValidator.minimum(ValidatorConstant.DEPARTEMENT_MIN));
        departementField.add(RangeValidator.maximum(ValidatorConstant.DEPARTEMENT_MAX));
        departementField.add(new ErrorHighlightBehavior());
        departementField.add(new RequiredBorderBehaviour());

        if (isInModification) {
            nomComplet.setEnabled(false);
            statutJuridique.setEnabled(false);
            siret.setEnabled(false);
            departementField.setEnabled(false);
        }

        fieldContainer = new WebMarkupContainer("fieldContainer");
        fieldContainer.setOutputMarkupId(true);

        fieldContainer.add(nomComplet, statutJuridique, nbEmployes, dateCreation, siret, logo, adresse, complementAdresse,
                codePostalField, villeField, departementField, specialite);

        add(fieldContainer);
    }

    @Override
    public boolean wantSubmitOnParentFormSubmit() {
        return true;
    }

    public WebMarkupContainer getFieldContainer() {
        return fieldContainer;
    }
}
