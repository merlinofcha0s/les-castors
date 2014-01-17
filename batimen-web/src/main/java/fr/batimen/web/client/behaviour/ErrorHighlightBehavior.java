package fr.batimen.web.client.behaviour;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

public class ErrorHighlightBehavior extends Behavior {

	private static final long serialVersionUID = -7426851486352182384L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.behavior.Behavior#onComponentTag(org.apache.wicket.
	 * Component, org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		FormComponent<?> fc = (FormComponent<?>) component;
		if (!fc.isValid()) {
			tag.put("class", "error");
		}
	}

}
