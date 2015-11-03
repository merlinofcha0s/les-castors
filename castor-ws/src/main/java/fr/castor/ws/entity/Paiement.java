package fr.castor.ws.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Entité Paiement : symbolise les informations de paiement de l'entreprise dans
 * la base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Paiement")
public class Paiement extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -8790105697360789578L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String numeroCarte;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateExpiration;
    @Column(length = 255, nullable = false)
    private String codeSecurite;
    @OneToOne(mappedBy = "paiement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Entreprise entreprise;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Adresse adresseFacturation;

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
     * @return the numeroCarte
     */
    public String getNumeroCarte() {
        return numeroCarte;
    }

    /**
     * @param numeroCarte
     *            the numeroCarte to set
     */
    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    /**
     * @return the dateExpiration
     */
    public Date getDateExpiration() {
        return dateExpiration;
    }

    /**
     * @param dateExpiration
     *            the dateExpiration to set
     */
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    /**
     * @return the codeSecurite
     */
    public String getCodeSecurite() {
        return codeSecurite;
    }

    /**
     * @param codeSecurite
     *            the codeSecurite to set
     */
    public void setCodeSecurite(String codeSecurite) {
        this.codeSecurite = codeSecurite;
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
     * @return the adresseFacturation
     */
    public Adresse getAdresseFacturation() {
        return adresseFacturation;
    }

    /**
     * @param adresseFacturation
     *            the adresseFacturation to set
     */
    public void setAdresseFacturation(Adresse adresseFacturation) {
        this.adresseFacturation = adresseFacturation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.numeroCarte, this.dateExpiration, this.codeSecurite));
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

        if (object instanceof Paiement) {
            Paiement other = (Paiement) object;
            return Objects.equals(this.numeroCarte, other.numeroCarte)
                    && Objects.equals(this.dateExpiration, other.dateExpiration)
                    && Objects.equals(this.codeSecurite, other.codeSecurite);
        }
        return false;
    }
}
