package fr.batimen.dto.helper;

import fr.batimen.dto.CategorieDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.util.Arrays;
import java.util.List;
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
public class CategorieIniter implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategorieIniter.class);
    /**
     * Transforme une ligne en CategorieDTO
     */
    public static Function<String, CategorieDTO> mapToCategorie = (line) -> {
        List<String> valeurs = Arrays.asList(line.split(";"));

        final CategorieDTO categorieDTO = new CategorieDTO();

        valeurs.forEach(valeur -> {
            //Si valeur est un integer c'est que c'est une categorie, sinon c'est un mot clé
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Valeur : " + valeur);
                }
                Integer categorie = Integer.valueOf(valeur);
                categorieDTO.getCategories().add(categorie);
            } catch (NumberFormatException nfe) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("C'est un mot clé !!!!");
                }
                categorieDTO.setMotCle(valeur);
            }
        });

        return categorieDTO;
    };
    private List<CategorieDTO> allCategories;

    public CategorieIniter() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("----------------------------- Début init du fichier des catégories --------------------------");
        }

        InputStream inputStreamCategories = CategorieIniter.class.getClassLoader().getResourceAsStream("mot-cles-categories.csv");
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

    public List<CategorieDTO> getAllCategories() {
        return allCategories;
    }
}