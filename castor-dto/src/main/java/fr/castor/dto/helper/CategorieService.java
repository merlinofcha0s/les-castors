package fr.castor.dto.helper;

import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.MotCleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe qui va lire le contenu du fichier des catégories et qui le trasforme en liste
 *
 * <p>
 *     Le fichier est de la forme motclé;categorie1;categorie2;etc
 * </p>
 *
 * @author Casaucau Cyril
 */
@Singleton
public class CategorieService implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorieService.class);
    /**
     * Transforme une ligne en CategorieDTO
     */
    public static final Function<String, MotCleDTO> mapToCategorie = (line) -> {
        List<String> valeurs = Arrays.asList(line.split(";"));

        final MotCleDTO motCleDTO = new MotCleDTO();

        valeurs.forEach(valeur -> {
            //Si valeur est un integer c'est que c'est une categorie, sinon c'est un mot clé
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Valeur : " + valeur);
                }
                Short categorie = Short.valueOf(valeur);

                CategorieMetierDTO categorieMetierDTO = new CategorieMetierDTO();
                categorieMetierDTO.setCategorieMetier(categorie);
                motCleDTO.getCategoriesMetier().add(categorieMetierDTO);
            } catch (NumberFormatException nfe) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un mot clé !!!!");
                }
                motCleDTO.setMotCle(valeur);
            }
        });

        return motCleDTO;
    };
    private List<MotCleDTO> allCategories;

    public CategorieService() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("----------------------------- Début init du fichier des catégories --------------------------");
        }

        InputStream inputStreamCategories = CategorieService.class.getClassLoader().getResourceAsStream("mot-cles-categories.csv");
        BufferedReader readerCategories = null;
        try {
            readerCategories = new BufferedReader(new InputStreamReader(inputStreamCategories, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UTF-8 pas supporté lors de la lecture du fichier de mots clé des catégories", e);
        }
        Stream<String> lines = readerCategories.lines();
        allCategories = lines.map(mapToCategorie).collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("----------------------------- Fin init du fichier des catégories --------------------------");
        }
    }

    public Optional<MotCleDTO> getCategorieByMotCle(String motcCle) {
        return allCategories.stream().filter(cat -> cat.getMotCle().equals(motcCle)).findFirst();
    }

    public List<MotCleDTO> getAllCategories() {
        return allCategories;
    }
}