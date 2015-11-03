package fr.castor.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import fr.castor.dto.enums.TypeCompte;

public class PermissionDTO extends AbstractDTO {

    private static final long serialVersionUID = 3660353119551838149L;

    @NotNull
    private TypeCompte typeCompte;

    /**
     * @return the typeCompte
     */
    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    /**
     * @param typeCompte
     *            the typeCompte to set
     */
    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.typeCompte));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof PermissionDTO) {
            PermissionDTO other = (PermissionDTO) object;
            return Objects.equals(this.typeCompte, other.typeCompte);
        }
        return false;
    }
}