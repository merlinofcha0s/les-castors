package fr.castor.ws.dao;

import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.EntrepriseDTO;
import fr.castor.ws.entity.CategorieMetier;
import fr.castor.ws.entity.Entreprise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;

/**
 * Classe d'accés aux données des catégories métier
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "CategorieMetierDAO")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CategorieMetierDAO extends AbstractDAO<CategorieMetier> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorieMetierDAO.class);

    /**
     * Enregistre les catégories metiers des entreprises dans la base de
     * données.
     *
     * @param nouvelleCategorieMetier
     */
    public void persistCategorieMetier(CategorieMetier nouvelleCategorieMetier) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Persistence d'une nouvelle catégorie");
        }
        entityManager.persist(nouvelleCategorieMetier);
    }

    /**
     * Mise à jour des catégories de l'artisans
     *
     * Un peu barbare, on efface toutes les catégories précédentes.
     *
     * @param entrepriseDTO L'objet provenant du frontend
     * @param entrepriseAMettreAJour L'entité JPA
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void updateCategorieMetier(EntrepriseDTO entrepriseDTO, Entreprise entrepriseAMettreAJour) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Mise à jour des catégories");
            LOGGER.debug("On efface les catégories présentes:");
        }
        for (CategorieMetier categorieMetier : entrepriseAMettreAJour.getCategoriesMetier()) {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(categorieMetier.toString());
            }
            delete(categorieMetier);
        }
        entrepriseAMettreAJour.getCategoriesMetier().clear();
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Création des nouvelles catégories :");
        }
        for (CategorieMetierDTO categorieMetierDTO : entrepriseDTO.getCategoriesMetier()) {
            CategorieMetier categorieMetier = new CategorieMetier();
            categorieMetier.setCategorieMetier(categorieMetierDTO.getCategorieMetier());
            categorieMetier.setEntreprise(entrepriseAMettreAJour);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(categorieMetier.toString());
            }
            createMandatory(categorieMetier);
        }
    }
}
