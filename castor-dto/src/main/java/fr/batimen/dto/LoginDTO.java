package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.PASSWORD_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.PASSWORD_RANGE_MIN;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Objet d'echange pour le login
 * 
 * @author Casaucau Cyril
 * 
 */
public class LoginDTO extends AbstractDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8518392464976304684L;

    @NotNull
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String login;
    @Size(min = PASSWORD_RANGE_MIN, max = PASSWORD_RANGE_MAX)
    private String password;

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login
     *            the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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

        if (object instanceof LoginDTO) {
            LoginDTO other = (LoginDTO) object;
            return Objects.equals(this.login, other.login) && Objects.equals(this.password, other.password);
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
        return Objects.hashCode(Objects.hash(this.login, this.password));
    }
}