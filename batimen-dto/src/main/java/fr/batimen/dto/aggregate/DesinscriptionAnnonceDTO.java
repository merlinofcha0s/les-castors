package fr.batimen.dto.aggregate;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.batimen.dto.DemandeAnnonceDTO;

public class DesinscriptionAnnonceDTO extends DemandeAnnonceDTO {

    private static final long serialVersionUID = -7272230926800633585L;

    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
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
        return Objects.hashCode(Objects.hash(this.getHashID(), this.getLoginDemandeur(), this.getTypeCompteDemandeur(),
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