package fr.batimen.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.batimen.dto.constant.ValidatorConstant;

/**
 * DTO du mail de contact
 * 
 * @author Adnane
 *
 */
public class ContactMailDTO extends AbstractDTO {

	private static final long serialVersionUID = -2959507422753247664L;

	@NotNull
	private String name;
	@NotNull
	private String email;
	@NotNull
	private String subject;
	@NotNull
	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.name, this.email, this.subject, this.message));
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof ContactMailDTO) {
			ContactMailDTO other = (ContactMailDTO) object;
			return Objects.equals(this.name, other.name) && Objects.equals(this.email, other.email)
					&& Objects.equals(this.subject, other.subject)
					&& Objects.equals(this.message, other.message);
		}
		return false;
	}

}
