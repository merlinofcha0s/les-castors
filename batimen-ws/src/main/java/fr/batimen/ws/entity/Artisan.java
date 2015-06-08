package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.Civilite;

/**
 * Entité Artisan : symbolise un artisan en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Artisan")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.ARTISAN_BY_EMAIL,
                query = "SELECT art FROM Artisan AS art WHERE art.email = :email"),
        @NamedQuery(name = QueryJPQL.ARTISAN_BY_LOGIN,
                query = "SELECT art FROM Artisan AS art WHERE art.login = :login"),
        @NamedQuery(name = QueryJPQL.ARTISAN_HASH_BY_LOGIN,
                query = "SELECT a.password FROM Artisan AS a WHERE a.login = :login"),
        @NamedQuery(name = QueryJPQL.ARTISAN_BY_ACTIVATION_KEY,
                query = "SELECT a FROM Artisan AS a WHERE a.cleActivation = :cleActivation"),
        @NamedQuery(name = QueryJPQL.ARTISAN_STATUT_BY_LOGIN,
                query = "SELECT a.isActive FROM Artisan AS a WHERE a.login = :login") })
public class Artisan extends AbstractUser implements Serializable {

    private static final long serialVersionUID = -4398985801030020390L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected Civilite civilite;

    @OneToMany(mappedBy = "artisan", targetEntity = Notation.class, cascade = CascadeType.REMOVE)
    private List<Notation> scoreGlobal = new ArrayList<>();
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Entreprise entreprise;
    @OneToMany(mappedBy = "artisan",
            targetEntity = Permission.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<>();
    @OneToMany(mappedBy = "artisanNotifier",
            targetEntity = Notification.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private final Set<Notification> notifications = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artisans", targetEntity = Annonce.class)
    private final Set<Annonce> annonces = new HashSet<>();

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

    /**
     * @return the civilite
     */
    public Civilite getCivilite() {
        return civilite;
    }

    /**
     * @param civilite
     *            the civilite to set
     */
    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    /**
     * @return the typeCompte
     */
    public Set<Permission> getPermission() {
        return permissions;
    }

    /**
     * @param permission
     *            the typeCompte to set
     */
    public void setTypeCompte(Set<Permission> permission) {
        this.permissions = permission;
    }

    /**
     * @return the annonces
     */
    public Set<Annonce> getAnnonces() {
        return annonces;
    }

    /**
     * @param permission
     *            the permission to set
     */
    public void setPermission(Set<Permission> permission) {
        this.permissions = permission;
    }

    /**
     * @return the permissions
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @return the notifications
     */
    public Set<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param permissions
     *            the permissions to set
     */
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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
