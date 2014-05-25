package fr.batimen.web.client.extend.nouveaudevis;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.BatimenToolTip;

/**
 * Form de l'etape 2 de création d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape2AnnonceForm extends Form<CreationAnnonceDTO> {

	private static final long serialVersionUID = 6521295805432818556L;

	public Etape2AnnonceForm(String id, IModel<CreationAnnonceDTO> model) {
		super(id, model);

		// Mode Multipart pour l'upload de fichier.
		this.setMultiPart(true);
		this.setMarkupId("formEtape2");

		DropDownChoice<Metier> corpsMetierSelect = new DropDownChoice<Metier>("metier", Arrays.asList(Metier.values()));
		corpsMetierSelect.setMarkupId("corpsMetierSelect");
		corpsMetierSelect.setRequired(true);
		corpsMetierSelect.add(new RequiredBorderBehaviour());
		corpsMetierSelect.add(new ErrorHighlightBehavior());

		TextField<String> objetDevisField = new TextField<String>("titre");
		objetDevisField.setRequired(true);
		objetDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_TITRE_MIN,
		        ValidatorConstant.CREATION_ANNONCE_TITRE_MAX));
		objetDevisField.setMarkupId("objetDevisField");
		objetDevisField.add(new ErrorHighlightBehavior());
		objetDevisField.add(new RequiredBorderBehaviour());

		TextArea<String> descriptionDevisField = new TextArea<String>("description");
		descriptionDevisField.setRequired(true);
		descriptionDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MIN,
		        ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MAX));
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

		TextField<Integer> nbDevisField = new TextField<Integer>("nbDevis");
		nbDevisField.setRequired(true);
		nbDevisField.setMarkupId("nbDevisField");
		nbDevisField.add(RangeValidator.range(ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MIN,
		        ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MAX));
		nbDevisField.add(new ErrorHighlightBehavior());
		nbDevisField.add(new RequiredBorderBehaviour());

		MultiFileUploadField photoField = new MultiFileUploadField("photos", 5);
		photoField.setMarkupId("photoField");

		TextField<String> adresseField = new TextField<String>("adresse");
		adresseField.setRequired(true);
		adresseField.setMarkupId("adresseField");
		adresseField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_ADRESSE_MIN,
		        ValidatorConstant.CREATION_ANNONCE_ADRESSE_MAX));
		adresseField.add(new ErrorHighlightBehavior());
		adresseField.add(new RequiredBorderBehaviour());

		TextField<String> adresseComplementField = new TextField<String>("complementAdresse");
		adresseComplementField.setMarkupId("adresseComplementField");
		adresseComplementField.add(StringValidator
		        .maximumLength(ValidatorConstant.CREATION_ANNONCE_COMPLEMENT_ADRESSE_MAX));
		adresseComplementField.add(new ErrorHighlightBehavior());
		adresseComplementField.add(new RequiredBorderBehaviour());

		TextField<String> codePostalField = new TextField<String>("codePostal");
		codePostalField.setRequired(true);
		codePostalField.setMarkupId("codePostalField");
		codePostalField.add(new PatternValidator(ValidatorConstant.CREATION_ANNONCE_CODE_POSTAL_REGEX));
		codePostalField.add(new ErrorHighlightBehavior());
		codePostalField.add(new RequiredBorderBehaviour());

		TextField<String> villeField = new TextField<String>("ville");
		villeField.setRequired(true);
		villeField.setMarkupId("villeField");
		villeField.add(StringValidator.maximumLength(ValidatorConstant.CREATION_ANNONCE_VILLE_MAX));
		villeField.add(new ErrorHighlightBehavior());
		villeField.add(new RequiredBorderBehaviour());

		SubmitLink validateQualification = new SubmitLink("validateQualification");
		validateQualification.setMarkupId("validateQualification");

		this.add(BatimenToolTip.getTooltipBehaviour());

		this.add(corpsMetierSelect);
		this.add(objetDevisField);
		this.add(descriptionDevisField);
		this.add(typeContactField);
		this.add(delaiInterventionField);
		this.add(nbDevisField);
		this.add(photoField);
		this.add(adresseField);
		this.add(adresseComplementField);
		this.add(codePostalField);
		this.add(villeField);
		this.add(validateQualification);
	}
}
