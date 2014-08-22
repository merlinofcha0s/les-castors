package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;

public class Etape2PartenaireForm extends Form<CreationPartenaireDTO> {

    private static final long serialVersionUID = 8347005157184562826L;

    public Etape2PartenaireForm(String id, IModel<CreationPartenaireDTO> model) {
        super(id, model);

        // Mode Multipart pour l'upload de fichier.
        this.setMultiPart(true);
        this.setMarkupId("formPartenaireEtape2");

        DropDownChoice<Civilite> civilite = new DropDownChoice<Civilite>("artisan.civilite", Arrays.asList(Civilite
                .values()));
        civilite.setRequired(true);
        civilite.setMarkupId("civilite");
        civilite.add(new ErrorHighlightBehavior());
        civilite.add(new RequiredBorderBehaviour());

        TextField<String> nom = new TextField<String>("artisan.nom");
        nom.setRequired(true);
        nom.setMarkupId("nomField");
        nom.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_NOM_MIN, ValidatorConstant.CLIENT_NOM_MAX));
        nom.add(new ErrorHighlightBehavior());
        nom.add(new RequiredBorderBehaviour());

        this.add(civilite);
        this.add(nom);

    }

}
