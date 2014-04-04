package fr.batimen.dto.enums;

public enum Metier {

	SECURITE("Sécurité"), AMENAGEMENT_ESPACE("Aménagement de l'espace"), ARCHITECTURE("Architecture"), CHARPENTE(
	        "Charpente / Toiture"), CHAUFFAGE("Chauffage"), CLIMATISATION("Climatisation"), CONSTRUCTION("Construction"), CUISINE(
	        "Cuisine"), ELECTRICITE("Electricité"), EXPERTISE("Expertise"), FACADE("Facade"), FENETRE("Fenêtre"), ISOLATION(
	        "Isolation"), JARDIN("Jardin"), MACONNERIE("Maçonnerie"), MENUISERIES("Menuiserie"), PEINTURE("Peinture"), PISCINE(
	        "Piscine"), PLAFOND("Plafond"), PLOMBERIE("Plomberie"), RENOVATION_INTERIEUR("Rénovation intérieure"), REVETEMENT_AU_SOL_INT(
	        "Revêtement au sol (Intérieur)"), REVETEMENT_AU_SOL_EXT("Revêtement au sol (Exterieur)"), SDB_WC("SDB / WC"), TERRASSEMENT_ASSAINISSEMENT(
	        "Terrassement / Assainissement");

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
