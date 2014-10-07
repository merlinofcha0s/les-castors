package fr.batimen.web.client.extend;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import fr.batimen.dto.ContactMailDTO;
import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.web.app.constants.WebConstants;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.event.FeedBackPanelEvent;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ContactUsService;

/**
 * Page de contact qui permettra aux utilisateur de contacter l'équipe
 * 
 * @author Casaucau Cyril
 * 
 */
public class Contact extends MasterPage {

	private static final long serialVersionUID = -2549295715502248172L;

	/**
	 * View Components
	 */
	private AjaxLink<String> resetButton;
	private AjaxSubmitLink submitButton;
	private TextField<String> nameField;
	private TextField<String> emailField;
	private TextField<String> subjectField;
	private TextArea<String> messageField;
	private Form<ContactMailDTO> form;

	public Contact() {
		super("Contact de castors.fr", "", "Nous contacter", true, "img/bg_title1.jpg");
		initComponents();
	}

	/**
	 * TODO javadoc
	 */
	private void initComponents() {
		ContactMailDTO contactMailDTO = new ContactMailDTO();
		form = new Form<ContactMailDTO>("contactForm", new CompoundPropertyModel<ContactMailDTO>(
				contactMailDTO));
		form.setOutputMarkupId(true);

		nameField = new TextField<String>("name");
		nameField.setRequired(true);
		nameField.add(new ErrorHighlightBehavior());
		nameField.add(StringValidator.lengthBetween(ValidatorConstant.CLIENT_NOM_MIN,
				ValidatorConstant.CLIENT_NOM_MAX));
		emailField = new TextField<String>("email");
		emailField.setRequired(true);
		emailField.add(EmailAddressValidator.getInstance());
		emailField.add(new ErrorHighlightBehavior());

		subjectField = new TextField<String>("subject");
		subjectField.setRequired(true);
		subjectField.add(new ErrorHighlightBehavior());

		messageField = new TextArea<String>("message");
		messageField.setRequired(true);
		messageField.add(new ErrorHighlightBehavior());

		// TODO Auto-generated method stub
		submitButton = new AjaxSubmitLink("submitButton") {
			private static final long serialVersionUID = 5400416625335864317L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// send shit to server
				int response = ContactUsService.pushContactMail((ContactMailDTO) getForm()
						.getModelObject());
				// finalize
				feedBackPanelGeneral.success("yolo");
				target.add(feedBackPanelGeneral);
				target.add(getForm());
				target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(getForm());
				target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
				this.send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
			}

		};

		resetButton = new AjaxLink<String>("resetButton") {
			private static final long serialVersionUID = -8327260512161698549L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				nameField.clearInput();
				emailField.clearInput();
				subjectField.clearInput();
				messageField.clearInput();
				target.add(form);
				target.appendJavaScript(WebConstants.JS_WINDOW_RESIZE_EVENT);
			}
		};

		// add components to view
		form.add(nameField);
		form.add(emailField);
		form.add(subjectField);
		form.add(messageField);
		form.add(resetButton);
		form.add(submitButton);

		this.add(form);

	}
}
