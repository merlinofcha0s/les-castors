package fr.batimen.web.client.panel;

import fr.batimen.web.client.master.MasterPage;

/**
 * Premiere page visible quand l'utilisateur arrivera sur le site, elle definira
 * notre offre
 * 
 * @author Casaucau Cyril
 * 
 */
public class Accueil extends MasterPage {

	private static final long serialVersionUID = -690817359101639588L;

	public Accueil() {
		super("Page d'accueil de batimen.fr", "lol", "Accueil batimen");
	}
}
