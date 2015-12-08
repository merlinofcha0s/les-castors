package fr.castor.web.client.component;

import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.client.extend.nouveau.communs.JSCommun;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Version custom du feedback panel provenant de wicket Cette version prends en
 * charge les specificités du template html5 dixit
 *
 * @author Casaucau Cyril
 */
public class CastorFeedbackPanel extends FeedbackPanel {

    private static final long serialVersionUID = -919099060805273405L;

    /**
     * Constructeur par défaut
     *
     * @param id l'id html du feedbackPanel
     */
    public CastorFeedbackPanel(String id) {
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

    /**
     * Envoi un message à l'utilisateur via le feedback panel
     *
     * @param message      Le message à envoyer
     * @param messageLevel Le niveau du message ERROR, INFO, SUCCESS
     */
    public void sendMessage(String message, FeedbackMessageLevel messageLevel) {
        chooseLevel(message, messageLevel);
    }

    /**-
     * Envoi un message à l'utilisateur via le feedback panel et scroll automatiquement en haut de la page
     * <p>
     * Comme ca on est sur que l'utilisateur voit le message.
     *
     * @param message      Le message à envoyer
     * @param messageLevel Le niveau du message ERROR, INFO, SUCCESS
     * @param target       La target Ajax
     */
    public void sendMessageAndGoToTop(String message, FeedbackMessageLevel messageLevel, AjaxRequestTarget target) {
        chooseLevel(message, messageLevel);
        JSCommun.scrollToTop(target);
    }

    private void chooseLevel(String message, FeedbackMessageLevel messageLevel) {
        switch (messageLevel) {
            case ERROR:
                this.error(message);
                break;
            case INFO:
                this.info(message);
                break;
            case SUCCESS:
                this.success(message);
                break;
            default:
                this.error(message);
                break;
        }
    }
}