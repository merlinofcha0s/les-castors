package fr.batimen.web.client.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class LoginEvent extends AbstractEvent {

	public LoginEvent(AjaxRequestTarget target) {
		super(target);
	}

}
