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

import fr.batimen.dto.enums.StatutJuridique;

/**
 * Entité Entreprise : Symbolise l'entreprise de l'artisan en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Entreprise")
public class Entreprise extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 8234078910852637284L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(length = 40, nullable = false)
	private String nomComplet;
	@Column(nullable = false)
	private StatutJuridique statutJuridique;
	@Column(nullable = false)
	private Integer capitalSociale;
	@Column(nullable = false)
	private Integer nbEmployees;
	@Column(length = 255, nullable = false)
	private String logo;
	@OneToOne(mappedBy = "entreprise", cascade = CascadeType.REMOVE)
	private Artisan artisan;
	@OneToOne(cascade = CascadeType.REMOVE)
	private Paiement paiement;
	@OneToOne(cascade = CascadeType.REMOVE)
	private Adresse adresse;

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
	 * @return the nomComplet
	 */
	public String getNomComplet() {
		return nomComplet;
	}

	/**
	 * @param nomComplet
	 *            the nomComplet to set
	 */
	public void setNomComplet(String nomComplet) {
		this.nomComplet = nomComplet;
	}

	/**
	 * @return the statutJuridique
	 */
	public StatutJuridique getStatutJuridique() {
		return statutJuridique;
	}

	/**
	 * @param statutJuridique
	 *            the statutJuridique to set
	 */
	public void setStatutJuridique(StatutJuridique statutJuridique) {
		this.statutJuridique = statutJuridique;
	}

	/**
	 * @return the capitalSociale
	 */
	public Integer getCapitalSociale() {
		return capitalSociale;
	}

	/**
	 * @param capitalSociale
	 *            the capitalSociale to set
	 */
	public void setCapitalSociale(Integer capitalSociale) {
		this.capitalSociale = capitalSociale;
	}

	/**
	 * @return the nbEmployees
	 */
	public Integer getNbEmployees() {
		return nbEmployees;
	}

	/**
	 * @param nbEmployees
	 *            the nbEmployees to set
	 */
	public void setNbEmployees(Integer nbEmployees) {
		this.nbEmployees = nbEmployees;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the artisan
	 */
	public Artisan getArtisan() {
		return artisan;
	}

	/**
	 * @param artisan
	 *            the artisan to set
	 */
	public void setArtisan(Artisan artisan) {
		this.artisan = artisan;
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
	 * @return the adresse
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse
	 *            the adresse to set
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.nomComplet, this.statutJuridique, this.nbEmployees));
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

		if (object instanceof Entreprise) {
			Entreprise other = (Entreprise) object;
			return Objects.equals(this.nomComplet, other.nomComplet)
					&& Objects.equals(this.statutJuridique, other.statutJuridique)
					&& Objects.equals(this.nbEmployees, other.nbEmployees);
		}
		return false;
	}

}
