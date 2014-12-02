package fr.batimen.dto.enums;

public enum TypeTravaux {

    NEUF("Neuf"), RENOVATION("Rénovation");

    private TypeTravaux(String affichage) {
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
