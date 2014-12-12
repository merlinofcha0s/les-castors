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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Notation> getNotationByLoginClient(String login) {
        List<Notation> notations = null;

        try {
            TypedQuery<Notation> query = entityManager.createNamedQuery(QueryJPQL.NOTATION_BY_CLIENT_LOGIN,
                    Notation.class);
            query.setParameter(QueryJPQL.PARAM_CLIENT_LOGIN, login);

            notations = query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<Notation>();
        }

        return notations;
    }
}
