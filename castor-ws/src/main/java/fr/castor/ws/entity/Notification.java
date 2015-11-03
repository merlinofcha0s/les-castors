package fr.castor.ws.entity;

import fr.castor.core.constant.QueryJPQL;
import fr.castor.dto.enums.StatutNotification;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.enums.TypeNotification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Entité qui symbolise les notifications en base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Notification")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.NOTIFICATION_BY_CLIENT_LOGIN,
                query = "SELECT n.typeNotification, n.dateNotification, n.statutNotification, n.pourQuiNotification, artisanNotifier.login, clientNotifier.login, artisanNotifier.entreprise.nomComplet, annonce.hashID, artisanNotifier.entreprise.siret  FROM Notification AS n WHERE n.clientNotifier.login = :login AND annonce.etatAnnonce != 4 AND pourQuiNotification = :typeCompte ORDER BY dateNotification ASC"),
        @NamedQuery(name = QueryJPQL.NOTIFICATION_BY_ARTISAN_LOGIN,
                query = "SELECT n.typeNotification, n.dateNotification, n.statutNotification, n.pourQuiNotification, artisanNotifier.login, clientNotifier.login, artisanNotifier.entreprise.nomComplet, annonce.hashID,  artisanNotifier.entreprise.siret  FROM Notification AS n WHERE n.artisanNotifier.login = :login AND annonce.etatAnnonce != 4 AND pourQuiNotification = :typeCompte ORDER BY dateNotification ASC"),
        @NamedQuery(name = QueryJPQL.NOTIFICATION_COUNT_BY_ARTISAN_LOGIN,
                query = "SELECT COUNT(n) FROM Notification AS n WHERE n.artisanNotifier.login = :login AND n.pourQuiNotification = :typeCompte"),
        @NamedQuery(name = QueryJPQL.NOTIFICATION_COUNT_BY_CLIENT_LOGIN,
                query = "SELECT COUNT(n) FROM Notification AS n WHERE n.clientNotifier.login = :login AND n.pourQuiNotification = :typeCompte AND n.annonce.etatAnnonce != 4")
})
public class Notification extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1953843726850626949L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TypeNotification typeNotification;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateNotification;
    @Column(nullable = false)
    private StatutNotification statutNotification;
    @Column(nullable = false)
    private TypeCompte pourQuiNotification;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artisan")
    private Artisan artisanNotifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client clientNotifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_annonce")
    private Annonce annonce;

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
     * @return the typeNotification
     */
    public TypeNotification getTypeNotification() {
        return typeNotification;
    }

    /**
     * @param typeNotification
     *            the typeNotification to set
     */
    public void setTypeNotification(TypeNotification typeNotification) {
        this.typeNotification = typeNotification;
    }

    /**
     * @return the dateNotification
     */
    public Date getDateNotification() {
        return dateNotification;
    }

    /**
     * @param dateNotification
     *            the dateNotification to set
     */
    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }

    /**
     * @return the artisanNotifier
     */
    public Artisan getArtisanNotifier() {
        return artisanNotifier;
    }

    /**
     * @param artisanNotifier
     *            the artisanNotifier to set
     */
    public void setArtisanNotifier(Artisan artisanNotifier) {
        this.artisanNotifier = artisanNotifier;
    }

    /**
     * @return the clientNotifier
     */
    public Client getClientNotifier() {
        return clientNotifier;
    }

    /**
     * @param clientNotifier
     *            the clientNotifier to set
     */
    public void setClientNotifier(Client clientNotifier) {
        this.clientNotifier = clientNotifier;
    }

    /**
     * @return the statutNotification
     */
    public StatutNotification getStatutNotification() {
        return statutNotification;
    }

    /**
     * @param statutNotification
     *            the statutNotification to set
     */
    public void setStatutNotification(StatutNotification statutNotification) {
        this.statutNotification = statutNotification;
    }

    /**
     * @return the pourQuiNotification
     */
    public TypeCompte getPourQuiNotification() {
        return pourQuiNotification;
    }

    /**
     * @param pourQuiNotification
     *            the pourQuiNotification to set
     */
    public void setPourQuiNotification(TypeCompte pourQuiNotification) {
        this.pourQuiNotification = pourQuiNotification;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.typeNotification, this.dateNotification));
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

        if (object instanceof Notification) {
            Notification other = (Notification) object;
            return Objects.equals(this.typeNotification, other.typeNotification)
                    && Objects.equals(this.dateNotification, other.dateNotification);
        }
        return false;
    }

}
