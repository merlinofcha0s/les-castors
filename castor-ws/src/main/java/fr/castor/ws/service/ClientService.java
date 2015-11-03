package fr.castor.ws.service;

import java.io.IOException;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import fr.castor.ws.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.core.exception.BackendException;
import fr.castor.core.exception.EmailException;
import fr.castor.dto.enums.EtatAnnonce;
import fr.castor.ws.dao.ClientDAO;
import fr.castor.ws.entity.Annonce;

/**
 * Classe de gestion d'envoi de mail.
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "ClientService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private EmailService emailService;

    /**
     * Methode d'activation d'un compte client.
     * 
     * @param cleActivation
     *            la clé permettant de retrouver le client et d'activer son
     *            compte.
     * @return Le resultat de l'activation.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer activateAccount(Client clientByKey) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Activation du compte client : " + clientByKey.getLogin());
        }

        if (!clientByKey.getLogin().isEmpty()) {
            if (clientByKey.getIsActive().equals(Boolean.FALSE)) {
                clientByKey.setIsActive(Boolean.TRUE);

                // On active son annonce.
                Set<Annonce> annonces = clientByKey.getDevisDemandes();
                if (!annonces.isEmpty()) {

                    for (Annonce annonceToActivate : annonces) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Activation de son annonce : " + annonceToActivate.getId());
                        }
                        annonceToActivate.setEtatAnnonce(EtatAnnonce.ACTIVE);
                        try {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Envoi du mail de confirmation de l'annonce: " + annonceToActivate.getId());
                            }
                            emailService.envoiMailConfirmationCreationAnnonce(annonceToActivate);
                        } catch (EmailException | MandrillApiError | IOException e) {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("Problème d'envoi d'email d'activation d'annonce", e);
                            }
                        }
                    }
                }
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("MAJ des infos du client dans la BDD");
                    }
                    clientDAO.saveClient(clientByKey);
                } catch (BackendException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Impossible de mettre à jour le client apres activation de son compte", e);
                    }
                    return CodeRetourService.RETOUR_KO;
                }
            } else {
                return CodeRetourService.ANNONCE_RETOUR_DEJA_ACTIF;
            }

        } else {
            return CodeRetourService.ANNONCE_RETOUR_COMPTE_INEXISTANT;
        }

        return CodeRetourService.RETOUR_OK;
    }
}
