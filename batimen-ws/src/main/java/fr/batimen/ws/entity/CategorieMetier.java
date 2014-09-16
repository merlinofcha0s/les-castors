package fr.batimen.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entité Entreprise : Symbolise la categorie metier de l'entreprise en base de
 * données.
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

    @ManyToOne
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

}
