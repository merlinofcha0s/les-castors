package fr.batimen.dto.enums;

public enum Civilite {

	MONSIEUR("Monsieur"), MADAME("Madame"), MADEMOISELLE("Mademoiselle");

	private Civilite(String affichage) {
		this.affichage = affichage;
	}

	private String affichage;

	public String getType() {
		return affichage;
	}

	@Override
	public String toString() {
		return affichage;
	}

}
