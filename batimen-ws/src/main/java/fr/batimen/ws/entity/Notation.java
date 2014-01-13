package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Notation")
public class Notation extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -1038954593364210382L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Integer score;
	@Column(length = 500, nullable = false)
	private String commentaire;
	@OneToOne(cascade = CascadeType.REMOVE)
	private Annonce annonce;
	@ManyToOne
	@JoinColumn(name = "artisan_fk")
	private Artisan artisan;

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
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * @return the commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * @param commentaire
	 *            the commentaire to set
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(Objects.hash(this.score, this.commentaire));
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

		if (object instanceof Notation) {
			Notation other = (Notation) object;
			return Objects.equals(this.score, other.score) && Objects.equals(this.commentaire, other.commentaire);
		}
		return false;
	}

}
