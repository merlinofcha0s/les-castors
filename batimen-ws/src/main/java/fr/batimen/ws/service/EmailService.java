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
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.ws.dao.EmailDAO;
import fr.batimen.ws.entity.Client;

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
    public boolean envoiMailConfirmationCreationAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO, Client clientDejaInscrit)
            throws EmailException, MandrillApiError, IOException {

        // On prepare l'entete, on ne mets pas de titre.
        MandrillMessage confirmationAnnonceMessage = emailDAO.prepareEmail(null);

        StringBuilder nomDestinataire = new StringBuilder();
        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();

        getNomDestinataire(clientDejaInscrit.getNom(), clientDejaInscrit.getPrenom(), clientDejaInscrit.getLogin(),
                nomDestinataire);
        recipients.put(nomDestinataire.toString(), clientDejaInscrit.getEmail());

        // On charge le contenu
        Map<String, String> templateContent = new HashMap<String, String>();
        templateContent.put(Constant.TAG_EMAIL_USERNAME, nouvelleAnnonceDTO.getClient().getLogin());
        templateContent.put(Constant.TAG_EMAIL_METIER, nouvelleAnnonceDTO.getCategorieMetier().getName());
        templateContent.put(Constant.TAG_EMAIL_SOUS_CATEGORIE_METIER, nouvelleAnnonceDTO.getSousCategorie().getName());
        templateContent.put(Constant.TAG_EMAIL_DELAI_INTERVENTION, nouvelleAnnonceDTO.getDelaiIntervention().getType());
        templateContent.put(Constant.TAG_EMAIL_TYPE_CONTACT, nouvelleAnnonceDTO.getTypeContact().getAffichage());

        // On charge les recepteurs
        emailDAO.prepareRecipient(confirmationAnnonceMessage, recipients, true);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(confirmationAnnonceMessage,
                Constant.TEMPLATE_CONFIRMATION_ANNONCE, templateContent);

        return noError;
    }

    public boolean envoiMailActivationCompte(CreationAnnonceDTO nouvelleAnnonceDTO, String url) throws EmailException,
            MandrillApiError, IOException {

        // On prepare l'entete, on ne mets pas de titre.
        MandrillMessage activationCompteMessage = emailDAO.prepareEmail(null);

        StringBuilder nomDestinataire = new StringBuilder();
        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();

        getNomDestinataire(nouvelleAnnonceDTO.getClient().getNom(), nouvelleAnnonceDTO.getClient().getPrenom(),
                nouvelleAnnonceDTO.getClient().getLogin(), nomDestinataire);
        recipients.put(nomDestinataire.toString(), nouvelleAnnonceDTO.getClient().getEmail());

        // On charge les recepteurs
        emailDAO.prepareRecipient(activationCompteMessage, recipients, true);

        // On génére le lien
        String lienActivation = emailDAO.generationLienActivation(nouvelleAnnonceDTO.getClient().getLogin(),
                nouvelleAnnonceDTO.getClient().getPassword(), url);

        // On charge le contenu
        Map<String, String> templateContent = new HashMap<String, String>();
        templateContent.put(Constant.TAG_EMAIL_USERNAME, nouvelleAnnonceDTO.getClient().getLogin());

        // On charge les variables dynamique à remplacer
        List<MergeVar> mergevars = new LinkedList<MergeVar>();
        MergeVar mergeVar = new MergeVar(Constant.TAG_EMAIL_ACTIVATION_LINK, lienActivation);
        mergevars.add(mergeVar);
        activationCompteMessage.setGlobalMergeVars(mergevars);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(activationCompteMessage, Constant.TEMPLATE_ACTIVATION_COMPTE,
                templateContent);

        return noError;
    }

    private void getNomDestinataire(String nom, String prenom, String login, StringBuilder nomDestinataire) {
        if (!nom.isEmpty() && !prenom.isEmpty()) {
            nomDestinataire.append(nom);
            nomDestinataire.append(" ");
            nomDestinataire.append(prenom);
        } else {
            nomDestinataire = new StringBuilder(login);
        }
    }

}
