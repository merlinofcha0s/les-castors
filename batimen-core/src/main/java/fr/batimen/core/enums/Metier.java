package fr.batimen.core.enums;

public enum Metier {

	ELECTRICIEN("Electricien"), PLOMBIER("Plombier"), COUVREUR("Couvreur");

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
