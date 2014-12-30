package fr.batimen.web.client.validator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.util.lang.Args;

public class ChangePasswordValidator extends AbstractFormValidator {

    private static final long serialVersionUID = 8006933972594103363L;

    /** form components to be checked. */
    private final FormComponent<?>[] components;

    public ChangePasswordValidator(FormComponent<?> oldPassword, FormComponent<?> newPassword,
            FormComponent<?> confirmeNewPassword) {

        Args.notNull(oldPassword, "oldPassword");
        Args.notNull(newPassword, "newPassword");
        Args.notNull(confirmeNewPassword, "confirmeNewPassword");

        components = new FormComponent[] { oldPassword, newPassword, confirmeNewPassword };
    }

    @Override
    public FormComponent<?>[] getDependentFormComponents() {
        return components;
    }

    @Override
    public void validate(Form<?> form) {
        final FormComponent<?> oldPassword = components[0];
        final FormComponent<?> newPassword = components[1];
        final FormComponent<?> confirmeNewPassword = components[2];

        if (!oldPassword.getInput().isEmpty()
                && (newPassword.getInput().isEmpty() || confirmeNewPassword.getInput().isEmpty())) {
            if (newPassword.getInput().isEmpty()) {
                error(newPassword);
            } else {
                error(confirmeNewPassword);
            }

        } else if (!newPassword.getInput().isEmpty()
                && (oldPassword.getInput().isEmpty() || confirmeNewPassword.getInput().isEmpty())) {
            if (oldPassword.getInput().isEmpty()) {
                error(oldPassword);
            } else {
                error(confirmeNewPassword);
            }
            error(newPassword);
        } else if (!confirmeNewPassword.getInput().isEmpty()
                && (newPassword.getInput().isEmpty() || oldPassword.getInput().isEmpty())) {
            if (newPassword.getInput().isEmpty()) {
                error(newPassword);
            } else {
                error(oldPassword);
            }
            error(confirmeNewPassword);
        }
    }
}