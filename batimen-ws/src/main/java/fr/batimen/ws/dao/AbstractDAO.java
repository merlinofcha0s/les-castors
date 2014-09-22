package fr.batimen.ws.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe abstraite pour l'ensemble des classes DAO
 * 
 * @author Casaucau Cyril
 * 
 */
public class AbstractDAO {

    @PersistenceContext
    protected EntityManager entityManager;

}
