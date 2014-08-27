package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogAnimateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.CastorDatePicker;

public class Etape3EntrepriseForm extends Form<CreationPartenaireDTO> {

    private static final long serialVersionUID = 7654913676022607009L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Etape3EntrepriseForm.class);

    public Etape3EntrepriseForm(String id, IModel<CreationPartenaireDTO> model) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        this.setMultiPart(true);
        this.setMarkupId("formEntrepriseEtape3");

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

        TextField<Integer> nbEmploye = new TextField<Integer>("entreprise.nbEmploye");
        nbEmploye.setMarkupId("nbEmployeField");
        nbEmploye.add(new ErrorHighlightBehavior());

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
        siret.add(new ErrorHighlightBehavior());
        siret.add(new RequiredBorderBehaviour());

        final Dialog activiteDialog = new Dialog("activiteDialog") {

            private static final long serialVersionUID = 1703137705306176173L;

            /*
             * (non-Javadoc)
             * 
             * @see org.odlabs.wiquery.ui.dialog.Dialog#open(org.apache.wicket
             * .ajax.AjaxRequestTarget)
             */
            @Override
            public void open(AjaxRequestTarget ajaxRequestTarget) {
                super.open(ajaxRequestTarget);
            }

        };
        activiteDialog.setModal(true);
        activiteDialog.setTitle("Ajouter une activité");
        activiteDialog.setResizable(false);
        activiteDialog.setDraggable(false);
        activiteDialog.setWidth(620);
        activiteDialog.setShow(new DialogAnimateOption("fade"));
        activiteDialog.setHide(new DialogAnimateOption("fade"));

        Image ajouterImg = new Image("ajouterImg", new ContextRelativeResource("img/add.png"));

        AjaxLink<Void> ajouterActivite = new AjaxLink<Void>("ajouterActivite") {

            private static final long serialVersionUID = -9008353963454924193L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                activiteDialog.open(target);
            }
        };

        ajouterActivite.add(ajouterImg);

        this.add(nomComplet);
        this.add(statutJuridique);
        this.add(nbEmploye);
        this.add(dateCreation);
        this.add(siret);
        this.add(ajouterActivite);
        this.add(activiteDialog);

    }
}
