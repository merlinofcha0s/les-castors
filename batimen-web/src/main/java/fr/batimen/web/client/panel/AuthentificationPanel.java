package fr.batimen.web.client.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.client.event.LoginEvent;

public class AuthentificationPanel extends Panel {

	private static final long serialVersionUID = -1634093925835447825L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationPanel.class);

	private Form<AuthentificationPanel> loginForm;
	private TextField<String> login;
	private PasswordTextField password;
	private AjaxSubmitLink signIn;

	private Label errorLogin;

	public AuthentificationPanel(String id) {
		super(id);
		initForm();
	}

	private void initForm() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Initialisation du form d'authentification");
		}

		loginForm = new Form<AuthentificationPanel>("loginForm", new CompoundPropertyModel<AuthentificationPanel>(this));

		login = new TextField<String>("login", new Model<String>());
		login.setModelObject("Identifiant");
		login.setRequired(true);
		login.add(new StringValidator(ValidatorConstant.LOGIN_RANGE_MIN, ValidatorConstant.LOGIN_RANGE_MAX));

		password = new PasswordTextField("password", new Model<String>());
		password.setModelObject("Password");
		password.setRequired(true);
		password.add(new StringValidator(ValidatorConstant.PASSWORD_RANGE_MIN, ValidatorConstant.PASSWORD_RANGE_MAX));

		signIn = new AjaxSubmitLink("signIn") {

			private static final long serialVersionUID = 3183458686534816645L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				boolean authResult = AuthenticatedWebSession.get().signIn(login.getInput(),
						password.getConvertedInput());

				// if authentication succeeds redirect user to the requested
				// page
				if (authResult) {
					// On envoi un event pour que le connected container
					// (masterpage) se recharge
					send(getPage(), Broadcast.BREADTH, new LoginEvent(target));

					// Si il voulait aller sur une page en particulier, on
					// le redirige vers celle ci
					continueToOriginalDestination();
				} else {
					AuthentificationPanel.this.onError(target);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				AuthentificationPanel.this.onError(target);
			}

		};

		signIn.setMarkupId("signInButton");

		errorLogin = new Label("errorLogin", "");
		errorLogin.setOutputMarkupId(true);

		loginForm.add(signIn);
		loginForm.add(login);
		loginForm.add(password);

		this.add(loginForm);
		this.add(errorLogin);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fin initialisation du form d'authentification");
		}
	}

	public void resetLabelError() {
		errorLogin.setDefaultModelObject("");
		errorLogin.add(new AttributeModifier("class", "errorLoginDeactivated"));
	}

	public void onError(AjaxRequestTarget target) {
		errorLogin.setDefaultModelObject("Erreur dans la saisie ou identifiants inconnues, veuillez recommencer");
		errorLogin.add(new AttributeModifier("class", "errorLoginActivated"));
		target.add(errorLogin);
	}
}
