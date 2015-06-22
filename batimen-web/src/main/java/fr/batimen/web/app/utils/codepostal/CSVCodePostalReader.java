package fr.batimen.web.app.utils.codepostal;

import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Casaucau on 21/06/2015.
 */
@Singleton
public class CSVCodePostalReader implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVCodePostalReader.class);

    private final Map<String, List<LocalisationDTO>> localisationDTOs = new LinkedHashMap<>();

    public CSVCodePostalReader() {

        File codePostalFile = new File(WebApplication.get().getServletContext().getRealPath(File.pathSeparator).split(";")[0] + "WEB-INF/codePostal.txt");
        LOGGER.error(codePostalFile.getAbsolutePath());

        try {
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

    public Map<String, List<LocalisationDTO>> getLocalisationDTOs() {
        return localisationDTOs;
    }
}