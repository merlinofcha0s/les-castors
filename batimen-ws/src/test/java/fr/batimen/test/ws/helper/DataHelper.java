package fr.batimen.test.ws.helper;

import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.helper.CategorieLoader;

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
        creationAnnonceDTO.getClient().setTypeCompte(TypeCompte.CLIENT);

        // Infos Qualification Annonce
        creationAnnonceDTO.setDescription("Peinture d'un mur");

        creationAnnonceDTO.setDelaiIntervention(DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE);
        creationAnnonceDTO.setDepartement(06);
        creationAnnonceDTO.setCategorieMetier(CategorieLoader.getCategorieElectricite());
        creationAnnonceDTO.setSousCategorie(CategorieLoader.getCategorieElectricite().getSousCategories().get(0));
        creationAnnonceDTO.setTypeContact(TypeContact.EMAIL);
        creationAnnonceDTO.setVille("Nice");

        return creationAnnonceDTO;

    }

}
