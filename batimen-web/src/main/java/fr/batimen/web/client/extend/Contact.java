package fr.batimen.web.client.extend;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import fr.batimen.web.app.constants.WebConstants;
import fr.batimen.web.client.master.MasterPage;

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
	private AjaxLink<String> submitButton;
	private TextField<String> nameField;
	private TextField<String> emailField;
	private TextField<String> subjectField;
	private TextArea<String> messageField;
	private Form<String> form;

	public Contact() {
		super("Contact de castors.fr", "", "Nous contacter", true, "img/bg_title1.jpg");
		initComponents();
	}

	/**
	 * TODO javadoc
	 */
	private void initComponents() {
		form = new Form<String>("contactForm");
		form.setOutputMarkupId(true);
		
		nameField = new TextField<String>("nameField");
		emailField = new TextField<String>("emailField");
		subjectField = new TextField<String>("subjectField");
		messageField = new TextArea<String>("messageField");
		

		// TODO Auto-generated method stub
		submitButton = new AjaxLink<String>("submitButton") {
			private static final long serialVersionUID = 5400416625335864317L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				// validation
				
				// if valid send to WS then show info zone
				
				// else show error zone
			}
		};

		resetButton = new AjaxLink<String>("resetButton") {
			private static final long serialVersionUID = -8327260512161698549L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				nameField.clearInput();
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
