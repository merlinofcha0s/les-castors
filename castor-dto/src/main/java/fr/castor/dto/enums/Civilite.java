package fr.castor.dto.enums;

public enum Civilite {

    MONSIEUR("Monsieur"), MADAME("Madame");

    private String affichage;

    private Civilite(String affichage) {
        this.affichage = affichage;
    }

    public String getAffichage() {
        return affichage;
    }

    @Override
    public String toString() {
        return affichage;
    }

}
