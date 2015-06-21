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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Casaucau on 21/06/2015.
 */
@Singleton
public class CSVCodePostalReader implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVCodePostalReader.class);

    private final Map<String, LocalisationDTO> localisationDTOs = new LinkedHashMap<>();

    public CSVCodePostalReader() {

        File codePostalFile = new File(WebApplication.get().getServletContext().getRealPath(File.pathSeparator).split(";")[0] + "WEB-INF/codePostal.txt");
        LOGGER.error(codePostalFile.getAbsolutePath());

        try {
            List<String> lines = Files.readAllLines(codePostalFile.toPath(),
                    StandardCharsets.UTF_8);
            for(String line: lines){
                LocalisationDTO localisationDTO = new LocalisationDTO();
                String[] csvLine = line.split(";");
                localisationDTO.setDepartement(csvLine[1]);
                localisationDTO.setCommune(csvLine[2]);
                localisationDTO.setDepartement(csvLine[1].substring(0, 3));
                localisationDTOs.put(csvLine[1], localisationDTO);
            }
        } catch (IOException e) {
            LOGGER.error("Problème de lecture avec le fichier de code postal", e);
        }
    }

    public Map<String, LocalisationDTO> getLocalisationDTOs() {
        return localisationDTOs;
    }
}