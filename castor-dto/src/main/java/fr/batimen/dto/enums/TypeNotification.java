package fr.batimen.dto.enums;

public enum TypeNotification {

    INSCRIT_A_ANNONCE("s'est inscrit à votre", "notification_inscription_annonce", TypeCompte.ARTISAN), REPONDU_A_ANNONCE("a répondu à votre", "notification_a_repondu", TypeCompte.ARTISAN), A_CHOISI_ENTREPRISE(
            "a choisi votre", "notification_choisi_annonce", TypeCompte.CLIENT), A_NOTER_ENTREPRISE("a noté(e) votre", "notification_note_entreprise", TypeCompte.CLIENT), A_MODIFIER_ANNONCE("a modifié son", "notification_modification_annonce", TypeCompte.CLIENT);

    private String affichage;
    private String mailTemplateName;
    private TypeCompte parQui;

    TypeNotification(String affichage, String templateName, TypeCompte parQui) {
        this.affichage = affichage;
        this.mailTemplateName = templateName;
        this.parQui = parQui;
    }

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

    public TypeCompte getParQui() {
        return parQui;
    }
}