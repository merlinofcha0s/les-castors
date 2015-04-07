package fr.batimen.web.client.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Validator qui permet de controler les extensions des fichiers que les
 * utilisateurs veulent uploader.
 * 
 * @author Casaucau Cyril
 * 
 */
public class FileUploadValidator implements IValidator<Collection<FileUpload>> {

    private static final long serialVersionUID = 1L;

    private final List<String> allowedFileTypes = Arrays.asList("JPG", "PNG");

    @Override
    public void validate(IValidatable<Collection<FileUpload>> files) {
        for (FileUpload file : files.getValue()) {
            String extension = FilenameUtils.getExtension(file.getClientFileName());
            if (!allowedFileTypes.contains(extension.toUpperCase())) {
                ValidationError error = new ValidationError();

                StringBuilder errorMessage = new StringBuilder("Votre fichier doit être de type ");
                errorMessage.append(allowedFileTypes.toString());
                error.setMessage(errorMessage.toString());
                files.error(error);
            }
        }
    }
}
