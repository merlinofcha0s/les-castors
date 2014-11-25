package fr.batimen.ws.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.EmailException;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.ws.dao.EmailDAO;
import fr.batimen.ws.entity.Annonce;

/**
 * Classe de gestion d'envoi de mail.
 * 
 * @author Casaucau Cyril
 * @author Adnane
 */
@Stateless(name = "EmailService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmailService {

    @Inject
    private EmailDAO emailDAO;


    /**
     * Preparation et envoi d'un mail de confirmation, dans le but d'informer
     * l'utilisateur que l'annonce a correctement été enregistrée.
     * 
     * @param nouvelleAnnonceDTO
     *            L'objet que l'on a recu du frontend
     * @param clientDejaInscrit
     *            informations du client, si celui ci est deja inscrit
     * @return vrai si l'envoi s'est bien passé.
     * @throws EmailException
     * @throws MandrillApiError
     * @throws IOException
     */
    public boolean envoiMailConfirmationCreationAnnonce(Annonce nouvelleAnnonce) throws EmailException,
            MandrillApiError, IOException {

        // On prepare l'entete, on ne mets pas de titre.
        MandrillMessage confirmationAnnonceMessage = emailDAO.prepareEmail(null);

        StringBuilder nomDestinataire = new StringBuilder();
        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();

        getNomDestinataire(nouvelleAnnonce.getDemandeur().getNom(), nouvelleAnnonce.getDemandeur().getPrenom(),
                nouvelleAnnonce.getDemandeur().getLogin(), nomDestinataire);
        recipients.put(nomDestinataire.toString(), nouvelleAnnonce.getDemandeur().getEmail());

        // On charge le contenu
        Map<String, String> templateContent = new HashMap<String, String>();
        templateContent.put(Constant.TAG_EMAIL_USERNAME, nouvelleAnnonce.getDemandeur().getLogin());
        templateContent.put(Constant.TAG_EMAIL_METIER,
                CategorieLoader.getCategorieByCode(nouvelleAnnonce.getCategorieMetier()).getName());
        templateContent.put(Constant.TAG_EMAIL_SOUS_CATEGORIE_METIER, nouvelleAnnonce.getSousCategorieMetier());
        templateContent.put(Constant.TAG_EMAIL_DELAI_INTERVENTION, nouvelleAnnonce.getDelaiIntervention().getType());
        templateContent.put(Constant.TAG_EMAIL_TYPE_CONTACT, nouvelleAnnonce.getTypeContact().getAffichage());

        // On charge les recepteurs
        emailDAO.prepareRecipient(confirmationAnnonceMessage, recipients, true);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(confirmationAnnonceMessage,
                Constant.TEMPLATE_CONFIRMATION_ANNONCE, templateContent);

        return noError;
    }

    /**
     * Envoi d'un mail d'activation pour les nouveaux clients.
     * 
     * @param nouvelleAnnonceDTO
     *            La DTO contenant toutes les infos.
     * @param cleActivation
     *            La clé d'activation généré
     * @param url
     *            L'url du front
     * @return True si l'envoi du mail s'est bien passé
     * @throws EmailException
     * @throws MandrillApiError
     * @throws IOException
     */
    public boolean envoiMailActivationCompte(String nom, String prenom, String login, String email,
            String cleActivation, String url) throws EmailException, MandrillApiError, IOException {

        // On prepare l'entete, on ne mets pas de titre (il est géré par
        // mandrillApp).
        MandrillMessage activationCompteMessage = emailDAO.prepareEmail(null);

        StringBuilder nomDestinataire = new StringBuilder();
        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();

        getNomDestinataire(nom, prenom, login, nomDestinataire);
        recipients.put(nomDestinataire.toString(), email);

        // On charge les recepteurs
        emailDAO.prepareRecipient(activationCompteMessage, recipients, true);

        // On charge le contenu
        Map<String, String> templateContent = new HashMap<String, String>();
        templateContent.put(Constant.TAG_EMAIL_USERNAME, login);

        StringBuilder lienActivation = new StringBuilder(url);
        lienActivation.append(cleActivation);

        // On charge les variables dynamique à remplacer
        List<MergeVar> mergevars = new LinkedList<MergeVar>();
        MergeVar mergeVar = new MergeVar(Constant.TAG_EMAIL_ACTIVATION_LINK, lienActivation.toString());
        mergevars.add(mergeVar);
        activationCompteMessage.setGlobalMergeVars(mergevars);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(activationCompteMessage, Constant.TEMPLATE_ACTIVATION_COMPTE,
                templateContent);

        return noError;
    }

    /**
     * Envoi un email de contact contenant l'information contenue dans le DTO
     * 
     * @param mail
     *            DTO contenant l'information du mail de contact saisie par le
     *            client
     * @return état d'envoi du mail
     */
    public boolean envoiEmailContact(ContactMailDTO mail) throws EmailException, MandrillApiError, IOException {
        // On prepare l'entete, on ne mets pas de titre (il est géré par
        // mandrillApp).
        MandrillMessage contactMessage = emailDAO.prepareEmail(null);
        
        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();

        recipients.put(Constant.EMAIL_FROM_NAME, Constant.EMAIL_CASTOR_CONTACT);

        // On charge les recepteurs
        emailDAO.prepareRecipient(contactMessage, recipients, true);

        // On charge le contenu
        Map<String, String> templateContent = new HashMap<String, String>();
        templateContent.put(Constant.TAG_EMAIL_CONTACT_NAME, mail.getName());
        templateContent.put(Constant.TAG_EMAIL_CONTACT_SUBJECT, mail.getSubject());
        templateContent.put(Constant.TAG_EMAIL_CONTACT_EMAIL, mail.getEmail());
        templateContent.put(Constant.TAG_EMAIL_CONTACT_MESSAGE, mail.getMessage());

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(contactMessage, Constant.TEMPLATE_EMAIL_CONTACT, templateContent);

        return noError;
    }

    /**
     * Envoi un email d'accusé de reception du message de contact
     * 
     * @param mail
     *            DTO contenant l'information du mail de contact saisie par le
     *            client
     * @return état d'envoi du mail
     */
    public boolean envoiEmailAccuseReception(ContactMailDTO mail) throws EmailException, MandrillApiError, IOException {
        // On prepare l'entete, on ne mets pas de titre (il est géré par
        // mandrillApp).
        MandrillMessage accuseReception = emailDAO.prepareEmail(null);

        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();
        recipients.put(mail.getName(), mail.getEmail());

        // On charge les recepteurs
        emailDAO.prepareRecipient(accuseReception, recipients, true);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(accuseReception, Constant.TEMPLATE_ACCUSE_RECEPTION, null);

        return noError;
    }

    /**
     * Util method to generate destinatary name
     * @param nom
     * @param prenom
     * @param login
     * @param nomDestinataire
     */
    private void getNomDestinataire(String nom, String prenom, String login, StringBuilder nomDestinataire) {
        if (!nom.isEmpty() && !prenom.isEmpty()) {
            nomDestinataire.append(nom);
            nomDestinataire.append(" ");
            nomDestinataire.append(prenom);
        } else {
            nomDestinataire.append(login);
        }
    }

}
