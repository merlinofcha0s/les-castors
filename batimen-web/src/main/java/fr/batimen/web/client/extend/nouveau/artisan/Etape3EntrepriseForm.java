package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.FrenchDatePicker;

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
        FrenchDatePicker<Date> dateCreation = new FrenchDatePicker<Date>("entreprise.dateCreation");
        dateCreation.setMarkupId("dateCreationField");
        dateCreation.setDateFormat("dd/mm/yy");
        dateCreation.add(DateValidator.maximum(new Date(), "dd/MM/yyyy"));
        dateCreation.add(new ErrorHighlightBehavior());

        this.add(nomComplet);
        this.add(statutJuridique);
        this.add(nbEmploye);
        this.add(dateCreation);

    }
}
