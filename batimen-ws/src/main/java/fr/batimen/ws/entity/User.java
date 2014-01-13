package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import fr.batimen.core.constant.QueryJPQL;

@Entity
@Table(name = "Users")
@NamedQueries(value = { @NamedQuery(name = QueryJPQL.USER_LOGIN, query = "SELECT u FROM User AS u WHERE u.login = :login") })
public class User extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7591981472565360003L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(length = 4)
	private String civilite;
	@Column(length = 20)
	private String nom;
	@Column(length = 20)
	private String prenom;
	@Column(length = 10, nullable = false)
	private Integer numeroTel;
	@Column(length = 25, nullable = false)
	private String login;
	@Column(length = 80, nullable = false)
	private String password;
	@Column(length = 128, nullable = false)
	private String email;
	@Column(nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date dateInscription;
	@OneToMany(mappedBy = "demandeur", targetEntity = Annonce.class, cascade = CascadeType.REMOVE)
	private List<Annonce> devisDemandes;

	/**
	 * @return the devisDemandes
	 */
	public List<Annonce> getDevisDemandes() {
		return devisDemandes;
	}

	/**
	 * @param devisDemandes
	 *            the devisDemandes to set
	 */
	public void setDevisDemandes(List<Annonce> devisDemandes) {
		this.devisDemandes = devisDemandes;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the civilite
	 */
	public String getCivilite() {
		return civilite;
	}

	/**
	 * @param civilite
	 *            the civilite to set
	 */
	public void setCivilite(String civilite) {
		this.civilite = civilite;
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
	public Integer getNumeroTel() {
		return numeroTel;
	}

	/**
	 * @param numeroTel
	 *            the numeroTel to set
	 */
	public void setNumeroTel(Integer numeroTel) {
		this.numeroTel = numeroTel;
	}

	/**
	 * @return the dateInscription
	 */
	public Date getDateInscription() {
		return dateInscription;
	}

	/**
	 * @param dateInscription
	 *            the dateInscription to set
	 */
	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
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

		if (object instanceof User) {
			User other = (User) object;
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
}
