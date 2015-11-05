package fr.castor.web.client.behaviour;

import fr.castor.core.enums.PropertiesFileGeneral;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.client.event.FeedBackPanelEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.wicketstuff.html5.fileapi.FileFieldSizeCheckBehavior;
import org.wicketstuff.html5.fileapi.FileList;
import org.wicketstuff.html5.fileapi.Html5File;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Casaucau Cyril on 14/04/2015.
 * <p/>
 * Behaviour permettant de valider une photo : son type et son poids ainsi que le nombre de fichier, le tout coté client
 * <p/>
 * Elle charge également les photos directement dans la DTO
 *
 * @author Casaucau Cyril
 * @see FileFieldSizeCheckBehavior
 * <p/>
 * Son poids est checké grace à l'API File en HTML5
 */
public class FileFieldValidatorAndLoaderBehaviour extends FileFieldSizeCheckBehavior {

    private final List<String> allowedFileTypes = Arrays.asList("JPG", "PNG", "JPEG");

    private boolean validationOK = true;

    /**
     * Constructeur principal pour cette behaviour
     */
    public FileFieldValidatorAndLoaderBehaviour() {
        super();
    }


    /**
     * Called when size validation passed.
     *
     * @param target   wicket object representing the ajax response, not-null
     * @param fileList
     */
    @Override
    protected void onSubmit(AjaxRequestTarget target, FileList fileList) {
        boolean errorExtensionInvalide = false;
        boolean errorNumberOfFiles = false;

        Integer maxFileParAnnonce = Integer.valueOf(PropertiesFileGeneral.GENERAL.getProperties().getProperty("gen.max.number.file.annonce"));

        if (fileList.getNumOfFiles() > maxFileParAnnonce) {
            errorExtensionInvalide = true;
            StringBuilder sbErreurMaxFile = new StringBuilder("Vous ne pouvez pas charger plus de ");
            sbErreurMaxFile.append(maxFileParAnnonce).append(" photos par annonce.");
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, sbErreurMaxFile.toString(), FeedbackMessageLevel.ERROR));
        } else {
            //On valide le type de fichier
            for (int i = 0; i < fileList.getNumOfFiles(); i++) {
                Html5File file = fileList.get(i);
                String extension = FilenameUtils.getExtension(file.getName());
                if (!allowedFileTypes.contains(extension.toUpperCase())) {
                    StringBuilder errorMessage = new StringBuilder("Votre fichier doit être de type ");
                    errorMessage.append(allowedFileTypes.toString());
                    target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, errorMessage.toString(), FeedbackMessageLevel.ERROR));
                    errorExtensionInvalide = true;
                }
            }
        }

        if (!errorExtensionInvalide && !errorNumberOfFiles) {
            validationOK = true;
            target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target, "Vérification photo(s) OK !", FeedbackMessageLevel.SUCCESS));
        } else {
            validationOK = false;
        }
    }

    /**
     * Called when size validation failed.
     *
     * @param target   wicket object representing the ajax response, not-null
     * @param fileList
     */
    @Override
    protected void onError(AjaxRequestTarget target, FileList fileList) {
        target.getPage().send(target.getPage(), Broadcast.BREADTH, new FeedBackPanelEvent(target));
    }

    public boolean isValidationOK() {
        return validationOK;
    }

    public void setValidationOK(boolean validationOK) {
        this.validationOK = validationOK;
    }
}