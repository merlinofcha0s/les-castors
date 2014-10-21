package fr.batimen.dto.enums;

public enum TypeCompte {

    ADMINISTRATEUR_MANAGER("Administrateur", "admin:manager"), ADMINISTRATEUR_COMMERCIAL("Administrateur",
            "admin:commercial"), ARTISAN_PREMIUM("Artisan premium", "partenaire:premium"), ARTISAN_DEFAULT("Artisan",
            "partenaire:default"), CLIENT("Client", "particulier");

    private TypeCompte(String nomCompte, String roles) {
        this.nomCompte = nomCompte;
        this.roles = roles;
    }

    private String nomCompte;

    private String roles;

    public String getNomCompte() {
        return nomCompte;
    }

    /**
     * @return the roles
     */
    public String getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return nomCompte;
    }

}
