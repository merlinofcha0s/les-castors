package fr.batimen.web.client.extend.nouveau.artisan;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;
import fr.batimen.web.client.extend.nouveau.artisan.event.ChangementEtapeEvent;
import fr.batimen.web.client.validator.EmailUniquenessValidator;

public class Etape2PartenaireForm extends Form<CreationPartenaireDTO> {

    private static final long serialVersionUID = 8347005157184562826L;

    public Etape2PartenaireForm(String id, IModel<CreationPartenaireDTO> model) {
        super(id, model);

        final CreationPartenaireDTO nouveauPartenaire = model.getObject();

        this.setMarkupId("formPartenaireEtape2");

        DropDownChoice<Civilite> civilite = new DropDownChoice<Civilite>("artisan.civilite", Arrays.asList(Civilite
                .values()));
        civilite.setRequired(true);
        civilite.setMarkupId("civilite");
        civilite.add(new ErrorHighlightBehavior());
        civilite.add(new RequiredBorderBehaviour());

        TextField<String> nom = new TextField<String>("artisan.nom");
        nom.setRequired(true);
        nom.setMarkupId("nom");
        nom.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_NOM_MIN, ValidatorConstant.CLIENT_NOM_MAX));
        nom.add(new ErrorHighlightBehavior());
        nom.add(new RequiredBorderBehaviour());

        TextField<String> prenom = new TextField<String>("artisan.prenom");
        prenom.setRequired(true);
        prenom.setMarkupId("prenomField");
        prenom.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_PRENOM_MIN,
                ValidatorConstant.CLIENT_PRENOM_MAX));
        prenom.add(new ErrorHighlightBehavior());
        prenom.add(new RequiredBorderBehaviour());

        TextField<String> numeroTel = new TextField<String>("artisan.numeroTel");
        numeroTel.setRequired(true);
        numeroTel.setMarkupId("numeroTelField");
        numeroTel.add(new PatternValidator(ValidatorConstant.TELEPHONE_REGEX));
        numeroTel.add(new ErrorHighlightBehavior());
        numeroTel.add(new RequiredBorderBehaviour());

        TextField<String> email = new TextField<String>("artisan.email");
        email.setRequired(true);
        email.setMarkupId("emailField");
        email.add(EmailAddressValidator.getInstance());
        email.add(new EmailUniquenessValidator());
        email.add(new ErrorHighlightBehavior());
        email.add(new RequiredBorderBehaviour());

        TextField<String> identifiant = new TextField<String>("artisan.login");
        identifiant.setRequired(true);
        identifiant.setMarkupId("logintField");
        identifiant.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_LOGIN_RANGE_MIN,
                ValidatorConstant.CLIENT_LOGIN_RANGE_MAX));
        identifiant.add(new ErrorHighlightBehavior());
        identifiant.add(new RequiredBorderBehaviour());

        PasswordTextField passwordField = new PasswordTextField("artisan.password");
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

        AjaxSubmitLink validateEtape2Partenaire = new AjaxSubmitLink("validateEtape2Partenaire") {
            private static final long serialVersionUID = -171673358382084307L;

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
                nouveauPartenaire.setNumeroEtape(3);
                ChangementEtapeEvent changementEtapeEvent = new ChangementEtapeEvent(target, nouveauPartenaire);
                this.send(target.getPage(), Broadcast.EXACT, changementEtapeEvent);
            }

        };
        validateEtape2Partenaire.setMarkupId("validateEtape2Partenaire");

        this.add(civilite);
        this.add(nom);
        this.add(prenom);
        this.add(numeroTel);
        this.add(email);
        this.add(identifiant);
        this.add(passwordField);
        this.add(confirmPassword);
        this.add(validateEtape2Partenaire);

    }
}
