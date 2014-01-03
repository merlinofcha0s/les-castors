package fr.batimen.web.client.panel;

import fr.batimen.web.client.master.MasterPage;

/**
 * Page de contact qui permettra aux utilisateur de contacter l'équipe
 * 
 * @author Casaucau Cyril
 * 
 */
public class Contact extends MasterPage {

	private static final long serialVersionUID = -2549295715502248172L;

	public Contact() {
		super("Contact de batimen.fr", "", "Nous contacter", true, "img/bg_title1.jpg");
		super.setActiveMenu(MasterPage.CONTACT);
	}
}
