package fr.castor.dto.enums;

public enum DelaiIntervention {

	LE_PLUS_RAPIDEMENT_POSSIBLE("Le plus rapidement possible"), DEUX_SEMAINES("Deux semaines"), UN_MOIS("Un mois"), TROIS_MOIS(
			"Trois mois"), SIX_MOIS("Six mois"), UN_AN_OU_PLUS("Un an ou plus");

	private DelaiIntervention(String affichage) {
		this.affichage = affichage;
	}

	private String affichage;

	public String getText() {
		return affichage;
	}

	@Override
	public String toString() {
		return affichage;
	}

}
