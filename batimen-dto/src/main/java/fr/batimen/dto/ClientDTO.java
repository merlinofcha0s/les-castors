package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.helper.DeserializeJsonHelper;

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

    @Size(min = ValidatorConstant.LOGIN_RANGE_MIN, max = ValidatorConstant.LOGIN_RANGE_MAX)
    private String login;
    @Size(min = ValidatorConstant.PASSWORD_RANGE_MIN, max = ValidatorConstant.PASSWORD_RANGE_MAX)
    private String password;
    @Email
    @Size(max = 128)
    private String email;
    @Size(min = ValidatorConstant.CREATION_ANNONCE_NOM_MIN, max = ValidatorConstant.CREATION_ANNONCE_NOM_MAX)
    private String nom;
    @Size(min = ValidatorConstant.CREATION_ANNONCE_PRENOM_MIN, max = ValidatorConstant.CREATION_ANNONCE_PRENOM_MAX)
    private String prenom;
    @NotNull
    private Boolean isArtisan = false;
    @Pattern(message = "Numero de téléphone invalide", regexp = ValidatorConstant.CREATION_ANNONCE_TELEPHONE_REGEX)
    private String numeroTel;
    @NotNull
    private boolean isActive;

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
     * @return the isArtisan
     */
    public Boolean getIsArtisan() {
        return isArtisan;
    }

    /**
     * @param isArtisan
     *            the isArtisan to set
     */
    public void setIsArtisan(Boolean isArtisan) {
        this.isArtisan = isArtisan;
    }

    /**
     * @return the isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param isActive
     *            the isActive to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
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

    public static ClientDTO deserializeUserDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, ClientDTO.class);
    }

    public static List<ClientDTO> deserializeUserDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<ClientDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
