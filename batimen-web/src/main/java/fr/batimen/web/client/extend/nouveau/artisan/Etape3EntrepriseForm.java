package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.CastorDatePicker;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.batimen.web.client.extend.nouveau.artisan.validator.SiretValidator;
import fr.batimen.web.client.master.MasterPage;

/**
 * Form de l'etape 3 permettant au nouvel artisan de renseigner les informayions
 * de l'entreprise
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape3EntrepriseForm extends Form<CreationPartenaireDTO> {

    private static final long serialVersionUID = 7654913676022607009L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Etape3EntrepriseForm.class);

    public Etape3EntrepriseForm(String id, IModel<CreationPartenaireDTO> model) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        this.setMultiPart(true);
        this.setMarkupId("formEntrepriseEtape3");

        final CreationPartenaireDTO nouveauPartenaire = model.getObject();
        final List<CategorieMetierDTO> categorieSelectionnees = nouveauPartenaire.getEntreprise().getCategoriesMetier();

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

        TextField<Integer> nbEmployes = new TextField<Integer>("entreprise.nbEmploye");
        nbEmployes.setMarkupId("nbEmployeField");
        nbEmployes.add(new ErrorHighlightBehavior());

        // nbDevis.IConverter=Le champs nombre de devis doit contenir un nombre
        // !
        CastorDatePicker<Date> dateCreation = new CastorDatePicker<Date>("entreprise.dateCreation");
        dateCreation.setMarkupId("dateCreationField");
        dateCreation.setDateFormat("dd/mm/yy");
        dateCreation.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));
        dateCreation.add(new ErrorHighlightBehavior());

        TextField<String> siret = new TextField<String>("entreprise.siret");
        siret.setRequired(true);
        siret.setMarkupId("siretField");
        siret.add(new PatternValidator(ValidatorConstant.ENTREPRISE_SIRET_REGEXP));
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

        AjaxSubmitLink validateEtape3Partenaire = new AjaxSubmitLink("validateEtape3Partenaire") {

            private static final long serialVersionUID = 4945314422581299777L;

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
                if (categorieSelectionnees.isEmpty()) {
                    MasterPage.triggerEventFeedBackPanel(target, "Veuillez selectionner au moins une categorie");
                } else {
                    nouveauPartenaire.setNumeroEtape(4);
                    ChangementEtapeEventArtisan changementEtapeEvent = new ChangementEtapeEventArtisan(target,
                            nouveauPartenaire);
                    this.send(target.getPage(), Broadcast.EXACT, changementEtapeEvent);
                }

            }

        };

        validateEtape3Partenaire.setMarkupId("validateEtape3Partenaire");

        this.add(nomComplet);
        this.add(statutJuridique);
        this.add(nbEmployes);
        this.add(dateCreation);
        this.add(siret);
        this.add(logo);
        this.add(adresse);
        this.add(complementAdresse);
        this.add(codePostalField);
        this.add(villeField);
        this.add(validateEtape3Partenaire);
    }
}
