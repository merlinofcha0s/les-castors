package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.helper.DeserializeJsonHelper;

/**
 * Objet d'échanges pour les informations utilisateurs
 * 
 * @author Casaucau Cyril
 * 
 */
public class UserDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 908669177512952849L;

	@NotNull
	@Size(min = 4, max = 25)
	private String login;
	@NotNull
	@Size(min = 3, max = 40)
	private String fullname;
	@NotNull
	@Size(min = 80, max = 80)
	private String password;
	@Email
	@Size(max = 128)
	private String email;

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
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname == null ? "" : fullname;
	}

	/**
	 * @param fullname
	 *            the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
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

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (object instanceof UserDTO) {
			UserDTO other = (UserDTO) object;
			return Objects.equals(this.login, other.login) && Objects.equals(this.email, other.email)
					&& Objects.equals(this.fullname, other.fullname);
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
		return Objects.hashCode(Objects.hash(this.login, this.email, this.fullname));
	}

	public static UserDTO deserializeUserDTO(String json) {
		Gson gson = DeserializeJsonHelper.createGsonObject();
		return gson.fromJson(json, UserDTO.class);
	}

	public static List<UserDTO> deserializeUserDTOList(String json) {
		Gson gson = DeserializeJsonHelper.createGsonObject();
		Type collectionType = new TypeToken<List<UserDTO>>() {
		}.getType();
		return gson.fromJson(json, collectionType);
	}

}
