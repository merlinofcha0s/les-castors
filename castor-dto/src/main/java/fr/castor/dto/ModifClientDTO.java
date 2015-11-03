package fr.castor.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO utilisé pour modifier les informatins d'un client.
 *
 * @author Casaucau Cyril
 */
public class ModifClientDTO extends ClientDTO {

    @NotNull
    @Email
    private String oldEmail;

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModifClientDTO that = (ModifClientDTO) o;
        return Objects.equals(oldEmail, that.oldEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), oldEmail);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModifClientDTO{");
        sb.append('}');
        return sb.toString();
    }
}
