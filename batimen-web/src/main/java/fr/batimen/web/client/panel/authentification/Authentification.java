package fr.batimen.web.client.panel.authentification;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.client.master.MasterPage;

/**
 * Panel Wicket servant à authentifier l'utilisateur
 * 
 * @author Casaucau Cyril
 * 
 */
public final class Authentification extends MasterPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1451418474286472533L;
	private static final Logger logger = LoggerFactory.getLogger(Authentification.class);

	private Label hello;
	private Form<Authentification> loginForm;
	private TextField<String> login;
	private PasswordTextField password;
	private Button signIn;
	private Label error;
	private FeedbackPanel feedBackLogin;

	public Authentification() {
		super();
		initForm();
	}

	public Authentification(PageParameters params) {
		this();
	}

	private void initForm() {

		if (logger.isDebugEnabled()) {
			logger.debug("Initialisation du form");
		}

		hello = new Label("loginHello", "Bienvenue sur Batimen.fr veuillez vous identifier.");

		loginForm = new Form<Authentification>("loginForm", new CompoundPropertyModel<Authentification>(this));

		login = new TextField<String>("login", new Model<String>());
		login.setModelObject("Identifiant");
		login.setRequired(true);
		login.add(new StringValidator(ValidatorConstant.LOGIN_DTO_LOGIN_RANGE_MIN,
				ValidatorConstant.LOGIN_DTO_LOGIN_RANGE_MAX));

		password = new PasswordTextField("password", new Model<String>());
		password.setModelObject("Password");
		password.setRequired(true);
		password.add(new StringValidator(ValidatorConstant.LOGIN_DTO_PASSWORD_RANGE_MIN,
				ValidatorConstant.LOGIN_DTO_PASSWORD_RANGE_MAX));

		error = new Label("error", new Model<String>());
		error.setDefaultModelObject("Compte inexistant ou mot de passe incorrect");
		error.setVisible(false);

		signIn = new Button("signIn") {

			private static final long serialVersionUID = 975834590754460546L;

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.apache.wicket.ajax.markup.html.form.AjaxButton#onSubmit(org
			 * .apache.wicket.ajax.AjaxRequestTarget,
			 * org.apache.wicket.markup.html.form.Form)
			 */
			@Override
			public void onSubmit() {
				boolean authResult = AuthenticatedWebSession.get().signIn(login.getInput(),
						password.getConvertedInput());
				// if authentication succeeds redirect user to the requested
				// page
				if (authResult) {
					continueToOriginalDestination();
				} else {
					error.setVisible(true);
				}
			}
		};

		feedBackLogin = new FeedbackPanel("feedBackLogin");

		loginForm.add(error);
		loginForm.add(signIn);
		loginForm.add(login);
		loginForm.add(password);

		this.add(hello);
		this.add(loginForm);
		this.add(feedBackLogin);

		if (logger.isDebugEnabled()) {
			logger.debug("Fin initialisation du form");
		}

	}
}
