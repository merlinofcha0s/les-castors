package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;

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

    public HtmlEmail prepareEmail(String to, String toName, String subject) throws EmailException {

        HtmlEmail email = new HtmlEmail();
        // Information connexion recuperer dans le fichier de properties
        email.setHostName("mail.myserver.com");
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.setSSLCheckServerIdentity(true);

        email.setAuthenticator(new DefaultAuthenticator("username", "password"));
        // Information sur le client
        email.addTo(to, toName);
        email.setFrom(Constant.EMAIL_FROM, Constant.EMAIL_FROM_NAME);
        email.setSubject(subject);

        return email;
    }

    public void prepareContent(HtmlEmail email, String htmlContent, String alternativeContent) throws EmailException {
        // set the html message
        // "<html>The apache logo - <img src=\"cid:" + "\"></html>"
        email.setHtmlMsg(htmlContent);
        // set the alternative message
        // "Your email client does not support HTML messages"
        email.setTextMsg(alternativeContent);
    }

    public void sendEmail(HtmlEmail email) throws EmailException {
        email.send();
    }

    private void getEmailProperties() {

    }
}
