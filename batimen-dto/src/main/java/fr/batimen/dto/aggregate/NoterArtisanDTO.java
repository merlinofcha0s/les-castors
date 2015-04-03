package fr.batimen.dto.aggregate;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.NotationDTO;

public class NoterArtisanDTO extends AbstractDTO {

    private static final long serialVersionUID = 1830194401199647981L;

    @Valid
    private NotationDTO notation;
    @NotNull
    private String hashID;
    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String loginDemandeur;
    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String loginArtisan;

    /**
     * @return the notation
     */
    public NotationDTO getNotation() {
        return notation;
    }

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
     * @return the loginArtisan
     */
    public String getLoginArtisan() {
        return loginArtisan;
    }

    /**
     * @param notation
     *            the notation to set
     */
    public void setNotation(NotationDTO notation) {
        this.notation = notation;
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
     * @param loginArtisan
     *            the loginArtisan to set
     */
    public void setLoginArtisan(String loginArtisan) {
        this.loginArtisan = loginArtisan;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof NoterArtisanDTO) {
            NoterArtisanDTO other = (NoterArtisanDTO) object;
            return Objects.equals(this.notation.getScore(), other.notation.getScore())
                    && Objects.equals(this.notation.getCommentaire(), other.notation.getCommentaire())
                    && Objects.equals(this.hashID, other.hashID)
                    && Objects.equals(this.loginArtisan, other.loginArtisan)
                    && Objects.equals(this.loginDemandeur, other.loginDemandeur);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.notation.getScore(), this.notation.getCommentaire(), this.hashID,
                this.loginArtisan, this.loginDemandeur));
    }
}