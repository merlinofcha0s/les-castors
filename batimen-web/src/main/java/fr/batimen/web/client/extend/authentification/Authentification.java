package fr.batimen.web.client.extend.authentification;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.panel.AuthentificationPanel;

/**
 * Panel Wicket servant à authentifier l'utilisateur
 * 
 * @author Casaucau Cyril
 * 
 */
public final class Authentification extends MasterPage {

	private static final long serialVersionUID = 1451418474286472533L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Authentification.class);

	public Authentification() {
		super("Connexion à batimen", "lol", "Connexion à batimen.fr", true, "img/bg_title1.jpg");
		AuthentificationPanel authentificationPanel = new AuthentificationPanel("authentificationPanel");
		this.add(authentificationPanel);
	}

	public Authentification(PageParameters params) {
		this();
	}

}
