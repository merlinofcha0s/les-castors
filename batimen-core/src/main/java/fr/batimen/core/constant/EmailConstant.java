package fr.batimen.core.constant;

public class EmailConstant {

    private EmailConstant() {

    }

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
    public static final String TEMPLATE_NOTIFICATION = "notification";

    // Creation annonce tags Email
    public static final String TAG_EMAIL_USERNAME = "username";
    public static final String TAG_EMAIL_TITRE = "titre";
    public static final String TAG_EMAIL_METIER = "metier";
    public static final String TAG_EMAIL_SOUS_CATEGORIE_METIER = "souscategoriemetier";
    public static final String TAG_EMAIL_TYPE_CONTACT = "typecontact";
    public static final String TAG_EMAIL_DELAI_INTERVENTION = "delaiintervention";
    public static final String TAG_EMAIL_ACTIVATION_LINK = "lienactivation";

    // Contact Email tags
    public static final String TAG_EMAIL_CONTACT_NAME = "CONTACTERNAME";
    public static final String TAG_EMAIL_CONTACT_EMAIL = "CONTACTEREMAIL";
    public static final String TAG_EMAIL_CONTACT_SUBJECT = "MESSAGESUBJECT";
    public static final String TAG_EMAIL_CONTACT_MESSAGE = "MESSAGECONTENT";

}
