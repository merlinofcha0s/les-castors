package fr.batimen.dto.enums;

public enum TypeNotification {

    INSCRIT_A_ANNONCE("s'est inscrit à votre"), REPONDU_A_ANNONCE("a répondu à votre"), A_CHOISI_ENTREPRISE(
            "a choisi votre");

    private TypeNotification(String affichage) {
        this.affichage = affichage;
    }

    private String affichage;

    public String getAffichage() {
        return affichage;
    }

    @Override
    public String toString() {
        return affichage;
    }

}
