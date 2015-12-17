package fr.castor.web.client.modal;

import fr.castor.dto.constant.ValidatorConstant;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.client.component.LinkLabel;
import fr.castor.web.client.component.ModalCastor;
import fr.castor.web.client.event.LoginEvent;
import fr.castor.web.client.extend.Contact;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.web.client.extend.nouveau.devis.NouveauDevis;
import fr.castor.web.client.session.BatimenSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Panel qui contient un form permettant à l'utilisateur de pouvoir
 * s'identifier.
 * 
 * @author Casaucau Cyril
 * 
 */
public class AuthentificationModal extends ModalCastor {

    private static final long serialVersionUID = -1634093925835447825L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationModal.class);

    @Inject
    private Authentication authentication;

    @Inject
    private RolesUtils rolesUtils;

    private StatelessForm<AuthentificationModal> loginForm;
    private TextField<String> login;
    private PasswordTextField password;
    private LinkLabel passwordForgot;

    private Label errorLogin;

    public AuthentificationModal(String id) {
        super(id, "Connexion à l'espace client / artisan", "620");
        initForm();
    }

    private final void initForm() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialisation du form d'authentification");
        }

        loginForm = new StatelessForm<>("loginForm", new Model<>());

        login = new TextField<>("login", new Model<>());
        login.setMarkupId("loginModal");
        login.setRequired(true);
        login.add(new StringValidator(ValidatorConstant.CLIENT_LOGIN_RANGE_MIN,
                ValidatorConstant.CLIENT_LOGIN_RANGE_MAX));

        password = new PasswordTextField("password", new Model<>());
        password.setModelObject("Password");
        password.setMarkupId("passwordModal");
        password.setRequired(true);
        password.add(new StringValidator(ValidatorConstant.PASSWORD_RANGE_MIN, ValidatorConstant.PASSWORD_RANGE_MAX));

        AjaxSubmitLink signIn = new AjaxSubmitLink("signIn") {

            private static final long serialVersionUID = 3183458686534816645L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                BatimenSession session = (BatimenSession) Session.get();

                boolean authResult = session.signIn(login.getInput(), password.getConvertedInput());

                // if authentication succeeds redirect user to the requested
                // page
                if (authResult) {
                    // On envoi un event pour que le connected container
                    // (masterpage) se recharge

                    // Si il voulait aller sur une page en particulier, on
                    // le redirige vers celle ci
                    if(getPage().getClass().equals(NouveauDevis.class) || rolesUtils.checkRoles(TypeCompte.ADMINISTRATEUR)){
                        send(getPage(), Broadcast.BREADTH, new LoginEvent(target));
                        continueToOriginalDestination();
                    } else {
                        this.setResponsePage(MesAnnonces.class);
                    }

                } else {
                    AuthentificationModal.this.onError(target);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                AuthentificationModal.this.onError(target);
            }

        };

        signIn.setMarkupId("signInButton");

        errorLogin = new Label("errorLogin", "");
        errorLogin.setOutputMarkupId(true);

        Model<String> passwordForgotModel = new Model<>("Mot de passe oublié ?");

        passwordForgot = new LinkLabel("passwordForgot", passwordForgotModel) {
            @Override
            public void onClick() {
                this.setResponsePage(Contact.class);
            }
        };

        loginForm.add(signIn, login, password);

        this.add(loginForm, errorLogin, passwordForgot);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin initialisation du form d'authentification");
        }
    }

    public void resetLabelError() {
        errorLogin.setDefaultModelObject("");
        errorLogin.add(new AttributeModifier("class", "errorLoginDeactivated"));
    }

    public void onError(AjaxRequestTarget target) {
        errorLogin
                .setDefaultModelObject("Erreur dans la saisie / identifiants inconnues / compte pas activé, veuillez recommencer");
        errorLogin.add(new AttributeModifier("class", "errorLoginActivated"));
        target.add(errorLogin);
    }
}
