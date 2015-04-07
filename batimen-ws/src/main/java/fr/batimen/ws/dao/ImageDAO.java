package fr.batimen.ws.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.ws.entity.Image;

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

}
