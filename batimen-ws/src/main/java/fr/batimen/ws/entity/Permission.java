package fr.batimen.ws.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.batimen.dto.enums.TypeCompte;

/**
 * Entité qui symbolise les permissions utilisateurs.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Permission")
public class Permission extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private TypeCompte typeCompte;
    @ManyToOne
    @JoinColumn(name = "artisan_fk")
    private Artisan artisan;
    @ManyToOne
    @JoinColumn(name = "client_fk")
    private Client client;

    /**
     * @return the typeCompte
     */
    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    /**
     * @param typeCompte
     *            the typeCompte to set
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
     * @param artisan
     *            the artisan to set
     */
    public void setArtisan(Artisan artisan) {
        this.artisan = artisan;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

}
