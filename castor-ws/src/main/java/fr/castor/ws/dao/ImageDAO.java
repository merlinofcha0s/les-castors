package fr.castor.ws.dao;

import fr.castor.core.constant.QueryJPQL;
import fr.castor.ws.entity.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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
            return new ArrayList<>();
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
            return new ArrayList<>();
        }
    }

    public List<Image> getImageBySiret(String siret){
        try {
            TypedQuery<Image> query = entityManager.createNamedQuery(QueryJPQL.IMAGE_BY_SIRET, Image.class);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_SIRET, siret);
            List<Image> images = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb d'images trouvée pour l'annonce: {}", images.size());
            }

            return images;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune images trouvées dans la BDD pour l'entreprise {}", siret, nre);
            }
            return new ArrayList<>();
        }
    }

    public List<Image> getImageBySiretByArtisan(String siret, String loginArtisan){
        try {
            TypedQuery<Image> query = entityManager.createNamedQuery(QueryJPQL.IMAGE_BY_SIRET_BY_CLIENT, Image.class);
            query.setParameter(QueryJPQL.PARAM_ENTREPRISE_SIRET, siret);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, loginArtisan);
            List<Image> images = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb d'images trouvée pour l'annonce: {}", images.size());
            }

            return images;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune images trouvées dans la BDD pour l'entreprise {}", siret, nre);
            }
            return new ArrayList<>();
        }
    }
}