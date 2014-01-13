package fr.batimen.dto.enums;

public enum EtatAnnonce {

	ACTIVE("Active"), DESACTIVE("Désactive"), A_NOTER("A Noter");

	private EtatAnnonce(String affichage) {
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
