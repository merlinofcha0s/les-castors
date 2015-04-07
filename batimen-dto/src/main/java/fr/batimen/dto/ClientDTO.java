package fr.batimen.dto;

import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_LOGIN_RANGE_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_NOM_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_NOM_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_PRENOM_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.CLIENT_PRENOM_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.PASSWORD_RANGE_MAX;
import static fr.batimen.dto.constant.ValidatorConstant.PASSWORD_RANGE_MIN;
import static fr.batimen.dto.constant.ValidatorConstant.TELEPHONE_REGEX;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.modelmapper.ModelMapper;

import fr.batimen.dto.enums.Civilite;

/**
 * Objet d'échanges pour les informations utilisateurs
 * 
 * @author Casaucau Cyril
 * 
 */
public class ClientDTO extends AbstractDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 908669177512952849L;

    private Civilite civilite;
    @Size(min = CLIENT_LOGIN_RANGE_MIN, max = CLIENT_LOGIN_RANGE_MAX)
    private String login;
    @Size(min = PASSWORD_RANGE_MIN, max = PASSWORD_RANGE_MAX)
    private String password;
    @Size(min = PASSWORD_RANGE_MIN, max = PASSWORD_RANGE_MAX)
    private String oldPassword;
    @Email
    @Size(max = 128)
    private String email;
    @Size(min = CLIENT_NOM_MIN, max = CLIENT_NOM_MAX)
    private String nom;
    @Size(min = CLIENT_PRENOM_MIN, max = CLIENT_PRENOM_MAX)
    private String prenom;
    @Pattern(message = "Numero de téléphone invalide", regexp = TELEPHONE_REGEX)
    private String numeroTel;
    private String cleActivation;
    @NotNull
    private Boolean isActive = false;
    @Valid
    private final List<PermissionDTO> permissions = new ArrayList<PermissionDTO>();

    /**
     * @return the login
     */
    public String getLogin() {
        return login == null ? "" : login;
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
        return password == null ? "" : password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email == null ? "" : email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the numeroTel
     */
    public String getNumeroTel() {
        return numeroTel;
    }

    /**
     * @param numeroTel
     *            the numeroTel to set
     */
    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     *            the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the cleActivation
     */
    public String getCleActivation() {
        return cleActivation == null ? "" : cleActivation;
    }

    /**
     * @param cleActivation
     *            the cleActivation to set
     */
    public void setCleActivation(String cleActivation) {
        this.cleActivation = cleActivation;
    }

    /**
     * @return the civilite
     */
    public Civilite getCivilite() {
        return civilite;
    }

    /**
     * @param civilite
     *            the civilite to set
     */
    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    /**
     * @return the permissions
     */
    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword
     *            the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof ClientDTO) {
            ClientDTO other = (ClientDTO) object;
            return Objects.equals(this.login, other.login) && Objects.equals(this.email, other.email);
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
        return Objects.hashCode(Objects.hash(this.login, this.email));
    }

    public static ClientDTO copy(ClientDTO clientSource) {
        ModelMapper mapper = new ModelMapper();
        ClientDTO clientCopied = new ClientDTO();
        mapper.map(clientSource, clientCopied);
        return clientCopied;
    }
}
