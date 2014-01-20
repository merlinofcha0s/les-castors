package fr.batimen.web.app;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.UrlPathPageParametersEncoder;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import fr.batimen.core.constant.Constant;
import fr.batimen.web.client.panel.Accueil;
import fr.batimen.web.client.panel.Contact;
import fr.batimen.web.client.panel.MonCompte;
import fr.batimen.web.client.panel.QuiSommeNous;
import fr.batimen.web.client.panel.authentification.Authentification;
import fr.batimen.web.client.panel.nouveau.NouveauDevis;
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

		// Config HTML
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getMarkupSettings().setCompressWhitespace(true);
		// Le passer a true en prod
		// TODO : Penser a gérer le param avec maven
		getMarkupSettings().setStripWicketTags(false);

		// Config Jquery
		// Pas de http pour éviter le content mix blocking dans le browser
		// (appel d'une url http dans une page https)
		getJavaScriptLibrarySettings().setJQueryReference(
				new UrlResourceReference(Url.parse("//code.jquery.com/jquery-1.9.1.min.js")));
		// Jquery-ui avec le theme Smoothness
		addResourceReplacement(WiQueryCoreThemeResourceReference.get(), new WiQueryCoreThemeResourceReference(
				"smoothness"));

		// Cfg urls des pages principales
		mountPage(Constant.ACCUEIL_URL, Accueil.class);
		mountPage(Constant.AUTHENTIFICATION_URL, Authentification.class);
		mountPage(Constant.MON_COMPTE_URL, MonCompte.class);
		mountPage(Constant.QUI_SOMMES_NOUS_URL, QuiSommeNous.class);
		mountPage(Constant.CONTACT_URL, Contact.class);

		// Encode la page de cette maniere : nouveaudevis/departement/06
		mount(new MountedMapper("/nouveaudevis", NouveauDevis.class, new UrlPathPageParametersEncoder()));

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
