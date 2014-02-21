package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import fr.batimen.dto.enums.Metier;

/**
 * Entité Artisan : symbolise un artisan en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Artisan")
public class Artisan extends AbstractUser implements Serializable {

	private static final long serialVersionUID = -4398985801030020390L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Metier metier;
	@Column(length = 14, nullable = false)
	private Integer siret;
	@Column(nullable = false)
	private Integer nbAnnonce;
	@Column(nullable = false)
	private String activitePrincipale;
	@Column(nullable = false)
	private String domaineActivite;
	@OneToMany(mappedBy = "artisan", targetEntity = Notation.class, cascade = CascadeType.REMOVE)
	private List<Notation> scoreGlobal = new ArrayList<>();
	@OneToOne(cascade = CascadeType.REMOVE)
	private Entreprise entreprise;

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
	 * @return the metier
	 */
	public Metier getMetier() {
		return metier;
	}

	/**
	 * @param metier
	 *            the metier to set
	 */
	public void setMetier(Metier metier) {
		this.metier = metier;
	}

	/**
	 * @return the siret
	 */
	public Integer getSiret() {
		return siret;
	}

	/**
	 * @param siret
	 *            the siret to set
	 */
	public void setSiret(Integer siret) {
		this.siret = siret;
	}

	/**
	 * @return the nbAnnonce
	 */
	public Integer getNbAnnonce() {
		return nbAnnonce;
	}

	/**
	 * @param nbAnnonce
	 *            the nbAnnonce to set
	 */
	public void setNbAnnonce(Integer nbAnnonce) {
		this.nbAnnonce = nbAnnonce;
	}

	/**
	 * @return the activitePrincipale
	 */
	public String getActivitePrincipale() {
		return activitePrincipale;
	}

	/**
	 * @param activitePrincipale
	 *            the activitePrincipale to set
	 */
	public void setActivitePrincipale(String activitePrincipale) {
		this.activitePrincipale = activitePrincipale;
	}

	/**
	 * @return the domaineActivite
	 */
	public String getDomaineActivite() {
		return domaineActivite;
	}

	/**
	 * @param domaineActivite
	 *            the domaineActivite to set
	 */
	public void setDomaineActivite(String domaineActivite) {
		this.domaineActivite = domaineActivite;
	}

	/**
	 * @return the scoreGlobal
	 */
	public List<Notation> getScoreGlobal() {
		return scoreGlobal;
	}

	/**
	 * @param scoreGlobal
	 *            the scoreGlobal to set
	 */
	public void setScoreGlobal(List<Notation> scoreGlobal) {
		this.scoreGlobal = scoreGlobal;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.login, this.email, this.nom, this.prenom));
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

		if (object instanceof Artisan) {
			Artisan other = (Artisan) object;
			return Objects.equals(this.login, other.login) && Objects.equals(this.email, other.email)
			        && Objects.equals(this.nom, other.nom) && Objects.equals(this.prenom, other.prenom);
		}
		return false;
	}
}
