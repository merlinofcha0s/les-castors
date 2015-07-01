package fr.batimen.ws.service;

import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.ws.entity.CategorieMetier;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.entity.Image;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Classe de service pour les entreprises.
 *
 * @author Casaucau Cyril
 *
 */
@Stateless(name = "EntrepriseService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EntrepriseService {


    public EntrepriseDTO rempliEntrepriseInformation(Entreprise entreprise){
        if(entreprise.getId() != null){
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            EntrepriseDTO entrepriseDTO = mapper.map(entreprise, EntrepriseDTO.class);
            entrepriseDTO.setIsVerified(entreprise.getIsVerifier());
            entrepriseDTO.setSpecialite(entreprise.getSpecialite());

            for(CategorieMetier categorieMetier : entreprise.getCategoriesMetier()){
                entrepriseDTO.getCategoriesMetier().add(CategorieLoader.getCategorieByCode(categorieMetier.getCategorieMetier()));
            }

            for(Image image : entreprise.getImagesChantierTemoin()){
                entrepriseDTO.getPhotosChantiersTemoins().add(mapper.map(image, ImageDTO.class));
            }

            AdresseDTO adresseEntreprise = mapper.map(entreprise.getAdresse(), AdresseDTO.class);
            entrepriseDTO.setAdresseEntreprise(adresseEntreprise);
            return entrepriseDTO;
        }else{
            return new EntrepriseDTO();
        }
    }
}