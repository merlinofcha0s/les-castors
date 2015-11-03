package fr.castor.dto.aggregate;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.castor.dto.DemandeAnnonceDTO;
import fr.castor.dto.constant.ValidatorConstant;

public class DesinscriptionAnnonceDTO extends DemandeAnnonceDTO {

    private static final long serialVersionUID = -7272230926800633585L;

    @NotNull
    @Size(min = ValidatorConstant.CLIENT_LOGIN_RANGE_MIN, max = ValidatorConstant.CLIENT_LOGIN_RANGE_MAX)
    private String loginArtisan;

    /**
     * @return the loginArtisan
     */
    public String getLoginArtisan() {
        return loginArtisan;
    }

    /**
     * @param loginArtisan
     *            the loginArtisan to set
     */
    public void setLoginArtisan(String loginArtisan) {
        this.loginArtisan = loginArtisan;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.batimen.dto.DemandeAnnonceDTO#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.getId(), this.getLoginDemandeur(), this.getTypeCompteDemandeur(),
                this.getLoginArtisan()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.batimen.dto.DemandeAnnonceDTO#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            DesinscriptionAnnonceDTO other = (DesinscriptionAnnonceDTO) object;
            return Objects.equals(this.getLoginArtisan(), other.getLoginArtisan());
        } else {
            return false;
        }
    }
}