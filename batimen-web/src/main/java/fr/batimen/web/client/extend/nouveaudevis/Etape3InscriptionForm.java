package fr.batimen.web.client.extend.nouveaudevis;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;

public class Etape3InscriptionForm extends Form<CreationAnnonceDTO> {

	private static final long serialVersionUID = 2500892594731116597L;

	private DropDownChoice<Civilite> civiliteField;
	private TextField<String> nomField;
	private TextField<String> prenomField;
	private TextField<String> numeroTelField;
	private TextField<String> emailField;
	private TextField<String> loginField;
	private PasswordTextField passwordField;
	private PasswordTextField confirmPassword;
	private SubmitLink validateInscription;

	public Etape3InscriptionForm(String id, IModel<CreationAnnonceDTO> model) {
		super(id, model);

		this.setMarkupId("formEtape3");

		civiliteField = new DropDownChoice<Civilite>("civilite", Arrays.asList(Civilite.values()));
		civiliteField.setMarkupId("civilite");

		nomField = new TextField<String>("nom");
		nomField.setMarkupId("nom");
		nomField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_NOM_MIN,
				ValidatorConstant.CREATION_ANNONCE_NOM_MAX));

		prenomField = new TextField<String>("prenom");
		prenomField.setMarkupId("prenom");
		prenomField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_PRENOM_MIN,
				ValidatorConstant.CREATION_ANNONCE_PRENOM_MAX));

		numeroTelField = new TextField<String>("numeroTel");
		numeroTelField.setRequired(true);
		numeroTelField.add(new RequiredBorderBehaviour());
		numeroTelField.add(new ErrorHighlightBehavior());
		numeroTelField.add(new PatternValidator(ValidatorConstant.CREATION_ANNONCE_TELEPHONE_REGEX));

		emailField = new TextField<String>("email");
		emailField.setRequired(true);
		emailField.add(new RequiredBorderBehaviour());
		emailField.add(new ErrorHighlightBehavior());
		emailField.add(EmailAddressValidator.getInstance());

		loginField = new TextField<String>("login");
		loginField.setRequired(true);
		loginField.add(new RequiredBorderBehaviour());
		loginField.add(new ErrorHighlightBehavior());
		loginField.add(StringValidator.lengthBetween(ValidatorConstant.LOGIN_RANGE_MIN,
				ValidatorConstant.LOGIN_RANGE_MAX));

		passwordField = new PasswordTextField("password");
		passwordField.setRequired(true);
		passwordField.add(new RequiredBorderBehaviour());
		passwordField.add(new ErrorHighlightBehavior());
		passwordField.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
				ValidatorConstant.PASSWORD_RANGE_MAX));

		confirmPassword = new PasswordTextField("confirmPassword", new Model<String>());
		confirmPassword.setRequired(true);
		confirmPassword.add(new RequiredBorderBehaviour());
		confirmPassword.add(new ErrorHighlightBehavior());
		confirmPassword.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
				ValidatorConstant.PASSWORD_RANGE_MAX));

		passwordField.add(new EqualPasswordInputValidator(passwordField, confirmPassword));

		validateInscription = new SubmitLink("validateInscription");
		validateInscription.setMarkupId("validateInscription");

		this.add(civiliteField);
		this.add(nomField);
		this.add(prenomField);
		this.add(numeroTelField);
		this.add(emailField);
		this.add(loginField);
		this.add(passwordField);
		this.add(confirmPassword);
		this.add(validateInscription);
	}
}
