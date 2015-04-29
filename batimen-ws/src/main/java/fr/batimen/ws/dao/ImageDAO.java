package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.ws.entity.Image;

import java.util.List;

/**
 * Classe d'accés aux données pour les images.
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "ImageDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImageDAO extends AbstractDAO<Image> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotationDAO.class);

    public List<Image> getImageByHashIDByClient(String hashID, String login){

        try {
            TypedQuery<Image> query = entityManager.createNamedQuery(QueryJPQL.IMAGE_BY_HASH_ID_AND_LOGIN_CLIENT, Image.class);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, hashID);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);
            List<Image> images = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb d'images trouvée pour l'annonce: {}", images.size());
            }

            return images;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune images trouvées dans la BDD pour l'annonce {}", hashID, nre);
            }
            return null;
        }
    }

    public List<Image> getImageByHashID(String hashID){

        try {
            TypedQuery<Image> query = entityManager.createNamedQuery(QueryJPQL.IMAGE_BY_HASH_ID, Image.class);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, hashID);
            List<Image> images = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb d'images trouvée pour l'annonce: {}", images.size());
            }

            return images;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune images trouvées dans la BDD pour l'annonce {}", hashID, nre);
            }
            return null;
        }
    }
}