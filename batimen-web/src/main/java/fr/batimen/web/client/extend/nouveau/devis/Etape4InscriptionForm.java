package fr.batimen.web.client.extend.nouveau.devis;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.component.BatimenToolTip;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;
import fr.batimen.web.client.validator.CheckBoxTrueValidator;
import fr.batimen.web.client.validator.EmailUniquenessValidator;
import fr.batimen.web.client.validator.LoginUniquenessValidator;

/**
 * Form de l'etape 4 de création d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape4InscriptionForm extends Form<CreationAnnonceDTO> {

    private static final long serialVersionUID = 2500892594731116597L;

    private final CreationAnnonceDTO nouvelleAnnonce;

    private final PasswordTextField passwordField;
    private final TextField<String> nomField;
    private final TextField<String> prenomField;
    private final TextField<String> numeroTelField;
    private final TextField<String> emailField;
    private final TextField<String> loginField;
    private AjaxSubmitLink validateInscription;
    private final String idValidateInscription = "validateInscription";

    public Etape4InscriptionForm(String id, IModel<CreationAnnonceDTO> model) {
        super(id, model);

        this.setMarkupId("formEtape4");

        nouvelleAnnonce = model.getObject();

        nomField = new TextField<String>("client.nom");
        nomField.setMarkupId("nom");
        nomField.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_NOM_MIN, ValidatorConstant.CLIENT_NOM_MAX));

        prenomField = new TextField<String>("client.prenom");
        prenomField.setMarkupId("prenom");
        prenomField.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_PRENOM_MIN,
                ValidatorConstant.CLIENT_PRENOM_MAX));

        numeroTelField = new TextField<String>("client.numeroTel");
        numeroTelField.setMarkupId("numeroTel");
        numeroTelField.setRequired(true);
        numeroTelField.add(new RequiredBorderBehaviour());
        numeroTelField.add(new ErrorHighlightBehavior());
        numeroTelField.add(new PatternValidator(ValidatorConstant.TELEPHONE_REGEX));

        emailField = new TextField<String>("client.email");
        emailField.setMarkupId("email");
        emailField.setRequired(true);
        emailField.add(new RequiredBorderBehaviour());
        emailField.add(new ErrorHighlightBehavior());
        emailField.add(EmailAddressValidator.getInstance());
        emailField.add(new EmailUniquenessValidator());

        loginField = new TextField<String>("client.login");
        loginField.setMarkupId("login");
        loginField.setRequired(true);
        loginField.add(new RequiredBorderBehaviour());
        loginField.add(new ErrorHighlightBehavior());
        loginField.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_LOGIN_RANGE_MIN,
                ValidatorConstant.CLIENT_LOGIN_RANGE_MAX));
        loginField.add(new LoginUniquenessValidator());

        passwordField = new PasswordTextField("client.password");
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

        validateInscription = new AjaxSubmitLink(idValidateInscription) {

            private static final long serialVersionUID = 6200004097590331163L;

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onSubmit
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                nouvelleAnnonce.setNumeroEtape(5);
                ChangementEtapeClientEvent changementEtapeEventClient = new ChangementEtapeClientEvent(target,
                        nouvelleAnnonce);
                target.getPage().send(target.getPage(), Broadcast.BREADTH, changementEtapeEventClient);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onError
             * (org.apache.wicket.ajax.AjaxRequestTarget,
             * org.apache.wicket.markup.html.form.Form)
             */
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm());
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

        };
        validateInscription.setMarkupId(idValidateInscription);

        this.add(nomField);
        this.add(prenomField);
        this.add(numeroTelField);
        this.add(emailField);
        this.add(loginField);
        this.add(passwordField);
        this.add(confirmPassword);
        this.add(cguConfirm);
        this.add(cguLink);
        this.add(validateInscription);
        this.add(BatimenToolTip.getTooltipBehaviour());
    }

    /**
     * @return the passwordField
     */
    public PasswordTextField getPasswordField() {
        return passwordField;
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
     * @param validateInscription
     *            the validateInscription to set
     */
    public void setValidateInscription(AjaxSubmitLink validateInscription) {
        this.validateInscription = validateInscription;
    }

    /**
     * @return the idValidateInscription
     */
    public String getIdValidateInscription() {
        return idValidateInscription;
    }

}
