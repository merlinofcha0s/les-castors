package fr.batimen.ws.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.ws.entity.Notation;

/**
 * Classe d'accés aux données pour les notations.
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "NotationDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotationDAO extends AbstractDAO<Notation> {

    /**
     * Recupere les notations pour un client via ces annonces par ordre
     * decroissant ainsi que le nom complet des entreprises correspondantes.
     * 
     * @param login
     *            le login du client
     * @param limitToThreeNotation
     *            limite la requete aux 3 derniers avis
     * @return les notations et les noms complets associés des entreprises
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Object[]> getNotationByLoginClient(String login, Boolean limitToThreeNotation) {
        List<Object[]> notations = null;

        try {
            TypedQuery<Object[]> query = entityManager.createNamedQuery(QueryJPQL.NOTATION_BY_CLIENT_LOGIN,
                    Object[].class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            if (limitToThreeNotation) {
                query.setMaxResults(3);
            }

            notations = query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<Object[]>();
        }

        return notations;
    }
}