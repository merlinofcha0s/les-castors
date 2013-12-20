package fr.batimen.web.client.panel;

import fr.batimen.web.client.master.MasterPage;

/**
 * Page qui permettra à l'équipe de se présenter.
 * 
 * @author Casaucau Cyril
 * 
 */
public class QuiSommeNous extends MasterPage {

	private static final long serialVersionUID = -3366422085477423896L;

	public QuiSommeNous() {
		super("Qui sommes nous ?", "", "Qui sommes nous ?");
		super.setActiveMenu(MasterPage.QUI_SOMMES_NOUS);
	}

}
