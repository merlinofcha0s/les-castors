package fr.batimen.ws.dao;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.EmailException;

/**
 * Classe de formatage d'email
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "EmailDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmailDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailDAO.class);

    private MandrillApi mandrillApi;
    private String apiKey;
    private boolean emailActive;

    public EmailDAO() {
        getMessageProperties();
        mandrillApi = new MandrillApi(apiKey);
    }

    public MandrillMessage prepareEmail(String subject) {

        // create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setAutoText(true);
        message.setFromEmail(Constant.EMAIL_FROM);
        message.setFromName(Constant.EMAIL_FROM_NAME);

        return message;
    }

    public void prepareRecipient(MandrillMessage message, Map<String, String> recipients, boolean preserveRecipients) {

        List<Recipient> to = new LinkedList<Recipient>();

        for (Entry<String, String> recipient : recipients.entrySet()) {
            Recipient recipientFormatted = new Recipient();
            recipientFormatted.setName(recipient.getKey());
            recipientFormatted.setEmail(recipient.getValue());
            to.add(recipientFormatted);
        }

        message.setTo(to);
        message.setPreserveRecipients(preserveRecipients);
    }

    public void prepareContent(MandrillMessage message, String htmlContent) {
        // set the html message
        // "<html>The apache logo - <img src=\"cid:" + "\"></html>"
        message.setHtml(htmlContent);
    }

    public boolean sendEmail(MandrillMessage message) throws MandrillApiError, IOException, EmailException {
        // TODO Remplacer par sendTemplate une fois que le template aura ete
        // choisi
        if (emailActive) {
            MandrillMessageStatus[] messagesStatus = mandrillApi.messages().send(message, false);
            return checkErrorOnSentEmail(messagesStatus);
        } else {
            return true;
        }

    }

    private boolean checkErrorOnSentEmail(MandrillMessageStatus[] messagesStatus) throws EmailException {

        boolean noError = true;
        // On verifie que tout s'est bien passé.
        for (int i = 0; i < messagesStatus.length; i++) {
            if (!Constant.EMAIL_SENT.equals(messagesStatus[i].getStatus())) {
                noError = false;
                throw new EmailException("Certain mails n'ont pas été distribués correctement",
                        messagesStatus[i].getEmail(), messagesStatus[i].getStatus());
            }
        }

        return noError;
    }

    private void getMessageProperties() {
        Properties appProperties = new Properties();
        try {
            appProperties.load(getClass().getClassLoader().getResourceAsStream("email.properties"));
            apiKey = appProperties.getProperty("mandrill.api.key");
            emailActive = Boolean.valueOf(appProperties.getProperty("email.active"));
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur de récupération des properties des mails", e);
            }
        }
    }
}
