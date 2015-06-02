package fr.batimen.web.client.extend.nouveau.artisan;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.aggregate.ModificationAnnonceDTO;
import fr.batimen.dto.aggregate.ModificationEntrepriseDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.app.security.RolesUtils;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.CastorDatePicker;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEventArtisan;
import fr.batimen.web.client.extend.nouveau.devis.NouveauUtils;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.validator.SiretValidator;
import fr.batimen.ws.client.service.ArtisanServiceREST;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
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

    @Inject
    private Authentication authentication;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

    private static final Logger LOGGER = LoggerFactory.getLogger(Etape3EntrepriseForm.class);

    public Etape3EntrepriseForm(final String id, final IModel<CreationPartenaireDTO> model, final boolean isInModification) {
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

        final TextField<Integer> nbEmployes = new TextField<Integer>("entreprise.nbEmployees");
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

        final AjaxLink<Void> etapePrecedenteNouveauArtisan3 = new AjaxLink<Void>("etapePrecedenteNouveauArtisan3") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_3.ordinal() + 1);
            }
        };

        etapePrecedenteNouveauArtisan3.setOutputMarkupId(true);
        etapePrecedenteNouveauArtisan3.setMarkupId("etapePrecedenteNouveauArtisan3");

        WebMarkupContainer containerEtapePrecedenteNouveauArtisan3 = new WebMarkupContainer("containerEtapePrecedenteNouveauArtisan3") {
            @Override
            public boolean isVisible() {
                return !isInModification;
            }
        };

        containerEtapePrecedenteNouveauArtisan3.add(etapePrecedenteNouveauArtisan3);


        AjaxSubmitLink validateEtape3Partenaire = new AjaxSubmitLink("validateEtape3Partenaire") {

            private static final long serialVersionUID = 4945314422581299777L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm());
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (categorieSelectionnees.isEmpty()) {
                    MasterPage.triggerEventFeedBackPanel(target, "Veuillez selectionner au moins une categorie", FeedbackMessageLevel.ERROR);
                } else if (isInModification) {
                    nouveauPartenaire.getEntreprise().setAdresseEntreprise(nouveauPartenaire.getAdresse());
                    Integer codeRetour = artisanServiceREST.saveEntrepriseInformation(nouveauPartenaire.getEntreprise());
                    if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                        authentication.setEntrepriseUserInfo(nouveauPartenaire.getEntreprise());
                        MasterPage.triggerEventFeedBackPanel(target, "Profil mis à jour avec succés", FeedbackMessageLevel.SUCCESS);
                    } else {
                        MasterPage.triggerEventFeedBackPanel(target, "Problème durant l'appel au service de mise à jour, veuillez réessayer ultérieurement ", FeedbackMessageLevel.ERROR);
                    }

                } else {
                    nouveauPartenaire.setNumeroEtape(4);
                    ChangementEtapeEventArtisan changementEtapeEvent = new ChangementEtapeEventArtisan(target,
                            nouveauPartenaire);
                    this.send(target.getPage(), Broadcast.EXACT, changementEtapeEvent);
                }
            }
        };

        validateEtape3Partenaire.setMarkupId("validateEtape3Partenaire");

        WebMarkupContainer terminerInscriptionPartenaire = new WebMarkupContainer("terminerInscriptionPartenaire") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (isInModification) {
                    StringBuilder classCSS = new StringBuilder(tag.getAttribute("class"));
                    classCSS.append(" offset3");
                    tag.remove("class");
                    tag.put("class", classCSS.toString());
                }else{
                    StringBuilder classCSS = new StringBuilder(tag.getAttribute("class"));
                    classCSS.append(" nouveauPartenaire-btn");
                    tag.remove("class");
                    tag.put("class", classCSS.toString());
                }
            }
        };

        terminerInscriptionPartenaire.add(validateEtape3Partenaire);

        add(nomComplet, statutJuridique, nbEmployes, dateCreation, siret, logo, adresse, complementAdresse,
                codePostalField, villeField, departementField, terminerInscriptionPartenaire, containerEtapePrecedenteNouveauArtisan3);
    }
}
