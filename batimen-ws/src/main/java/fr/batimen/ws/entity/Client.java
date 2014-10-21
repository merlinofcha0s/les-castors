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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.batimen.core.constant.QueryJPQL;

/**
 * Entité client : symbolise un client en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Client")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.CLIENT_LOGIN, query = "SELECT c FROM Client AS c WHERE c.login = :login"),
        @NamedQuery(name = QueryJPQL.CLIENT_BY_EMAIL, query = "SELECT c FROM Client AS c WHERE c.email = :email"),
        @NamedQuery(name = QueryJPQL.CLIENT_BY_ACTIVATION_KEY,
                query = "SELECT c FROM Client AS c WHERE c.cleActivation = :cleActivation"),
        @NamedQuery(name = QueryJPQL.HASH_BY_LOGIN, query = "SELECT c.password FROM Client AS c WHERE c.login = :login") })
public class Client extends AbstractUser implements Serializable {

    private static final long serialVersionUID = -7591981472565360003L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @OneToMany(mappedBy = "demandeur", targetEntity = Annonce.class, cascade = CascadeType.REMOVE)
    private List<Annonce> devisDemandes = new ArrayList<Annonce>();
    @OneToMany(mappedBy = "client", targetEntity = Permission.class, cascade = CascadeType.REMOVE)
    protected List<Permission> permissions = new ArrayList<Permission>();

    /**
     * @return the devisDemandes
     */
    public List<Annonce> getDevisDemandes() {
        return devisDemandes;
    }

    /**
     * @param devisDemandes
     *            the devisDemandes to set
     */
    public void setDevisDemandes(List<Annonce> devisDemandes) {
        this.devisDemandes = devisDemandes;
    }

    /**
     * @return the typeCompte
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

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

        if (object instanceof Client) {
            Client other = (Client) object;
            return Objects.equals(this.login, other.login) && Objects.equals(this.email, other.email);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.login, this.email));
    }
}
