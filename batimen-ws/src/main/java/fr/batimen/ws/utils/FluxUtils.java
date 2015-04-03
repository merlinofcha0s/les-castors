package fr.batimen.ws.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;

import fr.batimen.ws.enums.PropertiesFileWS;

/**
 * Utilitaire d'aide à la transformation de flux.
 * 
 * @author Casaucau Cyril
 * 
 */
public class FluxUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxUtils.class);

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
     * Transforme une liste de FormDataBodyPart en liste de File
     * 
     * @param formDataBodyParts
     *            La liste à tranformer
     * @return La liste de File correspondant
     */
    public static List<File> transformFormDataBodyPartsToFiles(List<FormDataBodyPart> formDataBodyParts) {

        List<File> files = new LinkedList<File>();

        for (FormDataBodyPart formDataBodyPart : formDataBodyParts) {
            BodyPartEntity bpe = (BodyPartEntity) formDataBodyPart.getEntity();

            Properties imageProperties = PropertiesFileWS.IMAGE.getProperties();

            // On construit le chemin ou l'on va enregistrer temporairement
            // l'image
            StringBuilder pathToFile = new StringBuilder(imageProperties.getProperty("temp.img.dir"));
            // On génére un nom aléatoire
            pathToFile.append(RandomStringUtils.random(80, true, true));
            // On récupère l'extension du fichier
            pathToFile.append(".").append(
                    FilenameUtils.getExtension(formDataBodyPart.getContentDisposition().getFileName()));

            File photo = new File(pathToFile.toString());

            try {
                FileUtils.copyInputStreamToFile(bpe.getInputStream(), photo);
            } catch (IOException ioe) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Impossible d'ecrire l'input stream en file (photo)", ioe);
                }
            }

            files.add(photo);
        }
        return files;
    }
}