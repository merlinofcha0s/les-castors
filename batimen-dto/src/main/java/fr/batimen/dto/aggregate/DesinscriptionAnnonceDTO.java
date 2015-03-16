package fr.batimen.dto.aggregate;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

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

}
