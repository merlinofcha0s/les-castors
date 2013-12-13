package fr.batimen.web.client.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.web.client.master.SecuredPage;

public final class HomePage extends SecuredPage {

	private static final long serialVersionUID = 1L;

	public HomePage() {
		super("Page accueil de batimen", "lol", "Bienvenue sur batimen.fr");
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
	}

	public HomePage(final PageParameters parameters) {
		this();
	}
}
