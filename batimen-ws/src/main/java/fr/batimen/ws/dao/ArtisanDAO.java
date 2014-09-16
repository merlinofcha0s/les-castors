package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import fr.batimen.ws.entity.Artisan;

/**
 * Classe d'accés aux données pour les artisans
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "ArtisanDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArtisanDAO extends AbstractDAO {

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveArtisan(Artisan nouvelArtisan) {

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Artisan getArtisanByEmail(String email) {
        return null;
    }

}
