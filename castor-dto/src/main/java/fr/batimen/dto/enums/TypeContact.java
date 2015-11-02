package fr.batimen.dto.enums;

public enum TypeContact {

	EMAIL("Email"), TELEPHONE("Téléphone");

	private TypeContact(String affichage) {
		this.affichage = affichage;
	}

	private String affichage;

	public String getAffichage() {
		return affichage;
	}

	@Override
	public String toString() {
		return affichage;
	}

}
