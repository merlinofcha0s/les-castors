package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entité qui symbolise l'adresse en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Adresse")
public class Adresse extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 3650281700578111213L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(length = 255, nullable = false)
	private String adresse;
	@Column(length = 255, nullable = false)
	private String complementAdresse;
	@Column(length = 5, nullable = false)
	private String codePostal;
	@Column(length = 45, nullable = false)
	private String ville;
	@Column(nullable = false)
	private Integer departement;
	@OneToOne(mappedBy = "adresse", cascade = CascadeType.REMOVE)
	private Entreprise entreprise;
	@OneToOne(mappedBy = "adresseFacturation", cascade = CascadeType.REMOVE)
	private Paiement paiement;
	@OneToOne(mappedBy = "adresseChantier", cascade = CascadeType.REMOVE)
	private Annonce annonce;

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
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse
	 *            the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * @return the complementAdresse
	 */
	public String getComplementAdresse() {
		return complementAdresse;
	}

	/**
	 * @param complementAdresse
	 *            the complementAdresse to set
	 */
	public void setComplementAdresse(String complementAdresse) {
		this.complementAdresse = complementAdresse;
	}

	/**
	 * @return the codePostal
	 */
	public String getCodePostal() {
		return codePostal;
	}

	/**
	 * @param codePostal
	 *            the codePostal to set
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * @param ville
	 *            the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return the entreprise
	 */
	public Entreprise getEntreprise() {
		return entreprise;
	}

	/**
	 * @param entreprise
	 *            the entreprise to set
	 */
	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	/**
	 * @return the paiement
	 */
	public Paiement getPaiement() {
		return paiement;
	}

	/**
	 * @param paiement
	 *            the paiement to set
	 */
	public void setPaiement(Paiement paiement) {
		this.paiement = paiement;
	}

	/**
	 * @return the annonce
	 */
	public Annonce getAnnonce() {
		return annonce;
	}

	/**
	 * @param annonce
	 *            the annonce to set
	 */
	public void setAnnonce(Annonce annonce) {
		this.annonce = annonce;
	}

	/**
	 * @return the departement
	 */
	public Integer getDepartement() {
		return departement;
	}

	/**
	 * @param departement
	 *            the departement to set
	 */
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
		return Objects.hashCode(Objects.hash(this.adresse, this.complementAdresse, this.codePostal, this.ville));
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

		if (object instanceof Adresse) {
			Adresse other = (Adresse) object;
			return Objects.equals(this.adresse, other.adresse)
			        && Objects.equals(this.complementAdresse, other.complementAdresse)
			        && Objects.equals(this.codePostal, other.codePostal) && Objects.equals(this.ville, other.ville);
		}
		return false;
	}
}
