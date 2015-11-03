package fr.castor.ws.entity;

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
    private Short categorieMetier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_fk")
    private Entreprise entreprise;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motcle_fk")
    private MotCle motCle;

    public CategorieMetier(Short categorieMetier) {
        this();
        this.categorieMetier = categorieMetier;
    }

    public CategorieMetier() {
        super();
    }

    /**
     * @return the categorieMetier
     */
    public Short getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(Short categorieMetier) {
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

    public MotCle getMotCle() {
        return motCle;
    }

    public void setMotCle(MotCle motCle) {
        this.motCle = motCle;
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
        return Objects.equals(categorieMetier, that.categorieMetier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categorieMetier, entreprise);
    }

    @Override
    public String toString() {
        return String.valueOf(categorieMetier);
    }
}
