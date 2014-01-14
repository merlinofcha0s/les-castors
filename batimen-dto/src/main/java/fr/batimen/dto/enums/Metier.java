package fr.batimen.dto.enums;

public enum Metier {

	ELECTRICIEN("Electricien"), PLOMBIER("Plombier"), COUVREUR("Couvreur"), PEINTRE("Peintre");

	private Metier(String nomMetier) {
		this.nomMetier = nomMetier;
	}

	private String nomMetier;

	public String getMetier() {
		return nomMetier;
	}

	@Override
	public String toString() {
		return nomMetier;
	}

}
