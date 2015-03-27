package fr.batimen.web.client.extend.nouveau.devis;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.app.constants.Etape;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.app.utils.ProgrammaticBeanLookup;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.extend.CGU;
import fr.batimen.web.client.extend.member.client.ModifierMonProfil;
import fr.batimen.web.client.extend.nouveau.devis.event.ChangementEtapeClientEvent;
import fr.batimen.web.client.validator.ChangePasswordValidator;
import fr.batimen.web.client.validator.CheckBoxTrueValidator;
import fr.batimen.web.client.validator.EmailUniquenessValidator;
import fr.batimen.web.client.validator.LoginUniquenessValidator;
import fr.batimen.ws.client.service.UtilisateurServiceREST;

/**
 * Form de l'etape 4 de création d'annonce.
 * 
 * @author Casaucau Cyril
 * 
 */
public class Etape4InscriptionForm extends Form<CreationAnnonceDTO> {

    private static final long serialVersionUID = 2500892594731116597L;

    @Inject
    private UtilisateurServiceREST utilisateurServiceREST;

    @Inject
    private Authentication authentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierMonProfil.class);

    private final CreationAnnonceDTO nouvelleAnnonce;

    private final PasswordTextField passwordField;
    private final PasswordTextField confirmPassword;
    private final PasswordTextField oldPasswordField;
    private final TextField<String> nomField;
    private final TextField<String> prenomField;
    private final TextField<String> numeroTelField;
    private final TextField<String> emailField;
    private final TextField<String> loginField;
    private AjaxSubmitLink validateInscription;
    private static final String ID_VALIDATE_INSCRIPTION = "validateInscription";
    private WebMarkupContainer cguContainer;
    private CheckBox cguConfirm;

    public Etape4InscriptionForm(String id, IModel<CreationAnnonceDTO> model, final Boolean forModification) {
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
        numeroTelField.add(new ErrorHighlightBehavior());
        numeroTelField.add(new PatternValidator(ValidatorConstant.TELEPHONE_REGEX));

        emailField = new TextField<String>("client.email");
        emailField.setMarkupId("email");
        emailField.setRequired(true);
        emailField.add(new RequiredBorderBehaviour());
        emailField.add(new ErrorHighlightBehavior());
        emailField.add(EmailAddressValidator.getInstance());

        EmailUniquenessValidator emailUniquenessValidator = (EmailUniquenessValidator) ProgrammaticBeanLookup
                .lookup("emailUniquenessValidator");

        emailField.add(emailUniquenessValidator);

        loginField = new TextField<String>("client.login");
        loginField.setMarkupId("login");
        loginField.setRequired(true);
        loginField.add(new RequiredBorderBehaviour());
        loginField.add(new ErrorHighlightBehavior());
        loginField.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_LOGIN_RANGE_MIN,
                ValidatorConstant.CLIENT_LOGIN_RANGE_MAX));

        LoginUniquenessValidator loginUniquenessValidator = (LoginUniquenessValidator) ProgrammaticBeanLookup
                .lookup("loginUniquenessValidator");

        loginField.add(loginUniquenessValidator);

        passwordField = new PasswordTextField("client.password");
        passwordField.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
                ValidatorConstant.PASSWORD_RANGE_MAX));

        confirmPassword = new PasswordTextField("confirmPassword", new Model<String>());
        confirmPassword.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
                ValidatorConstant.PASSWORD_RANGE_MAX));

        oldPasswordField = new PasswordTextField("client.oldPassword");
        oldPasswordField.add(StringValidator.lengthBetween(ValidatorConstant.PASSWORD_RANGE_MIN,
                ValidatorConstant.PASSWORD_RANGE_MAX));

        WebMarkupContainer oldPasswordContainer = new WebMarkupContainer("oldPasswordContainer") {

            private static final long serialVersionUID = 7652901872189255854L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return forModification;
            }

        };

        oldPasswordContainer.add(oldPasswordField);

        this.add(new EqualPasswordInputValidator(passwordField, confirmPassword));

        cguContainer = new WebMarkupContainer("CGUContainer") {

            private static final long serialVersionUID = 2137784015650154163L;

            @Override
            public boolean isVisible() {
                return !forModification;
            }

        };

        Link<String> cguLink = new Link<String>("cguLink") {

            private static final long serialVersionUID = -7368483899425701479L;

            @Override
            public void onClick() {
                this.setResponsePage(CGU.class);
            }
        };

        cguLink.setMarkupId("cguLink");
        cguContainer.add(cguLink);

        cguConfirm = new CheckBox("cguConfirmation", Model.of(Boolean.FALSE));
        cguContainer.add(cguConfirm);

        initChooser(forModification);

        AjaxLink<Void> etapePrecedente4 = new AjaxLink<Void>("etapePrecedente4") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                NouveauUtils.sendEventForPreviousStep(target, Etape.ETAPE_4.ordinal() + 1);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return !forModification;
            }

        };

        etapePrecedente4.setOutputMarkupId(true);
        etapePrecedente4.setMarkupId("etapePrecedente4");

        validateInscription = new AjaxSubmitLink(ID_VALIDATE_INSCRIPTION) {

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
                if (forModification) {
                    modificationCheckAndRecording();
                    target.add(getForm());
                    this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
                } else {
                    nouvelleAnnonce.setNumeroEtape(5);
                    ChangementEtapeClientEvent changementEtapeEventClient = new ChangementEtapeClientEvent(target,
                            nouvelleAnnonce);
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, changementEtapeEventClient);
                }

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
        validateInscription.setMarkupId(ID_VALIDATE_INSCRIPTION);

        this.add(nomField, prenomField, numeroTelField, emailField, loginField, passwordField, confirmPassword,
                oldPasswordContainer, cguContainer, validateInscription, etapePrecedente4);
    }

    public void initChooser(final Boolean forModification) {

        if (!forModification) {
            cguConfirm.setRequired(true);
            cguConfirm.add(new CheckBoxTrueValidator());
            cguConfirm.add(new RequiredBorderBehaviour());
            confirmPassword.setMarkupId("confirmPassword");
            confirmPassword.setRequired(true);
            confirmPassword.add(new RequiredBorderBehaviour());
            confirmPassword.add(new ErrorHighlightBehavior());
            passwordField.setMarkupId("password");
            passwordField.setRequired(true);
            passwordField.add(new RequiredBorderBehaviour());
            passwordField.add(new ErrorHighlightBehavior());
        } else {
            passwordField.setRequired(Boolean.FALSE);
            confirmPassword.setRequired(Boolean.FALSE);
            oldPasswordField.setRequired(Boolean.FALSE);
            this.add(new ChangePasswordValidator(oldPasswordField, passwordField, confirmPassword));
        }

    }

    private void modificationCheckAndRecording() {
        // Si l'utilisateur veut modifier son mot de passe (pas
        // besoin de reverifier tout les champs, le validateur l'a
        // deja fait)
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Modification des données de l'utilisateur : "
                    + authentication.getCurrentUserInfo().getLogin());
        }

        Boolean isPasswordMatch = Boolean.FALSE;
        if (!passwordField.getInput().isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Modification de mot de passe detectée");
            }
            String oldPassword = nouvelleAnnonce.getClient().getOldPassword();
            String newPassword = nouvelleAnnonce.getClient().getPassword();
            isPasswordMatch = HashHelper.check(oldPassword, authentication.getCurrentUserInfo().getPassword());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Check de l'ancien pasword : " + isPasswordMatch);
            }
            // SI le password match avec le hash
            if (isPasswordMatch) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Hashing du nouveau mot de passe");
                }
                nouvelleAnnonce.getClient().setPassword(HashHelper.hashScrypt(newPassword));
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Ancien mot de passe invalide");
                }
                error("Ancien mot de passe invalide");
            }
            // Si il ne veut pas changer son mot de passe on le set
            // pour eviter l'ecrasement de celui ci (pour model
            // mapper)
        } else {
            // Dans ce cas, il n'y a pas de modification de mot de
            // passe, on considere qu'il match pour activer l'appel
            // au webservice
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pas de modification de mot de passe");
                LOGGER.debug("On set le password actuel de l'utilisateur");
            }
            isPasswordMatch = Boolean.TRUE;
            nouvelleAnnonce.getClient().setPassword(authentication.getCurrentUserInfo().getPassword());
        }

        if (isPasswordMatch) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Les données et le champs requis sont valides, on appel le webservice pour enregistrer les données");
            }
            // Call ws service pour la mise a jour des données.
            Integer codeRetour = utilisateurServiceREST.updateUtilisateurInfos(nouvelleAnnonce.getClient());

            if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                // Si tout est ok setter les infos du client dto
                // dans la session
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Set des nouvelles informations en session de l'utilisateur");
                }
                authentication.setCurrentUserInfo(nouvelleAnnonce.getClient());
                success("Vos données ont bien été mises à jour");
            } else {
                error("Problème de mise à jour avec vos données, veuillez réessayer plus tard");
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Résultat de l'appel du webservice : " + codeRetour);
            }
        }
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
        return ID_VALIDATE_INSCRIPTION;
    }

}
