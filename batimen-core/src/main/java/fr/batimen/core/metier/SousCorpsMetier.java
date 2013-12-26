package fr.batimen.core.metier;

import java.io.Serializable;
import java.util.Objects;

/**
 * Symbolise la sous categorie de type de travaux
 * 
 * @author Casaucau Cyril
 * 
 */
public class SousCorpsMetier implements Serializable {

	private static final long serialVersionUID = 3981037191991395277L;

	private String nom;

	public SousCorpsMetier(String nom) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.nom));
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

		if (object instanceof SousCorpsMetier) {
			SousCorpsMetier other = (SousCorpsMetier) object;
			return Objects.equals(this.nom, other.nom);
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
