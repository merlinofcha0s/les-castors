package fr.batimen.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.batimen.dto.constant.ValidatorConstant;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;

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
	@Size(min = ValidatorConstant.CREATION_ANNONCE_CODEPOSTAL_MAX, max = ValidatorConstant.CREATION_ANNONCE_CODEPOSTAL_MAX)
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

}
