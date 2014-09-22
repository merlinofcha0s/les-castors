package fr.batimen.dto.enums;

public enum StatutJuridique {

    SARL("SARL"), SA("SA"), AE("Auto entrepreneur"), SAS("SAS"), EURL("EURL"), SASU("SASU");

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
