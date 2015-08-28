package fr.batimen.test.ws.helper;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.ContactMailDTO;
import fr.batimen.dto.MotCleDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.constant.Categorie;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;

public class DataHelper {

    public static CreationAnnonceDTO getAnnonceDTOData() {

        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        // Infos Client
        creationAnnonceDTO.getClient().setNom("Du Pebron");
        creationAnnonceDTO.getClient().setPrenom("Johnny");
        creationAnnonceDTO.setAdresse("106 chemin du pébron");
        creationAnnonceDTO.setComplementAdresse("Res des pébrons");
        creationAnnonceDTO.setCodePostal("06700");
        creationAnnonceDTO.setIsSignedUp(false);
        creationAnnonceDTO.getClient().setEmail("pebron@delapebronne.com");
        creationAnnonceDTO.getClient().setLogin("johnny06");
        creationAnnonceDTO.getClient().setNumeroTel("0615458596");
        creationAnnonceDTO.getClient().setPassword(
                "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setTypeCompte(TypeCompte.CLIENT);

        creationAnnonceDTO.getClient().getPermissions().add(permissionDTO);

        // Infos Qualification Annonce
        creationAnnonceDTO.setDescription("Peinture d'un mur");

        creationAnnonceDTO.setDelaiIntervention(DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE);
        creationAnnonceDTO.setDepartement(06);
        creationAnnonceDTO.setTypeContact(TypeContact.EMAIL);
        creationAnnonceDTO.setVille("Nice");
        creationAnnonceDTO.setTypeTravaux(TypeTravaux.NEUF);

        MotCleDTO categorieDTO = new MotCleDTO();
        categorieDTO.setMotCle("Salles de bains");

        CategorieMetierDTO categorieMetierElectriciteDTO = new CategorieMetierDTO();
        categorieMetierElectriciteDTO.setCategorieMetier(Categorie.ELECTRICITE_CODE);
        categorieDTO.getCategoriesMetier().add(categorieMetierElectriciteDTO);

        CategorieMetierDTO categorieMetierDecorationMaconnerieDTO = new CategorieMetierDTO();
        categorieMetierDecorationMaconnerieDTO.setCategorieMetier(Categorie.DECORATION_MACONNERIE_CODE);
        categorieDTO.getCategoriesMetier().add(categorieMetierDecorationMaconnerieDTO);

        creationAnnonceDTO.getMotCles().add(categorieDTO);

        return creationAnnonceDTO;
    }

    /**
     * Genere un mail de contact de test
     * 
     * @return
     */
    public static ContactMailDTO getContactMailData() {
        ContactMailDTO res = new ContactMailDTO();
        res.setName("dutest michel");
        res.setEmail("pebron@delapebronne.com");
        res.setSubject("Do you wanna know?");
        res.setMessage("No forget about it, it's ok");
        return res;
    }

}
