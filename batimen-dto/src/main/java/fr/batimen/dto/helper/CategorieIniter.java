package fr.batimen.dto.helper;

import fr.batimen.dto.CategorieDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe qui va lire le contenu du fichier des catégories et qui le trasforme en liste
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
        try {
            String adresseFichier = CategorieIniter.class.getClassLoader().getResource("mot-cles-categories.csv").getFile();
            Stream<String> lines = Files.lines(Paths.get(adresseFichier));
            allCategories = lines.map(mapToCategorie).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Erreur de lecture du ficher contenant les catégories", e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("----------------------------- Fin init du fichier des catégories --------------------------");
        }
    }

    public List<CategorieDTO> getAllCategories() {
        return allCategories;
    }
}