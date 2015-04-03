package fr.batimen.ws.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.utils.RolesUtils;

/**
 * Classe d'accés aux données pour les annonces
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "AnnonceDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnnonceDAO extends AbstractDAO<Annonce> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceDAO.class);

    @Inject
    private RolesUtils rolesUtils;

    /**
     * Récupère les annonces par login de leurs utilsateurs
     * 
     * @param login
     *            le login du client dont on veut recupérer les annonces.
     * @return Liste d'annonces appartenant à l'utilisateur.
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Object[]> getAnnoncesByLoginForMesAnnonces(String login) {

        List<Object[]> listAnnonceByLogin = null;

        try {
            TypedQuery<Object[]> query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_LOGIN_FETCH_ARTISAN,
                    Object[].class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            listAnnonceByLogin = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL OK ");
            }

            return listAnnonceByLogin;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new ArrayList<Object[]>();
        }
    }

    /**
     * Récupère les annonces par login de leurs utilsateurs
     * 
     * @param login
     *            le login du client dont on veut recupérer les annonces.
     * @return Liste d'annonces appartenant à l'utilisateur.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Annonce> getAnnoncesByLogin(String login) {

        List<Annonce> listAnnonceByLogin = null;

        try {
            TypedQuery<Annonce> query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_LOGIN, Annonce.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            listAnnonceByLogin = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL OK ");
            }

            return listAnnonceByLogin;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new ArrayList<Annonce>();
        }
    }

    /**
     * Recupere les annonces par titre, description et login : notament utilisé
     * dans la verification de la duplication.
     * 
     * @param titre
     *            Le titre de l'annonce.
     * @param description
     *            La description de l'annonce.
     * @param login
     *            Le login du cliebnt
     * @return La liste d'annonce qui correspond au titre, description et
     *         utilsateur present en BDD
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Annonce> getAnnonceByTitleAndDescriptionAndLogin(String description, String login) {

        List<Annonce> annoncesBytitreAndDescription = null;

        try {
            TypedQuery<Annonce> query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_TITLE_AND_DESCRIPTION,
                    Annonce.class);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_DESCRIPTION, description);
            query.setParameter(QueryJPQL.CLIENT_LOGIN, login);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Chargement requete JPQL OK ");
            }

            annoncesBytitreAndDescription = query.getResultList();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Récuperation resultat requete JPQL OK ");
            }

            return annoncesBytitreAndDescription;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return new ArrayList<Annonce>();
        }
    }

    /**
     * Sauvegarde d'une annonce lors de son initialisation, check dans la bdd si
     * elle existe déjà pour un utilisateur donné.
     * 
     * @param nouvelleAnnonce
     *            L'annonce a sauvegarder dans la bdd
     * @throws DuplicateEntityException
     *             Exception throw si l'entité existe déjà.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAnnonceFirstTime(Annonce nouvelleAnnonce) throws DuplicateEntityException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut persistence d'une nouvelle annonce......");
        }

        List<Annonce> annoncesDupliquees = getAnnonceByTitleAndDescriptionAndLogin(nouvelleAnnonce.getDescription(),
                nouvelleAnnonce.getDemandeur().getLogin());

        // On check si l'annonce n'existe pas déjà
        if (annoncesDupliquees.isEmpty()) {
            entityManager.persist(nouvelleAnnonce);
        } else {
            StringBuilder sbError = new StringBuilder("Impossible de perister l'annonce: ");
            sbError.append(nouvelleAnnonce.getId());
            sbError.append(" car elle existe déjà");

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(sbError.toString());
            }

            throw new DuplicateEntityException(sbError.toString());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin persistence d'une nouvelle annonce......OK");
        }
    }

    /**
     * Calcul le nb d'annonce qu'un client a postés
     * 
     * @param login
     *            le login du client
     * @return Le nb d'annonce
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long getNbAnnonceByLogin(String login) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut comptage du nb d'annonce");
        }

        TypedQuery<Long> query = entityManager.createNamedQuery(QueryJPQL.NB_ANNONCE_BY_LOGIN, Long.class);
        query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin comptage du nb d'annonce");
        }

        return query.getSingleResult();
    }

    /**
     * Récupère les informations d'une annonce dans le but de les afficher.
     * 
     * @param login
     *            le login du client
     * @return Le nb d'annonce
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Annonce getAnnonceByIDForAffichage(String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de la récuperation de l'annonce par l'id");
        }

        try {
            TypedQuery<Annonce> query = entityManager.createNamedQuery(
                    QueryJPQL.ANNONCE_BY_ID_FETCH_ARTISAN_ENTREPRISE_CLIENT_ADRESSE, Annonce.class);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, id);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fin de la récuperation de l'annonce par l'id");
            }

            return query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }

    /**
     * Charge une annonce grace à son hash ID, ne crée pas de transaction
     * 
     * @param login
     *            le login du client
     * @return Le nb d'annonce
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Annonce getAnnonceByIDWithoutTransaction(String id, boolean isAdmin) {
        return getAnnonceByID(id, isAdmin);
    }

    /**
     * Charge une annonce grace à son hash ID, crée une transaction
     * 
     * @param login
     *            le login du client
     * @return Le nb d'annonce
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Annonce getAnnonceByIDWithTransaction(String id, boolean isAdmin) {
        return getAnnonceByID(id, isAdmin);
    }

    private Annonce getAnnonceByID(String id, boolean isAdmin) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de la récuperation de l'annonce par l'id");
        }

        TypedQuery<Annonce> query = null;
        try {
            if (isAdmin) {
                query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_ID_ADMIN, Annonce.class);
            } else {
                query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_ID, Annonce.class);
            }

            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, id);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fin de la récuperation de l'annonce par l'id");
            }

            return query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }

    /**
     * Mets a jour le nb de consultation d'une annonce dans la BDD
     * 
     * @param nbConsultation
     *            le nb de consultation deja incrémenté
     * @param hashID
     *            L'identifiant unique de l'annonce.
     * @return True si la mise a jour s'est bien passé .
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean updateAnnonceNbConsultationByHashId(Integer nbConsultation, String hashID) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de mise a jour du nb de consultation d'une annonce");
        }

        try {
            Query query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_UPDATE_NB_CONSULTATION);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, hashID);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_NB_CONSULTATION, nbConsultation);
            Integer nbUpdated = query.executeUpdate();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb de row updated: " + nbUpdated);
                LOGGER.debug("Fin de la mise a jour du nb de consultation d'une annonce");
            }

            if (nbUpdated == 0 || nbUpdated != 1) {
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }

    /**
     * Supprime une annonce présente en base de données : <br/>
     * Attention ce n'est pas vraiment une supression, on ne fait que desactiver
     * l'annonce en base de données.
     * 
     * @param demandeAnnonceDTO
     *            Objet qui possede les infos pour verifier que l'utilisateur a
     *            les droits et pour supprimer l'annonce.
     * @return True si la suppression s'est bien passée.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean suppressionAnnonce(DemandeAnnonceDTO demandeAnnonceDTO) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de la suppression de l'annonce : " + demandeAnnonceDTO.getHashID());
            LOGGER.debug("A la demande de : " + demandeAnnonceDTO.getLoginDemandeur());
            LOGGER.debug("Et qui possede le role : " + demandeAnnonceDTO.getTypeCompteDemandeur());
        }
        Query query = null;
        try {
            if (rolesUtils.checkIfClient(demandeAnnonceDTO.getTypeCompteDemandeur())) {
                query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_CLIENT);
                query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, demandeAnnonceDTO.getLoginDemandeur());
            } else if (rolesUtils.checkIfAdmin(demandeAnnonceDTO.getTypeCompteDemandeur())) {
                query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_ADMIN);
            }

            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, demandeAnnonceDTO.getHashID());
            query.setParameter(QueryJPQL.PARAM_ANNONCE_ETAT, EtatAnnonce.SUPPRIMER);

            Integer nbUpdated = query.executeUpdate();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fin de la suppression de l'annonce");
            }

            if (nbUpdated == 0 || nbUpdated != 1) {
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }

    /**
     * Cherche en base de données et desactive toutes les annonces qui sont plus
     * petite que la date passée en paramètre et qui ont un nombre d'artisan
     * inscrit égale a la properties du nb max d'artisan <br/>
     * 
     * @param todayMinusXDays
     *            : la date du jour - le nb de jour present dans le fichier de
     *            properties
     * @param nbMaxArtisan
     *            : Nombre max d'artisan pouvant s'inscrire a une annonce (voir
     *            fichier de properties)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void desactiveAnnoncePerime(Date todayMinusXDays, Integer nbMaxArtisan) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de mise a jour des annonces périmées");
        }

        try {
            Query query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_DESACTIVE_PERIMEE);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_TODAY_MINUS_X_DAYS, todayMinusXDays);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_NB_ARTISAN_MAX, nbMaxArtisan);
            Integer nbUpdated = query.executeUpdate();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("nb de row updated: " + nbUpdated);
                LOGGER.debug("Fin de mise a jour des annonces périmées");
            }

        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
        }
    }

    /**
     * Recherche une annonce par son id et le login du demandeur <br/>
     * 
     * @param hashID
     *            L'id unique de l'annonce
     * @param loginDemandeur
     *            Le login du demandeur (celui qui a poster l'annonce)
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Annonce getAnnonceByIdByLogin(String hashID, String loginDemandeur) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Debut de requete selection annonce par hash et demandeur");
        }

        TypedQuery<Annonce> query = null;
        try {

            query = entityManager.createNamedQuery(QueryJPQL.ANNONCE_BY_HASHID_AND_DEMANDEUR, Annonce.class);

            query.setParameter(QueryJPQL.PARAM_ANNONCE_ID, hashID);
            query.setParameter(QueryJPQL.PARAM_ANNONCE_DEMANDEUR_LOGIN, loginDemandeur);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Fin de requete selection annonce par hash et demandeur");
            }

            return query.getSingleResult();
        } catch (NoResultException nre) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Aucune correspondance trouvées dans la BDD", nre);
            }
            return null;
        }
    }
}
