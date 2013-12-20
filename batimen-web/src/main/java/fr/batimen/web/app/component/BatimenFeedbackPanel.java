package fr.batimen.web.app.component;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Version custom du feedback panel provenant de wicket Cette version prends en
 * charge les specificités du template html5 dixit
 * 
 * @author Casaucau Cyril
 * 
 */
public class BatimenFeedbackPanel extends FeedbackPanel {

	private static final long serialVersionUID = -919099060805273405L;

	/**
	 * Constructeur par défaut
	 * 
	 * @param id
	 *            l'id html du feedbackPanel
	 */
	public BatimenFeedbackPanel(String id) {
		super(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.markup.html.panel.FeedbackPanel#getCSSClass(org.apache
	 * .wicket.feedback.FeedbackMessage)
	 */
	@Override
	protected String getCSSClass(FeedbackMessage message) {
		String css;

		switch (message.getLevel()) {
		case FeedbackMessage.SUCCESS:
			css = "box_type4";
			break;
		case FeedbackMessage.INFO:
			css = "box_type2";
			break;
		case FeedbackMessage.ERROR:
			css = "box_type6";
			break;
		default:
			css = "box_type2";
			break;
		}

		return css;
	}
}
