package fr.batimen.ws.utils;

import fr.batimen.ws.enums.PropertiesFileWS;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Utilitaire d'aide à la transformation de flux.
 *
 * @author Casaucau Cyril
 */
public class FluxUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxUtils.class);

    private FluxUtils() {
        super();
    }

    /**
     * Tranforme un input stream en string JSON
     *
     * @param content
     * @return
     */
    public static String getJsonByInputStream(InputStream content) {

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur pendant la transformation de l'input stream en JSON", e);
            }
        }
        return writer.toString();
    }

    /**
     * header sample
     * {
     * Content-Type=[image/png],
     * Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     **/
    //get uploaded filename, is there a easy way in RESTEasy?
    private static String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    /**
     * Transforme une liste de FormDataBodyPart en liste de File
     *
     * @param rawFiles La liste à tranformer
     * @return La liste de File correspondant
     */
    public static List<File> transformInputPartsToFiles(List<InputPart> rawFiles) {

        List<File> files = new LinkedList<>();

        for (InputPart rawfile : rawFiles) {
            Properties imageProperties = PropertiesFileWS.IMAGE.getProperties();

            // On construit le chemin ou l'on va enregistrer temporairement
            // l'image
            StringBuilder pathToFile = new StringBuilder(imageProperties.getProperty("temp.img.dir"));
            // On génére un nom aléatoire
            pathToFile.append(RandomStringUtils.random(80, true, true));
            // On récupère l'extension du fichier
            pathToFile.append(".").append(
                    FilenameUtils.getExtension(getFileName(rawfile.getHeaders())));

            File photo = new File(pathToFile.toString());

            try {
                FileUtils.copyInputStreamToFile(rawfile.getBody(InputStream.class, null), photo);
            } catch (IOException ioe) {
                LOGGER.error("Impossible d'ecrire l'input stream en file (photo) : {}", pathToFile.toString(), ioe);

            }

            files.add(photo);
        }
        return files;
    }
}