package fr.batimen.ws.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    /*public static String getJsonByInputStream(InputStream content) {

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(content, writer, "UTF-8");
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Erreur pendant la transformation de l'input stream en JSON", e);
            }
        }
        return writer.toString();
    }*/

    /**
     * Transforme une liste de FormDataBodyPart en liste de File
     * 
     * @param formDataBodyParts
     *            La liste à tranformer
     * @return La liste de File correspondant
     */
    /*public static List<File> transformFormDataBodyPartsToFiles(List<FormDataBodyPart> formDataBodyParts) {

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
    }*/
}