package fr.batimen.core.metier;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Symbolise le type de travaux demandé par le client
 * 
 * @author Casaucau Cyril
 * 
 */
public class CorpsMetier implements Serializable {

	private static final long serialVersionUID = 8534943151697223122L;

	private String nom;
	private final List<SousCorpsMetier> sousCorpsMetier = new LinkedList<SousCorpsMetier>();

	public CorpsMetier(String nom) {
		this.nom = nom;
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
	 * @return the sousCorpsMetier
	 */
	public List<SousCorpsMetier> getSousCorpsMetier() {
		return sousCorpsMetier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.nom, this.sousCorpsMetier));
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

		if (object instanceof CorpsMetier) {
			CorpsMetier other = (CorpsMetier) object;
			return Objects.equals(this.nom, other.nom) && Objects.equals(this.sousCorpsMetier, other.sousCorpsMetier);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return nom;
	}

}
