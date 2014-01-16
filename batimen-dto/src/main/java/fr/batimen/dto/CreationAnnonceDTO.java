package fr.batimen.dto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class CreationAnnonceDTO extends AbstractDTO {

	private static final long serialVersionUID = -7316855280979589583L;

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
	private String delaiIntervention;
	@Min(value = ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MIN)
	@Max(value = ValidatorConstant.CREATION_ANNONCE_NBDEVIS_MAX)
	@NotNull
	private Integer nbDevis;
	private String photo;
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
	private Boolean isEtape2 = true;
	@NotNull
	@Min(value = 01)
	@Max(value = 100)
	private Integer departement;

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

	public String getDelaiIntervention() {
		return delaiIntervention;
	}

	public void setDelaiIntervention(String delaiIntervention) {
		this.delaiIntervention = delaiIntervention;
	}

	public Integer getNbDevis() {
		return nbDevis;
	}

	public void setNbDevis(Integer nbDevis) {
		this.nbDevis = nbDevis;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public Boolean getIsEtape2() {
		return isEtape2;
	}

	public void setIsEtape2(Boolean isEtape2) {
		this.isEtape2 = isEtape2;
	}

	public Integer getDepartement() {
		return departement;
	}

	public void setDepartement(Integer departement) {
		this.departement = departement;
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

		if (object instanceof LoginDTO) {
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
