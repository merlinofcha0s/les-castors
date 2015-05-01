package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Event qui effacera tout ce qui se trouve dans le feedback panel
 *
 * @author Casaucau Cyril
 */
public class ClearFeedbackPanelEvent extends AbstractEvent {

    public ClearFeedbackPanelEvent(AjaxRequestTarget target) {
        super(target);
    }
}
