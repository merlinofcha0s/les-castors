package fr.batimen.test.ws.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;

import fr.batimen.core.exception.EmailException;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.test.ws.DataHelper;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.dao.EmailDAO;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.service.EmailService;

public class EmailServiceTest extends AbstractBatimenWsTest {

    @Inject
    private EmailDAO emailDAO;

    @Inject
    private EmailService emailService;

    @Inject
    private ClientDAO clientDAO;

    @Test
    public void sendEmailTest() throws MandrillApiError, IOException, EmailException {
        // On prepare l'entete
        MandrillMessage testMessage = emailDAO.prepareEmail("Email Testing");
        // On prepare le contenus
        emailDAO.prepareContent(testMessage, "<h1>Hi pal!</h2><br />Really, I'm just saying hi!");

        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();
        recipients.put("Admin", "admin@lol.fr");

        // On charge les recepteurs
        emailDAO.prepareRecipient(testMessage, recipients, true);

        // On envoi le mail
        boolean noError = emailDAO.sendEmail(testMessage);

        Assert.assertTrue(noError);
        Assert.assertNotNull(testMessage);
    }

    /**
     * Cas de test : Test qui envoi un mail de confirmation au client qui vient
     * de creer une demande de devis
     * 
     * @throws MandrillApiError
     * @throws IOException
     * @throws EmailException
     */
    @Test
    @UsingDataSet("datasets/in/client_creation_annonce.yml")
    public void sendConfirmationCreationAnnonce() throws MandrillApiError, IOException, EmailException {
        Client johnny = clientDAO.getClientByLoginName("johnny06");
        boolean noError = emailService.envoiMailConfirmationCreationAnnonce(DataHelper.getAnnonceData(), johnny);

        Assert.assertNotNull(johnny);
        Assert.assertTrue(noError);
    }
}
