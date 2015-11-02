package fr.batimen.ws.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Casaucau on 24/08/2015.
 */
@Entity
@Table(name = "MotCle")
public class MotCle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String motCle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonce_fk")
    private Annonce annonce;

    @OneToMany(mappedBy = "motCle",
            targetEntity = CategorieMetier.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private Set<CategorieMetier> categoriesMetier = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Set<CategorieMetier> getCategoriesMetier() {
        return categoriesMetier;
    }

    public void setCategoriesMetier(Set<CategorieMetier> categoriesMetier) {
        this.categoriesMetier = categoriesMetier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotCle motCle1 = (MotCle) o;
        return Objects.equals(motCle, motCle1.motCle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, motCle);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MotCle{");
        sb.append("id=").append(id);
        sb.append(", motCle='").append(motCle).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
