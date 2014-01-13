package fr.batimen.core.enums;

public enum StatutJuridique {

	SARL("SARL"), SA("SA");

	private StatutJuridique(String nomStatut) {
		this.nomStatut = nomStatut;
	}

	private String nomStatut;

	public String getNomStatut() {
		return nomStatut;
	}

	@Override
	public String toString() {
		return nomStatut;
	}

}
