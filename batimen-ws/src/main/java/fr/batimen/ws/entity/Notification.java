package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.StatutNotification;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.enums.TypeNotification;

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
                query = "SELECT n, artisanNotifier.login, clientNotifier.login, artisanNotifier.entreprise.nomComplet, annonce.hashID  FROM Notification AS n WHERE n.clientNotifier.login = :login AND pourQuiNotification = :typeCompte ORDER BY dateNotification ASC"),
        @NamedQuery(name = QueryJPQL.NOTIFICATION_BY_ARTISAN_LOGIN,
                query = "SELECT n, artisanNotifier.login, clientNotifier.login, artisanNotifier.entreprise.nomComplet, annonce.hashID  FROM Notification AS n WHERE n.artisanNotifier.login = :login AND pourQuiNotification = :typeCompte ORDER BY dateNotification ASC") })
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
    @ManyToOne
    @JoinColumn(name = "id_artisan")
    private Artisan artisanNotifier;
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client clientNotifier;
    @ManyToOne
    @JoinColumn(name = "id_annonce")
    private Annonce annonce;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
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
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return the clientNotifier
     */
    public Client getClientNotifier() {
        return clientNotifier;
    }

    /**
     * @param artisanNotifier
     *            the artisanNotifier to set
     */
    public void setArtisanNotifier(Artisan artisanNotifier) {
        this.artisanNotifier = artisanNotifier;
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
     * @return the pourQuiNotification
     */
    public TypeCompte getPourQuiNotification() {
        return pourQuiNotification;
    }

    /**
     * @param statutNotification
     *            the statutNotification to set
     */
    public void setStatutNotification(StatutNotification statutNotification) {
        this.statutNotification = statutNotification;
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
