package fr.batimen.dto.enums;

public enum TypeCompte {

    ADMINISTRATEUR("Administrateur"), PREMIUM_ARTISAN("Artisan premium"), DEFAULT_ARTISAN("Artisan defaut");

    private TypeCompte(String nomCompte) {
        this.nomCompte = nomCompte;
    }

    private String nomCompte;

    public String getNomCompte() {
        return nomCompte;
    }

    @Override
    public String toString() {
        return nomCompte;
    }

}
