package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;

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

    public static PermissionDTO deserializeLoginDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, PermissionDTO.class);
    }

    public static List<PermissionDTO> deserializeLoginDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<PermissionDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
