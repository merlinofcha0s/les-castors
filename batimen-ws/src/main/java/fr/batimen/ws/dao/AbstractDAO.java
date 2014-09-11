package fr.batimen.ws.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractDAO {

    @PersistenceContext
    protected EntityManager entityManager;

}
