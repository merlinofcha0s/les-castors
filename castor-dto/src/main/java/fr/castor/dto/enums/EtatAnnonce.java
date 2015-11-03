package fr.castor.dto.enums;

public enum EtatAnnonce {

    ACTIVE("Active", "30%"), DESACTIVE("Désactivée", "0%"), DONNER_AVIS("Donner un avis", "60%"), EN_ATTENTE("En attente", "15%"), SUPPRIMER("Supprimer", "75%"), QUOTA_MAX_ATTEINT(
            "Quota devis atteint", "45%"), TERMINER("Terminer", "100%");

    EtatAnnonce(String affichage, String percentage) {
        this.affichage = affichage;
        this.percentage = percentage;
    }

    private String affichage;

    private String percentage;

    public String getType() {
        return affichage;
    }

    public String getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return affichage;
    }

}
