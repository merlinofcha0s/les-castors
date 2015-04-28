package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.batimen.dto.enums.TypeCompte;

public class DemandeAnnonceDTO extends AbstractDTO {

    private static final long serialVersionUID = 5456420499959611062L;

    @NotNull
    @Size(max = 255)
    protected String hashID;
    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    protected String loginDemandeur;
    protected TypeCompte typeCompteDemandeur;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DemandeAnnonceDTO{");
        sb.append("hashID='").append(hashID).append('\'');
        sb.append(", loginDemandeur='").append(loginDemandeur).append('\'');
        sb.append(", typeCompteDemandeur=").append(typeCompteDemandeur);
        sb.append('}');
        return sb.toString();
    }
}