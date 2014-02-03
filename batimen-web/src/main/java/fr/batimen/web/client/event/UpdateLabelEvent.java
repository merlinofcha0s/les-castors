package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class UpdateLabelEvent extends AbstractEvent {

	public UpdateLabelEvent(AjaxRequestTarget target) {
		super(target);
	}

}
