package fr.batimen.dto;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class CreationAnnonceDTO extends AbstractDTO {

	private static final long serialVersionUID = -7316855280979589583L;

	// Annonce
	@NotNull
	private Metier metier;
	@NotNull
	@Size(min = ValidatorConstant.CREATION_ANNONCE_TITRE_MIN, max = ValidatorConstant.CREATION_ANNONCE_TITRE_MAX)
	private String titre;
	@NotNull
	@Size(min = ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MIN, max = ValidatorConstant.CREATION_ANNONCE_DESCRIPTION_MAX)
	private String description;
	@NotNull
	private TypeContact typeContact;
	@Size(max = ValidatorConstant.CREATION_ANNONCE_DELAI_INTERVENTION_MAX)
	@NotNull
	private DelaiIntervention delaiIntervention;
	@Min(value = ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MIN)
	@Max(value = ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MAX)
	@NotNull
	private Integer nbDevis;
	private List<File> photos = new ArrayList<File>();
	@NotNull
	@Size(min = ValidatorConstant.CREATION_ANNONCE_ADRESSE_MIN, max = ValidatorConstant.CREATION_ANNONCE_ADRESSE_MAX)
	private String adresse;
	@NotNull
	@Size(max = ValidatorConstant.CREATION_ANNONCE_COMPLEMENT_ADRESSE_MAX)
	private String complementAdresse;
	@NotNull
	@Pattern(message = "Format code postal invalide", regexp = ValidatorConstant.CREATION_ANNONCE_CODE_POSTAL_REGEX)
	private String codePostal;
	@NotNull
	@Size(max = ValidatorConstant.CREATION_ANNONCE_VILLE_MAX)
	private String ville;
	@NotNull
	private Integer numeroEtape = 1;
	@NotNull
	@Min(value = 01)
	@Max(value = 100)
	private Integer departement;

	// Incription
	private Civilite civilite;
	@Size(min = ValidatorConstant.CREATION_ANNONCE_NOM_MIN, max = ValidatorConstant.CREATION_ANNONCE_NOM_MAX)
	private String nom;
	@Size(min = ValidatorConstant.CREATION_ANNONCE_PRENOM_MIN, max = ValidatorConstant.CREATION_ANNONCE_PRENOM_MAX)
	private String prenom;
	@NotNull
	@Pattern(message = "Numero de téléphone invalide", regexp = ValidatorConstant.CREATION_ANNONCE_TELEPHONE_REGEX)
	private String numeroTel;
	@NotNull
	@Size(min = ValidatorConstant.LOGIN_RANGE_MIN, max = ValidatorConstant.LOGIN_RANGE_MAX)
	private String login;
	@NotNull
	@Size(min = ValidatorConstant.LOGIN_RANGE_MIN, max = ValidatorConstant.LOGIN_RANGE_MAX)
	private String password;
	@NotNull
	@Email
	private String email;
	@NotNull
	private Date dateInscription;
	@NotNull
	private Boolean isSignedUp = false;

	public Metier getMetier() {
		return metier;
	}

	public void setMetier(Metier metier) {
		this.metier = metier;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeContact getTypeContact() {
		return typeContact;
	}

	public void setTypeContact(TypeContact typeContact) {
		this.typeContact = typeContact;
	}

	public DelaiIntervention getDelaiIntervention() {
		return delaiIntervention;
	}

	public void setDelaiIntervention(DelaiIntervention delaiIntervention) {
		this.delaiIntervention = delaiIntervention;
	}

	public Integer getNbDevis() {
		return nbDevis;
	}

	public void setNbDevis(Integer nbDevis) {
		this.nbDevis = nbDevis;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getComplementAdresse() {
		return complementAdresse;
	}

	public void setComplementAdresse(String complementAdresse) {
		this.complementAdresse = complementAdresse;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return the numeroEtape
	 */
	public Integer getNumeroEtape() {
		return numeroEtape;
	}

	/**
	 * @param numeroEtape
	 *            the numeroEtape to set
	 */
	public void setNumeroEtape(Integer numeroEtape) {
		this.numeroEtape = numeroEtape;
	}

	public Integer getDepartement() {
		return departement;
	}

	public void setDepartement(Integer departement) {
		this.departement = departement;
	}

	/**
	 * @return the photos
	 */
	public List<File> getPhotos() {
		return photos;
	}

	/**
	 * @param photos
	 *            the photos to set
	 */
	public void setPhotos(List<File> photos) {
		this.photos = photos;
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

	/**
	 * @return the isSignedUp
	 */
	public Boolean getIsSignedUp() {
		return isSignedUp;
	}

	/**
	 * @param isSignedUp
	 *            the isSignedUp to set
	 */
	public void setIsSignedUp(Boolean isSignedUp) {
		this.isSignedUp = isSignedUp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.titre, this.metier, this.description, this.codePostal, this.ville));
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

		if (object instanceof CreationAnnonceDTO) {
			CreationAnnonceDTO other = (CreationAnnonceDTO) object;
			return Objects.equals(this.titre, other.titre) && Objects.equals(this.metier, other.metier)
					&& Objects.equals(this.description, other.description)
					&& Objects.equals(this.codePostal, other.codePostal) && Objects.equals(this.ville, other.ville);
		}
		return false;
	}

	public static CreationAnnonceDTO deserializeCreationAnnonceDTO(String json) {
		Gson gson = DeserializeJsonHelper.createGsonObject();
		return gson.fromJson(json, CreationAnnonceDTO.class);
	}

	public static List<CreationAnnonceDTO> deserializeCreationAnnonceDTOList(String json) {
		Gson gson = DeserializeJsonHelper.createGsonObject();
		Type collectionType = new TypeToken<List<CreationAnnonceDTO>>() {
		}.getType();
		return gson.fromJson(json, collectionType);
	}

}
