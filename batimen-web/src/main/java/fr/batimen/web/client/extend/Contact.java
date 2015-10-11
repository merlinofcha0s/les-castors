package fr.batimen.web.client.extend;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.constant.UrlPage;
import fr.batimen.dto.CaptchaDTO;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.constants.WebConstants;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.component.ReCaptcha;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.enums.PropertiesFileWeb;
import fr.batimen.ws.client.service.ContactUsServiceREST;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import javax.inject.Inject;

/**
 * Page de contact qui permettra aux utilisateur de contacter l'équipe
 *
 * @author Casaucau Cyril
 */
public class Contact extends MasterPage {

    private static final long serialVersionUID = -2549295715502248172L;

    @Inject
    private ContactUsServiceREST contactUsServiceREST;

    @Inject
    private Authentication authentication;

    /**
     * View Components
     */
    private AjaxLink<String> resetButton;
    private AjaxSubmitLink submitButton;
    private TextField<String> nameField;
    private TextField<String> emailField;
    private TextField<String> subjectField;
    private TextArea<String> messageField;
    private Form<ContactMailDTO> formContact;
    private ReCaptcha reCaptcha;
    private WebMarkupContainer formContainerContact;

    public Contact() {
        super("Contacter l'equipe des castors", "contactez lescastors contacte question", "Contacter l'équipe des castors", true, "img/bg_title1.jpg");
        initComponents();
    }

    /**
     * Component initialization method
     */
    private void initComponents() {
        final ContactMailDTO contactMailDTO = new ContactMailDTO();
        formContact = new Form<>("contactForm", new CompoundPropertyModel<>(contactMailDTO));
        formContact.setOutputMarkupId(true);

        nameField = new TextField<String>("name") {
            @Override
            public boolean isVisible() {
                return !authentication.isAuthenticated();
            }
        };
        nameField.add(new ErrorHighlightBehavior());
        nameField
                .add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_NOM_MIN, ValidatorConstant.CLIENT_NOM_MAX));

        emailField = new TextField<String>("email") {
            @Override
            public boolean isVisible() {
                return !authentication.isAuthenticated();
            }
        };

        emailField.add(EmailAddressValidator.getInstance());
        emailField.add(new ErrorHighlightBehavior());

        subjectField = new TextField<>("subject");
        subjectField.setRequired(true);
        subjectField.add(new ErrorHighlightBehavior());

        messageField = new TextArea<>("message");
        messageField.setRequired(true);
        messageField.add(new ErrorHighlightBehavior());

        if (authentication.isAuthenticated()) {
            emailField.setRequired(false);
            nameField.setRequired(false);
        } else {
            emailField.setRequired(true);
            nameField.setRequired(true);
        }

        reCaptcha = new ReCaptcha("reCaptcha") {
            @Override
            public boolean isVisible() {
                return !authentication.isAuthenticated();
            }
        };

        // Init du submit button
        submitButton = new AjaxSubmitLink("submitButton") {
            private static final long serialVersionUID = 5400416625335864317L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                CaptchaDTO captchaDTO = reCaptcha.verifyCaptcha();

                if (!Boolean.valueOf(captchaDTO.getSuccess()) && Boolean.valueOf(PropertiesFileWeb.APP.getProperties().getProperty("app.activate.captcha")) && !authentication.isAuthenticated()) {
                    MasterPage.triggerEventFeedBackPanel(target, "Veuillez cocher le recaptcha avant de pouvoir continuer", FeedbackMessageLevel.ERROR);
                } else {
                    if (authentication.isAuthenticated()) {
                        contactMailDTO.setEmail(authentication.getCurrentUserInfo().getEmail());
                        contactMailDTO.setName(authentication.getCurrentUserInfo().getLogin());
                    }

                    // send shit to server
                    int response = contactUsServiceREST.pushContactMail(contactMailDTO);
                    // finalize
                    if (response == CodeRetourService.RETOUR_OK) {
                        feedBackPanelGeneral
                                .success("Votre message a été transmis correctement. Nous vous répondrons dans les plus brefs délais.");
                        nameField.setDefaultModelObject("");
                        emailField.setDefaultModelObject("");
                        subjectField.setDefaultModelObject("");
                        messageField.setDefaultModelObject("");
                    } else {
                        feedBackPanelGeneral
                                .error("Une erreur est survenue lors de l'envoi du message.\nNous vous prions de nous excuser et de renouveller votre envoi ulterieurement");
                    }
                    target.add(feedBackPanelGeneral);
                    target.add(formContainerContact);
                    target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(formContainerContact);
                target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
                this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
            }

        };

        resetButton = new AjaxLink<String>("resetButton") {
            private static final long serialVersionUID = -8327260512161698549L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                nameField.setDefaultModelObject("");
                emailField.setDefaultModelObject("");
                subjectField.setDefaultModelObject("");
                messageField.setDefaultModelObject("");
                target.add(formContact);
                target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
            }
        };

        formContainerContact = new WebMarkupContainer("formContainerContact");
        formContainerContact.setOutputMarkupId(true);
        formContainerContact.add(nameField, emailField, subjectField, messageField);

        // add components to view
        formContact.add(formContainerContact, reCaptcha, resetButton, submitButton);

        add(formContact);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        StringBuilder adresseContact = new StringBuilder();
        adresseContact.append("https://lescastors.fr").append(UrlPage.CONTACT_URL);

        //Opengraph tags
        response.render(addOpenGraphMetaResourcesToHeader("og:url", adresseContact.toString()));
        response.render(addOpenGraphMetaResourcesToHeader("og:type", "website"));
        response.render(addOpenGraphMetaResourcesToHeader("og:title", "Contacter l'équipe du site lescastors.fr"));
        response.render(addOpenGraphMetaResourcesToHeader("og:description", "Posez vos questions à notre équipe"));
        response.render(addOpenGraphMetaResourcesToHeader("og:image", "https://res.cloudinary.com/lescastors/image/upload/v1443971771/mail/logo-bleu2x.png"));
    }
}
