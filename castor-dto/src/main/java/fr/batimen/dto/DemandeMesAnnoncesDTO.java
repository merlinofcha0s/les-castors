package fr.batimen.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;

/**
 * DTO servant récuperer les informations pour la page mes annonces.
 *
 * @author Casaucau Cyril
 */
public class DemandeMesAnnoncesDTO extends AbstractDTO {

    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String login;

    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String loginDemandeur;

    private Integer rangeAnnoncesDebut;

    private Integer rangeAnnonceFin;

    private Integer rangeNotificationsDebut;

    private Integer rangeNotificationsFin;

    public Integer getRangeNotificationsFin() {
        return rangeNotificationsFin;
    }

    public void setRangeNotificationsFin(Integer rangeNotificationsFin) {
        this.rangeNotificationsFin = rangeNotificationsFin;
    }

    public Integer getRangeNotificationsDebut() {
        return rangeNotificationsDebut;
    }

    public void setRangeNotificationsDebut(Integer rangeNotificationsDebut) {
        this.rangeNotificationsDebut = rangeNotificationsDebut;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginDemandeur() {
        return loginDemandeur;
    }

    public void setLoginDemandeur(String loginDemandeur) {
        this.loginDemandeur = loginDemandeur;
    }

    public Integer getRangeAnnoncesDebut() {
        return rangeAnnoncesDebut;
    }

    public void setRangeAnnoncesDebut(Integer rangeAnnoncesDebut) {
        this.rangeAnnoncesDebut = rangeAnnoncesDebut;
    }

    public Integer getRangeAnnonceFin() {
        return rangeAnnonceFin;
    }

    public void setRangeAnnonceFin(Integer rangeAnnonceFin) {
        this.rangeAnnonceFin = rangeAnnonceFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandeMesAnnoncesDTO that = (DemandeMesAnnoncesDTO) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(loginDemandeur, that.loginDemandeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, loginDemandeur);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DemandeMesAnnoncesDTO{");
        sb.append("login='").append(login).append('\'');
        sb.append(", loginDemandeur='").append(loginDemandeur).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
