package fr.batimen.test.ws.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.exception.EmailException;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.dao.EmailDAO;

public class EmailDAOTest extends AbstractBatimenWsTest {

    @Inject
    private EmailDAO emailDAO;

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

    @Test
    public void sendEmailTemplateTest() throws MandrillApiError, IOException, EmailException {
        // On prepare l'entete, on ne mets pas de titre.
        MandrillMessage testMessage = emailDAO.prepareEmail(null);

        // On construit les recepteurs
        Map<String, String> recipients = new HashMap<String, String>();
        recipients.put("Admin", "admin@lol.fr");

        // On charge les tags
        Map<String, String> tags = new HashMap<String, String>();
        tags.put(Constant.TAG_EMAIL_USERNAME, "test");
        tags.put(Constant.TAG_EMAIL_METIER, CategorieLoader.getCategorieElectricite().getName());
        tags.put(Constant.TAG_EMAIL_SOUS_CATEGORIE_METIER, CategorieLoader.getCategorieElectricite()
                .getSousCategories().get(0).getName());
        tags.put(Constant.TAG_EMAIL_DELAI_INTERVENTION, DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE.getType());
        tags.put(Constant.TAG_EMAIL_TYPE_CONTACT, TypeContact.EMAIL.getAffichage());

        // On charge les recepteurs
        emailDAO.prepareRecipient(testMessage, recipients, true);

        // On envoi le mail
        boolean noError = emailDAO.sendEmailTemplate(testMessage, Constant.TEMPLATE_CONFIRMATION_ANNONCE, tags);

        Assert.assertTrue(noError);
        Assert.assertNotNull(testMessage);
    }
}
