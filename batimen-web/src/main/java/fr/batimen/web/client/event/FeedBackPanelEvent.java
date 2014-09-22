package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class FeedBackPanelEvent extends AbstractEvent {

    private String message;

    public FeedBackPanelEvent(AjaxRequestTarget target) {
        super(target);
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message == null ? "" : message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
