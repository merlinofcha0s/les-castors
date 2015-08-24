package fr.batimen.ws.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(short categorieMetier) {
        this.categorieMetier = categorieMetier;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorieMetier that = (CategorieMetier) o;
        return Objects.equals(categorieMetier, that.categorieMetier) &&
                Objects.equals(id, that.id) &&
                Objects.equals(entreprise, that.entreprise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categorieMetier, entreprise);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategorieMetier{");
        sb.append("id=").append(id);
        sb.append(", categorieMetier=").append(categorieMetier);
        sb.append(", entreprise=").append(entreprise);
        sb.append('}');
        return sb.toString();
    }
}
