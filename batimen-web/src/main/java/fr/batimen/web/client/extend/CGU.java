package fr.batimen.web.client.extend;

import fr.batimen.web.client.master.MasterPage;

/**
 * Page de description de nos conditions générales de vente et d'utilisation.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CGU extends MasterPage {

	private static final long serialVersionUID = -4859858357188558602L;

	public CGU() {
		super("Conditions générales d'utilisation de batimen.fr", "", "Nos conditions générales d'utilisation", true,
		        "img/bg_title1.jpg");
		super.setActiveMenu(MasterPage.NONE);
	}

}
