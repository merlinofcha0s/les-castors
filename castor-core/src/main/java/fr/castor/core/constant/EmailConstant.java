package fr.castor.core.constant;

/**
 * Classe de constante pour les emails
 * 
 * @author Casaucau Cyril
 * 
 */
public class EmailConstant {

    // Email
    public static final String EMAIL_CASTOR_NOTIF = "notifications@lescastors.fr";
    public static final String EMAIL_CASTOR_CONTACT = "contact@lescastors.fr";
    public static final String EMAIL_FROM_NAME = "Les Castors";
    public static final String EMAIL_SENT = "sent";
    public static final String EMAIL_QUEUED = "queued";
    public static final String EMAIL_REJECTED = "rejected";
    public static final String EMAIL_INVALID = "invalid";
    // Template Email
    public static final String TEMPLATE_CONFIRMATION_ANNONCE = "confirmation_creation_annonce";
    public static final String TEMPLATE_ACTIVATION_COMPTE = "activation_compte";
    public static final String TEMPLATE_EMAIL_CONTACT = "contact_email";
    public static final String TEMPLATE_ACCUSE_RECEPTION = "accuse_reception_contact";
    public static final String TEMPLATE_NOTIFICATION_EQUIPE_NOUVELLE_ANNONCE = "notification_equipe_annonce";
    //Tag Email Généraux
    public static final String TAG_EMAIL_HOME = "urlhome";
    public static final String TAG_EMAIL_ESPACE_CLIENT = "urlespaceclient";
    public static final String TAG_EMAIL_FB = "urlfb";
    public static final String TAG_EMAIL_TWITTER = "urltwitter";
    public static final String TAG_EMAIL_GOOGLEPLUS = "urlgoogleplus";
    // Creation annonce tags Email
    public static final String TAG_EMAIL_USERNAME = "username";
    public static final String TAG_EMAIL_TITRE = "titre";
    public static final String TAG_EMAIL_MOT_CLE = "motcle";
    public static final String TAG_EMAIL_CATEGORIE_METIER = "categoriemetier";
    public static final String TAG_EMAIL_TYPE_CONTACT = "typecontact";
    public static final String TAG_EMAIL_DELAI_INTERVENTION = "delaiintervention";
    public static final String TAG_EMAIL_ACTIVATION_LINK = "lienactivation";
    // Contact Email tags
    public static final String TAG_EMAIL_CONTACT_NAME = "CONTACTERNAME";
    public static final String TAG_EMAIL_CONTACT_EMAIL = "CONTACTEREMAIL";
    public static final String TAG_EMAIL_CONTACT_SUBJECT = "MESSAGESUBJECT";
    public static final String TAG_EMAIL_CONTACT_MESSAGE = "MESSAGECONTENT";
    public static final String TAG_EMAIL_NOTIFICATION_CLIENT = "nomclient";
    public static final String TAG_EMAIL_NOTIFICATION_ARTISAN = "nomartisan";
    public static final String TAG_EMAIL_NOTIFICATION_NOM_ENTREPRISE = "nomentreprise";
    public static final String TAG_EMAIL_NOTIFICATION_URL_FRONT = "urlCastor";
    //Notification Equipe Annonce tags
    public static final String TAG_EMAIL_NOTIFICATION_EQUIPE_NOUVELLE_ANNONCE_ID = "IDANNONCE";

    private EmailConstant() {

    }
}