package fr.batimen.web.client.master;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Classe abstraite qui doit servir de classe mère à toutes les pages où il est
 * obligatoire de se logger.
 * 
 * @author Casaucau Cyril
 * 
 */
public abstract class SecuredPage extends MasterPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1647831861906535340L;

	public SecuredPage() {
		super();
	}

	public SecuredPage(final PageParameters parameters) {
		this();
	}

	public SecuredPage(String metaDescription, String metaKeywords, String title, boolean isPageWithTitleHeader) {
		super(metaDescription, metaKeywords, title, isPageWithTitleHeader);
		add(new Link<String>("goToHomePage") {
			private static final long serialVersionUID = -6480263784484841724L;

			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}
		});

		add(new Link<String>("logOut") {
			private static final long serialVersionUID = 5031796613478728383L;

			@Override
			public void onClick() {
				AuthenticatedWebSession.get().invalidate();
				setResponsePage(getApplication().getHomePage());
			}
		});
	}

	@Override
	protected void onConfigure() {
		AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
		// if user is not signed in, redirect him to sign in page
		if (!AuthenticatedWebSession.get().isSignedIn()) {
			app.restartResponseAtSignInPage();
		}

	}
}
