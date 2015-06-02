package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entité categorie metier : Symbolise la categorie metier de l'entreprise en
 * base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "CategorieMetier")
public class CategorieMetier implements Serializable {

    private static final long serialVersionUID = -3444525849226642872L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private short categorieMetier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_fk")
    private Entreprise entreprise;

    /**
     * @return the categorieMetier
     */
    public short getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @return the entreprise
     */
    public Entreprise getEntreprise() {
        return entreprise;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    /**
     * @param entreprise
     *            the entreprise to set
     */
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorieMetier that = (CategorieMetier) o;
        return Objects.equals(categorieMetier, that.categorieMetier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categorieMetier);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategorieMetier{");
        sb.append("id=").append(id);
        sb.append(", categorieMetier=").append(categorieMetier);
        sb.append(", entreprise=").append(entreprise.getId());
        sb.append('}');
        return sb.toString();
    }
}
