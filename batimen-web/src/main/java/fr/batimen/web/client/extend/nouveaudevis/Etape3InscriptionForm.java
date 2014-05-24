package fr.batimen.web.client.extend.nouveaudevis;

import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.BatimenToolTip;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.validator.CheckBoxTrueValidator;
import fr.batimen.ws.client.service.ClientService;

public class Etape3InscriptionForm extends Form<CreationAnnonceDTO> {

	private static final long serialVersionUID = 2500892594731116597L;

	private PasswordTextField passwordField;
	private DropDownChoice<Civilite> civiliteField;
	private TextField<String> nomField;
	private TextField<String> prenomField;
	private TextField<String> numeroTelField;
	private TextField<String> emailField;
	private TextField<String> loginField;
	private boolean emailOK = false;
	private boolean loginOK = false;

	public Etape3InscriptionForm(String id, IModel<CreationAnnonceDTO> model) {
		super(id, model);

		this.setMarkupId("formEtape3");

		String idCiviliteField = "civilite";
		String idNomField = "nom";
		String idPrenomField = "prenom";
		String idValidateInscription = "validateInscription";

		civiliteField = new DropDownChoice<Civilite>(idCiviliteField, Arrays.asList(Civilite.values()));
		civiliteField.setMarkupId(idCiviliteField);

		nomField = new TextField<String>(idNomField);
		nomField.setMarkupId(idNomField);
		nomField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_NOM_MIN,
		        ValidatorConstant.CREATION_ANNONCE_NOM_MAX));

		prenomField = new TextField<String>(idPrenomField);
		prenomField.setMarkupId(idPrenomField);
		prenomField.add(StringValidator.lengthBetween(ValidatorConstant.CREATION_ANNONCE_PRENOM_MIN,
		        ValidatorConstant.CREATION_ANNONCE_PRENOM_MAX));

		numeroTelField = new TextField<String>("numeroTel");
		numeroTelField.setMarkupId("numeroTel");
		numeroTelField.setRequired(true);
		numeroTelField.add(new RequiredBorderBehaviour());
		numeroTelField.add(new ErrorHighlightBehavior());
		numeroTelField.add(new PatternValidator(ValidatorConstant.CREATION_ANNONCE_TELEPHONE_REGEX));

		final Image confirmationEmailNotUse = new Image("confirmationEmailNotUse", new ContextRelativeResource(
		        "img/ok.png"));
		confirmationEmailNotUse.setVisible(false);
		confirmationEmailNotUse.setOutputMarkupId(true);
		// On le render qu'il soit visible ou pas (utilisé pour la maj en ajax)
		confirmationEmailNotUse.setOutputMarkupPlaceholderTag(true);

		emailField = new TextField<String>("email");
		emailField.setMarkupId("email");
		emailField.setRequired(true);
		emailField.add(new RequiredBorderBehaviour());
		emailField.add(new ErrorHighlightBehavior());
		emailField.add(EmailAddressValidator.getInstance());

		emailField.add(emailAjaxVerification(confirmationEmailNotUse));

		final Image confirmationLoginNotUse = new Image("confirmationLoginNotUse", new ContextRelativeResource(
		        "img/ok.png"));
		confirmationLoginNotUse.setVisible(false);
		confirmationLoginNotUse.setOutputMarkupId(true);
		// On le render qu'il soit visible ou pas (obligatoire pour la maj en
		// ajax)
		confirmationLoginNotUse.setOutputMarkupPlaceholderTag(true);

		loginField = new TextField<String>("login");
		loginField.setMarkupId("login");
		loginField.setRequired(true);
		loginField.add(new RequiredBorderBehaviour());
		loginField.add(new ErrorHighlightBehavior());
		loginField.add(StringValidator.lengthBetween(ValidatorConstant.LOGIN_RANGE_MIN,
		        ValidatorConstant.LOGIN_RANGE_MAX));
		loginField.add(loginAjaxVerification(confirmationLoginNotUse));

		passwordField = new PasswordTextField("password");
		passwordField.setMarkupId("password");
		passwordField.setRequired(true);
		passwordField.add(new RequiredBorderBehaviour());
		passwordField.add(new ErrorHighlightBehavior());
		passwordField.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
		        ValidatorConstant.PASSWORD_RANGE_MAX));

		PasswordTextField confirmPassword = new PasswordTextField("confirmPassword", new Model<String>());
		confirmPassword.setMarkupId("confirmPassword");
		confirmPassword.setRequired(true);
		confirmPassword.add(new RequiredBorderBehaviour());
		confirmPassword.add(new ErrorHighlightBehavior());
		confirmPassword.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
		        ValidatorConstant.PASSWORD_RANGE_MAX));

		this.add(new EqualPasswordInputValidator(passwordField, confirmPassword));

		CheckBox cguConfirm = new CheckBox("cguConfirmation", Model.of(Boolean.FALSE));
		cguConfirm.setRequired(true);
		cguConfirm.add(new CheckBoxTrueValidator());
		cguConfirm.add(new RequiredBorderBehaviour());

		Link<String> cguLink = new Link<String>("cguLink") {

			private static final long serialVersionUID = -7368483899425701479L;

			@Override
			public void onClick() {
				this.setResponsePage(CGU.class);
			}
		};

		cguLink.setMarkupId("cguLink");

		SubmitLink validateInscription = new SubmitLink(idValidateInscription);
		validateInscription.setMarkupId(idValidateInscription);

		this.add(civiliteField);
		this.add(nomField);
		this.add(prenomField);
		this.add(numeroTelField);
		this.add(emailField);
		this.add(confirmationEmailNotUse);
		this.add(loginField);
		this.add(confirmationLoginNotUse);
		this.add(passwordField);
		this.add(confirmPassword);
		this.add(cguConfirm);
		this.add(cguLink);
		this.add(validateInscription);
		this.add(BatimenToolTip.getTooltipBehaviour());
	}

	private AjaxFormComponentUpdatingBehavior emailAjaxVerification(final Image confirmationEmailNotUse) {
		return new AjaxFormComponentUpdatingBehavior("onblur") {

			private static final long serialVersionUID = -7445370917612037362L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				ClientDTO checkedClient = ClientService.getClientByEmail(emailField.getConvertedInput());

				if (checkedClient.getEmail().equals("")) {
					confirmationEmailNotUse.setImageResource(new ContextRelativeResource("img/ok.png"));
					confirmationEmailNotUse.setVisible(true);
					confirmationEmailNotUse.add(new AttributeModifier("title", "Email OK"));
					emailOK = true;
				} else {
					confirmationEmailNotUse.setImageResource(new ContextRelativeResource("img/error.png"));
					confirmationEmailNotUse.setVisible(true);
					confirmationEmailNotUse.add(new AttributeModifier("title", "Email déjà enregistré"));
					emailOK = false;
				}

				target.add(confirmationEmailNotUse);
			}

		};
	}

	private AjaxFormComponentUpdatingBehavior loginAjaxVerification(final Image confirmationLoginNotUse) {
		return new AjaxFormComponentUpdatingBehavior("onblur") {

			private static final long serialVersionUID = 658096645690773662L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				LoginDTO loginToCheck = new LoginDTO();
				loginToCheck.setLogin(loginField.getConvertedInput());

				ClientDTO checkedClient = ClientService.login(loginToCheck);

				if (checkedClient.getLogin().equals("")) {
					confirmationLoginNotUse.setImageResource(new ContextRelativeResource("img/ok.png"));
					confirmationLoginNotUse.setVisible(true);
					confirmationLoginNotUse.add(new AttributeModifier("title", "Nom d'utilisateur disponible"));
					loginOK = true;
				} else {
					confirmationLoginNotUse.setImageResource(new ContextRelativeResource("img/error.png"));
					confirmationLoginNotUse.setVisible(true);
					confirmationLoginNotUse.add(new AttributeModifier("title", "Nom d'utilisateur non disponible"));
					loginOK = false;

				}

				target.add(confirmationLoginNotUse);
			}

		};

	}

	/**
	 * @return the passwordField
	 */
	public PasswordTextField getPasswordField() {
		return passwordField;
	}

	/**
	 * @return the civiliteField
	 */
	public DropDownChoice<Civilite> getCiviliteField() {
		return civiliteField;
	}

	/**
	 * @return the nomField
	 */
	public TextField<String> getNomField() {
		return nomField;
	}

	/**
	 * @return the prenomField
	 */
	public TextField<String> getPrenomField() {
		return prenomField;
	}

	/**
	 * @return the numeroTelField
	 */
	public TextField<String> getNumeroTelField() {
		return numeroTelField;
	}

	/**
	 * @return the emailField
	 */
	public TextField<String> getEmailField() {
		return emailField;
	}

	/**
	 * @return the loginField
	 */
	public TextField<String> getLoginField() {
		return loginField;
	}

	/**
	 * @return the emailOK
	 */
	public boolean isEmailOK() {
		return emailOK;
	}

	/**
	 * @return the loginOK
	 */
	public boolean isLoginOK() {
		return loginOK;
	}

}
