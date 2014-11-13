package fr.batimen.dto.enums;

public enum StatutNotification {

    VU("Vu"), PAS_VUE("Pas vue");

    private StatutNotification(String nomStatut) {
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
