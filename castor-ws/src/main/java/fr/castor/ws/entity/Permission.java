package fr.castor.ws.entity;

import fr.castor.core.constant.QueryJPQL;
import fr.castor.dto.enums.TypeCompte;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entité qui symbolise les permissions utilisateurs.
 *
 * @author Casaucau Cyril
 */
@Entity
@Table(name = "Permission")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.PERMISSION_CLIENT_BY_LOGIN,
                query = "SELECT p FROM Permission AS p WHERE p.client.login IS NOT NULL AND p.client.login = :login"),
        @NamedQuery(name = QueryJPQL.PERMISSION_ARTISAN_BY_LOGIN,
                query = "SELECT p FROM Permission AS p WHERE p.artisan.login IS NOT NULL AND p.artisan.login = :login")})
public class Permission extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private TypeCompte typeCompte;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_fk")
    private Artisan artisan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_fk")
    private Client client;

    /**
     * @return the typeCompte
     */
    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    /**
     * @param typeCompte the typeCompte to set
     */
    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the artisan
     */
    public Artisan getArtisan() {
        return artisan;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param artisan the artisan to set
     */
    public void setArtisan(Artisan artisan) {
        this.artisan = artisan;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) &&
                typeCompte == that.typeCompte &&
                Objects.equals(artisan, that.artisan) &&
                Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeCompte, artisan, client);
    }
}