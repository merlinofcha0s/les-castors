package fr.batimen.web.app;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import fr.batimen.web.client.panel.Accueil;
import fr.batimen.web.client.panel.Contact;
import fr.batimen.web.client.panel.MonCompte;
import fr.batimen.web.client.panel.QuiSommeNous;
import fr.batimen.web.client.panel.authentification.Authentification;
import fr.batimen.web.client.session.BatimenSession;

/**
 * Classe principale de l'application
 * 
 * @author Casaucau Cyril
 */
public class BatimenApplication extends AuthenticatedWebApplication {
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return Accueil.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getMarkupSettings().setCompressWhitespace(true);

		// Cfg urls des pages principales
		mountPage("/accueil", Accueil.class);
		mountPage("/connexion", Authentification.class);
		mountPage("/moncompte", MonCompte.class);
		mountPage("/quisommesnous", QuiSommeNous.class);
		mountPage("/contact", Contact.class);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return Authentification.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return BatimenSession.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.authroles.authentication.AuthenticatedWebApplication
	 * #newSession(org.apache.wicket.request.Request,
	 * org.apache.wicket.request.Response)
	 */
	@Override
	public Session newSession(Request request, Response response) {
		return new BatimenSession(request);
	}

}
