package fr.batimen.dto.enums;

public enum EtatAnnonce {

    ACTIVE("Active"), DESACTIVE("Désactivée"), A_NOTER("A noter"), EN_ATTENTE("En attente"), SUPPRIMER("Supprimer"), QUOTA_MAX_ATTEINT(
            "Quota devis atteint"), TERMINER("Terminer");

    private EtatAnnonce(String affichage) {
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
