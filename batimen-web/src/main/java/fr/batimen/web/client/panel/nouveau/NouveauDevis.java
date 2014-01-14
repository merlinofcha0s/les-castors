package fr.batimen.web.client.panel.nouveau;

import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.master.MasterPage;

/**
 * Permet la création de nouveau devis par un client.
 * 
 * @author Casaucau Cyril
 * 
 */
// TODO : Faire les messages de feedback, s'occuper de faire passer les infos à
// l'étape 3 avec le compound property model.
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	private WebMarkupContainer containerInscription;
	private WebMarkupContainer progressBar;
	private MapFrance carteFrance;
	private Label etape;

	private CreationAnnonceDTO creationAnnonce = new CreationAnnonceDTO();

	// Composant étape 2
	private WebMarkupContainer containerQualification;
	private Form<CreationAnnonceDTO> formQualification;
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
	private Button validateQualification;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true, "img/bg_title1.jpg");

		// Etape 1 : selection du departement avec la carte de la france
		carteFrance = new MapFrance("mapFrance");

		// Init l'étape 2 : Qualification du devis.
		containerQualification = new WebMarkupContainer("containerQualification");
		containerQualification.setVisible(false);

		formQualification = new Form<CreationAnnonceDTO>("formQualification",
				new CompoundPropertyModel<CreationAnnonceDTO>(creationAnnonce));
		formQualification.setMultiPart(true);

		corpsMetierSelect = new DropDownChoice<Metier>("corpsMetierSelect", new Model<Metier>(), Arrays.asList(Metier
				.values()));
		corpsMetierSelect.setRequired(true);
		corpsMetierSelect.setMarkupId("corpsMetierSelect");

		objetDevisField = new TextField<String>("objetDevisField", Model.of(""));
		objetDevisField.setRequired(true);
		objetDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_TITRE_MIN,
				ValidatorConstant.CREATION_ANNONCE_TITRE_MAX));
		objetDevisField.setMarkupId("objetDevisField");

		descriptionDevisField = new TextArea<String>("descriptionDevisField", Model.of(""));
		descriptionDevisField.setRequired(true);
		descriptionDevisField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MIN,
				ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MAX));
		descriptionDevisField.setMarkupId("descriptionDevisField");

		typeContactField = new DropDownChoice<TypeContact>("typeContactField", new Model<TypeContact>(),
				Arrays.asList(TypeContact.values()));
		;
		typeContactField.setRequired(true);
		typeContactField.setMarkupId("typeContactField");

		delaiInterventionField = new TextField<String>("delaiInterventionField", Model.of(""));
		delaiInterventionField.setRequired(true);
		delaiInterventionField.setMarkupId("delaiInterventionField");
		delaiInterventionField.add(StringValidator
				.maximumLength(ValidatorConstant.CREATION_ANNONCE_DELAI_INTERVENTION_MAX));

		nbDevisField = new NumberTextField<Integer>("nbDevisField", new Model<Integer>());
		nbDevisField.setRequired(true);
		nbDevisField.setMarkupId("nbDevisField");
		nbDevisField.add(RangeValidator.minimum(ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MIN));
		nbDevisField.add(RangeValidator.maximum(ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MAX));

		photoField = new FileUploadField("photoField");
		photoField.setMarkupId("photoField");

		adresseField = new TextField<String>("adresseField", Model.of(""));
		adresseField.setRequired(true);
		adresseField.setMarkupId("adresseField");
		adresseField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_ADRESSE_MIN,
				ValidatorConstant.CREATION_ANNONCE_ADRESSE_MAX));

		adresseComplementField = new TextField<String>("adresseComplementField", Model.of(""));
		adresseComplementField.setRequired(true);
		adresseComplementField.setMarkupId("adresseComplementField");
		adresseComplementField.add(StringValidator
				.maximumLength(ValidatorConstant.CREATION_ANNONCE_COMPLEMENT_ADRESSE_MAX));

		codePostalField = new TextField<String>("codePostalField", Model.of(""));
		codePostalField.setRequired(true);
		codePostalField.setMarkupId("codePostalField");
		codePostalField.add(StringValidator.exactLength(ValidatorConstant.CREATION_ANNONCE_CODEPOSTAL_MAX));

		villeField = new TextField<>("villeField", Model.of(""));
		villeField.setRequired(true);
		villeField.setMarkupId("villeField");
		villeField.add(StringValidator.maximumLength(ValidatorConstant.CREATION_ANNONCE_VILLE_MAX));

		validateQualification = new Button("validateQualification") {

			private static final long serialVersionUID = 4080197880572044860L;

			@Override
			public void onSubmit() {
				feedBackPanelGeneral.info("Submit OK !!!");
			}

		};

		containerQualification.add(formQualification);

		formQualification.add(corpsMetierSelect);
		formQualification.add(objetDevisField);
		formQualification.add(descriptionDevisField);
		formQualification.add(typeContactField);
		formQualification.add(delaiInterventionField);
		formQualification.add(nbDevisField);
		formQualification.add(photoField);
		formQualification.add(adresseField);
		formQualification.add(adresseComplementField);
		formQualification.add(codePostalField);
		formQualification.add(villeField);
		formQualification.add(validateQualification);

		// Init l'étape 3
		containerInscription = new WebMarkupContainer("containerInscription");
		containerInscription.setVisible(false);

		progressBar = new WebMarkupContainer("progressBar");

		etape = new Label("etape", new Model<String>());

		this.add(carteFrance);
		this.add(containerQualification);
		this.add(containerInscription);
		this.add(progressBar);
		this.add(etape);

		changementEtape("10", "Etape 1/3");
	}

	public NouveauDevis(PageParameters parameters) {
		this();
		// On récupere le departement qu'a choisi l'utilisateur
		String departement = parameters.get("departement").toString();
		if (departement != null) {
			etape2Qualification(departement);
		}

	}

	private void etape2Qualification(String departement) {
		carteFrance.setVisible(false);
		containerQualification.setVisible(true);
		changementEtape("33", "Etape 2/3");

	}

	private void etape3Inscription() {
		carteFrance.setVisible(false);
		containerQualification.setVisible(false);
		changementEtape("66", "Etape 3/3");
	}

	private void changementEtape(String percent, String etapeSur3) {
		progressBar.add(new AttributeModifier("data-percent", percent));
		etape.setDefaultModelObject(etapeSur3);
	}
}
