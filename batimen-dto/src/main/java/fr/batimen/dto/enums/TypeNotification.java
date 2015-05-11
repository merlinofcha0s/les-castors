package fr.batimen.dto.enums;

public enum TypeNotification {

    INSCRIT_A_ANNONCE("s'est inscrit à votre", "notification_inscription_annonce"), REPONDU_A_ANNONCE("a répondu à votre", "notification_a_repondu"), A_CHOISI_ENTREPRISE(
            "a choisi votre", "notification_choisi_annonce"), A_NOTER_ENTREPRISE("a noté(e) votre", "notification_note_entreprise"), A_MODIFIER_ANNONCE("a modifié son", "notification_modification_annonce");

    private TypeNotification(String affichage, String templateName) {
        this.affichage = affichage;
        this.mailTemplateName = templateName;
    }

    private String affichage;

    private String mailTemplateName;

    public String getAffichage() {
        return affichage;
    }

    @Override
    public String toString() {
        return affichage;
    }

    public String getEmailTemplate(){
        return mailTemplateName;
    }

}
