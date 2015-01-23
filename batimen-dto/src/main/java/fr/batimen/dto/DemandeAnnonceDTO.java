package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class DemandeAnnonceDTO extends AbstractDTO {

    private static final long serialVersionUID = 5456420499959611062L;

    @NotNull
    @Size(max = 255)
    private String hashID;
    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String loginDemandeur;
    @NotNull
    private TypeCompte typeCompteDemandeur;

    /**
     * @return the hashID
     */
    public String getHashID() {
        return hashID;
    }

    /**
     * @return the loginDemandeur
     */
    public String getLoginDemandeur() {
        return loginDemandeur;
    }

    /**
     * @return the typeCompteDemandeur
     */
    public TypeCompte getTypeCompteDemandeur() {
        return typeCompteDemandeur;
    }

    /**
     * @param hashID
     *            the hashID to set
     */
    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    /**
     * @param loginDemandeur
     *            the loginDemandeur to set
     */
    public void setLoginDemandeur(String loginDemandeur) {
        this.loginDemandeur = loginDemandeur;
    }

    /**
     * @param typeCompteDemandeur
     *            the typeCompteDemandeur to set
     */
    public void setTypeCompteDemandeur(TypeCompte typeCompteDemandeur) {
        this.typeCompteDemandeur = typeCompteDemandeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects
                .hashCode(Objects.hash(this.getHashID(), this.getLoginDemandeur(), this.getTypeCompteDemandeur()));
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

        if (object instanceof DemandeAnnonceDTO) {
            DemandeAnnonceDTO other = (DemandeAnnonceDTO) object;
            return Objects.equals(this.getHashID(), other.getHashID())
                    && Objects.equals(this.getLoginDemandeur(), other.getLoginDemandeur())
                    && Objects.equals(this.getTypeCompteDemandeur(), other.getTypeCompteDemandeur());
        }
        return false;
    }

    public static DemandeAnnonceDTO deserializeDemandeAnnonceDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, DemandeAnnonceDTO.class);
    }

    public static List<DemandeAnnonceDTO> deserializeDemandeAnnonceDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<DemandeAnnonceDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
