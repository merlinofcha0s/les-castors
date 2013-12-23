package fr.batimen.web.client.panel.nouveau;

import fr.batimen.web.client.component.MapFrance;
import fr.batimen.web.client.master.MasterPage;

/**
 * Permet la création de nouveau devis par un client demandeur de travaux.
 * 
 * @author Casaucau Cyril
 * 
 */
public class NouveauDevis extends MasterPage {

	private static final long serialVersionUID = -7595966450246951918L;

	public NouveauDevis() {
		super("Nouveau devis", "devis batiment renovation", "Nouveau devis", true);
		MapFrance carteFrance = new MapFrance("mapFrance");
		this.add(carteFrance);
	}

}
