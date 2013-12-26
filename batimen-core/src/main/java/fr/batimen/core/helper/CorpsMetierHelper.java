package fr.batimen.core.helper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.batimen.core.metier.CorpsMetier;
import fr.batimen.core.metier.SousCorpsMetier;

/**
 * Sert a initialiser les corps de metier dans le but de les mettre au demarrage
 * de l'appli
 * 
 * @author Casaucau Cyril
 * 
 */
public class CorpsMetierHelper {

	public static List<CorpsMetier> init() {

		List<CorpsMetier> listCorpsMetier = new LinkedList<CorpsMetier>();

		// Plomberie
		CorpsMetier plomberie = new CorpsMetier("Plomberie");
		listCorpsMetier.add(plomberie);

		SousCorpsMetier sousPlomberie = new SousCorpsMetier("Sous Plomberie");
		plomberie.getSousCorpsMetier().add(sousPlomberie);

		SousCorpsMetier sousPlomberie2 = new SousCorpsMetier("Sous Plomberie2");
		plomberie.getSousCorpsMetier().add(sousPlomberie2);

		// Electricité
		CorpsMetier electricite = new CorpsMetier("Electricité");
		listCorpsMetier.add(electricite);

		SousCorpsMetier sousElectricite = new SousCorpsMetier("Sous Electricité");
		electricite.getSousCorpsMetier().add(sousElectricite);

		SousCorpsMetier sousElectricite2 = new SousCorpsMetier("Sous Electricité2");
		electricite.getSousCorpsMetier().add(sousElectricite2);

		// Maconnerie
		CorpsMetier maconnerie = new CorpsMetier("Maconnerie");
		listCorpsMetier.add(maconnerie);

		return Collections.unmodifiableList(listCorpsMetier);
	}
}
