package fr.batimen.web.client.behaviour;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Permet de signifier à l'utilisateur que le champ est en erreur (le colori en
 * rouge)
 * 
 * @author Casaucau Cyril
 * 
 */
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
			String baseCss = tag.getAttribute("class")==null? "" : tag.getAttribute("class");
			StringBuilder sb = new StringBuilder(baseCss);
			// class pour etat erreur input
			sb.append(" error");
			tag.put("class", sb.toString());
		}
	}

}
