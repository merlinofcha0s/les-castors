package fr.castor.ws.utils;

import fr.castor.dto.constant.Categorie;
import fr.castor.ws.entity.CategorieMetier;
import fr.castor.ws.entity.MotCle;

import javax.inject.Named;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Casaucau on 24/12/2015.
 */
@Named
public class EmailUtils {

    public EmailUtils() {
    }

    public String transformMotsClesToCSV(Set<MotCle> motsCles){
       return  motsCles.stream().map(motCle -> motCle.getMotCle()).collect(Collectors.joining(", "));
    }

    public String transformCategorieToCSV(Set<MotCle> motsCles){

        Set<String> categorie = new HashSet<>();

        for (MotCle motCle : motsCles) {
            for (CategorieMetier categorieMetier : motCle.getCategoriesMetier()) {
                String nomCateg = Categorie.getNameByCode(categorieMetier.getCategorieMetier());
                categorie.add(nomCateg);
            }
        }
        return categorie.stream().collect(Collectors.joining(", "));
    }
}
