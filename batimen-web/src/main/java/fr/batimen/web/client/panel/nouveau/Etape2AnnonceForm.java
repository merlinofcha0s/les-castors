package fr.batimen.web.client.panel.nouveau;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;

public class Etape2AnnonceForm extends Form<CreationAnnonceDTO> {

	private static final long serialVersionUID = 6521295805432818556L;

	private DropDownChoice<Metier> corpsMetierSelect;
	private TextField<String> objetDevisField;
	private TextArea<String> descriptionDevisField;
	private DropDownChoice<TypeContact> typeContactField;
	private TextField<String> delaiInterventionField;
	private NumberTextField<Integer> nbDevisField;
	private FileUploadField photoField;
	private TextField<String> adresseField;
	private TextField<String> adresseComplementField;
	private TextField<String> codePostalField;
	private TextField<String> villeField;
	private SubmitLink validateQualification;

	public Etape2AnnonceForm(String id, IModel<CreationAnnonceDTO> model) {
		super(id, model);

		// multipart pour l'upload de fichier.
		this.setMultiPart(true);
		this.setMarkupId("formEtape2");

		corpsMetierSelect = new DropDownChoice<Metier>("metier", Arrays.asList(Metier.values()));
		corpsMetierSelect.setRequired(true);
		corpsMetierSelect.setMarkupId("corpsMetierSelect");

		objetDevisField = new TextField<String>("titre");
		objetDevisField.setRequired(true);
		objetDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_TITRE_MIN,
				ValidatorConstant.CREATION_ANNONCE_TITRE_MAX));
		objetDevisField.setMarkupId("objetDevisField");

		descriptionDevisField = new TextArea<String>("description");
		descriptionDevisField.setRequired(true);
		descriptionDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MIN,
				ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MAX));
		descriptionDevisField.setMarkupId("descriptionDevisField");

		typeContactField = new DropDownChoice<TypeContact>("typeContact", Arrays.asList(TypeContact.values()));

		typeContactField.setRequired(true);
		typeContactField.setMarkupId("typeContactField");

		delaiInterventionField = new TextField<String>("delaiIntervention");
		delaiInterventionField.setRequired(true);
		delaiInterventionField.setMarkupId("delaiInterventionField");
		delaiInterventionField.add(StringValidator
				.maximumLength(ValidatorConstant.CREATION_ANNONCE_DELAI_INTERVENTION_MAX));

		nbDevisField = new NumberTextField<Integer>("nbDevis");
		nbDevisField.setRequired(true);
		nbDevisField.setMarkupId("nbDevisField");
		nbDevisField.add(RangeValidator.range(ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MIN,
				ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MAX));

		photoField = new FileUploadField("photo");
		photoField.setMarkupId("photoField");

		adresseField = new TextField<String>("adresse");
		adresseField.setRequired(true);
		adresseField.setMarkupId("adresseField");
		adresseField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_ADRESSE_MIN,
				ValidatorConstant.CREATION_ANNONCE_ADRESSE_MAX));

		adresseComplementField = new TextField<String>("complementAdresse");
		adresseComplementField.setMarkupId("adresseComplementField");
		adresseComplementField.add(StringValidator
				.maximumLength(ValidatorConstant.CREATION_ANNONCE_COMPLEMENT_ADRESSE_MAX));

		codePostalField = new TextField<String>("codePostal");
		codePostalField.setRequired(true);
		codePostalField.setMarkupId("codePostalField");
		codePostalField.add(new PatternValidator(ValidatorConstant.CREATION_ANNONCE_CODE_POSTAL_REGEX));

		villeField = new TextField<String>("ville");
		villeField.setRequired(true);
		villeField.setMarkupId("villeField");
		villeField.add(StringValidator.maximumLength(ValidatorConstant.CREATION_ANNONCE_VILLE_MAX));

		validateQualification = new SubmitLink("validateQualification");

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
