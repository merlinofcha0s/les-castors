package fr.castor.web.app.utils.codepostal;

import fr.castor.dto.LocalisationDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse et classe la ville et le departement par code postal d'apres un fichier csv
 *
 * @author Casaucau Cyril
 */
@Singleton
public class CSVCodePostalReader implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVCodePostalReader.class);

    //Triée par code postal
    //On utilise direct l'implementation car elle implemente serializable ce qui n'est pas le cas de l'interface (Map)
    private final LinkedHashMap<String, List<LocalisationDTO>> localisationDTOs = new LinkedHashMap<>();

    public CSVCodePostalReader() {

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Chargement du fichier csv contenant les informations de localisation");
        }

        ClassLoader classLoader = getClass().getClassLoader();

        File codePostalFile = new File("codePostal.txt");
        try {
            FileUtils.copyInputStreamToFile(classLoader.getResourceAsStream("codePostal.txt"), codePostalFile);
        } catch (IOException e) {
            LOGGER.error(codePostalFile.getAbsolutePath(), e);
        }

        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Lecture du fichier : ");
                LOGGER.debug(codePostalFile.getAbsolutePath());
            }
            List<String> lines = Files.readAllLines(codePostalFile.toPath(),
                    StandardCharsets.UTF_8);
            for(String line: lines){
                String[] csvLine = line.split(";");
                String codePostal = csvLine[1];
                String commune = csvLine[2];
                String departement = csvLine[1].substring(0, 2);

                LocalisationDTO localisationDTO = new LocalisationDTO();
                localisationDTO.setCodePostal(codePostal);
                localisationDTO.setCommune(commune);
                localisationDTO.setDepartement(departement);

                if(localisationDTOs.containsKey(codePostal)){
                    localisationDTOs.get(codePostal).add(localisationDTO);
                } else {
                    List<LocalisationDTO> memeCodePostalList = new LinkedList<>();
                    memeCodePostalList.add(localisationDTO);
                    localisationDTOs.put(codePostal, memeCodePostalList);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Problème de lecture avec le fichier de code postal", e);
        }
    }

    public LinkedHashMap<String, List<LocalisationDTO>> getLocalisationDTOs() {
        return localisationDTOs;
    }
}