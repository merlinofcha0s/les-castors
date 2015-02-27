package fr.batimen.ws.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;

/**
 * Entité Annonce, est utilisée pour symbolisé l'annonce d'un particulier en
 * base de données.
 * 
 * @author Casaucau Cyril
 * 
 */
@Entity
@Table(name = "Annonce")
@NamedQueries(value = {
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_LOGIN,
                query = "SELECT a FROM Annonce AS a WHERE a.demandeur.login = :login AND a.etatAnnonce != 4"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_LOGIN_FETCH_ARTISAN,
                query = "SELECT a.categorieMetier, a.description, a.etatAnnonce, count(art) FROM Annonce AS a LEFT OUTER JOIN a.artisans AS art WHERE a.demandeur.login = :login AND a.etatAnnonce != 4 GROUP BY a ORDER BY dateCreation ASC"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_TITLE_AND_DESCRIPTION,
                query = "SELECT a FROM Annonce AS a WHERE a.description = :description AND a.demandeur.login = :login AND a.etatAnnonce != 4"),
        @NamedQuery(name = QueryJPQL.NB_ANNONCE_BY_LOGIN,
                query = "SELECT count(a) FROM Annonce AS a WHERE a.demandeur.login = :login AND a.etatAnnonce != 4"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID_FETCH_ARTISAN_ENTREPRISE_CLIENT_ADRESSE,
                query = "SELECT a FROM Annonce AS a LEFT OUTER JOIN FETCH a.artisans AS art LEFT OUTER JOIN FETCH art.entreprise AS ent LEFT OUTER JOIN FETCH a.adresseChantier AS adr LEFT OUTER JOIN FETCH a.demandeur AS dem WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID,
                query = "SELECT a FROM Annonce AS a WHERE a.hashID = :hashID AND a.etatAnnonce != 4"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID_ADMIN, query = "SELECT a FROM Annonce AS a WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_UPDATE_NB_CONSULTATION,
                query = "UPDATE Annonce AS a set a.nbConsultation = :nbConsultation WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_CLIENT,
                query = "UPDATE Annonce a SET a.etatAnnonce = :etatAnnonce WHERE a IN (SELECT a FROM Annonce AS a WHERE a.demandeur.login  = :login AND a.hashID = :hashID)"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_ADMIN,
                query = "UPDATE Annonce a SET a.etatAnnonce = :etatAnnonce WHERE a IN (SELECT a FROM Annonce AS a WHERE a.hashID = :hashID)"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SELECTION_ENTREPRISE_FOR_CLIENT,
                query = "UPDATE Annonce a SET a.entrepriseSelectionnee = (SELECT ent FROM Entreprise AS ent WHERE ent.artisan.login = :artisanLoginChoisi) WHERE a.hashID = :hashID AND a.demandeur.login = :login"),
        @NamedQuery(name = QueryJPQL.ANNONCE_DESACTIVE_PERIMEE,
                query = "UPDATE Annonce a SET a.etatAnnonce = 4, a.dateMAJ = CURRENT_DATE WHERE a.dateCreation > :todayMinusXDays AND COUNT(a.artisans) = :nbArtisanMax") })
public class Annonce extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 3160372354800747789L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500, nullable = false)
    private String description;
    @Column(nullable = false)
    private TypeContact typeContact;
    @Column(nullable = false)
    private DelaiIntervention delaiIntervention;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateMAJ;
    @Column(nullable = false)
    private Integer nbConsultation;
    @Column(length = 255, nullable = true)
    private String photo;
    @Column(nullable = false)
    private Short categorieMetier;
    @Column(nullable = false)
    private String sousCategorieMetier;
    @Column(nullable = false)
    private EtatAnnonce etatAnnonce;
    @Column(nullable = false)
    private String hashID;
    @Column(nullable = false)
    private String selHashID;
    @Column(nullable = false)
    private TypeTravaux typeTravaux;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_selectionnee_fk")
    private Entreprise entrepriseSelectionnee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandeur_fk")
    private Client demandeur;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Notation notationAnnonce;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Adresse adresseChantier;
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Artisan.class)
    @JoinTable(name = "annonce_artisan",
            joinColumns = @JoinColumn(name = "annonce_id"),
            inverseJoinColumns = @JoinColumn(name = "artisans_id"))
    private List<Artisan> artisans = new ArrayList<Artisan>();
    @OneToMany(mappedBy = "annonce",
            targetEntity = Notification.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<Notification>();

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the typeContact
     */
    public TypeContact getTypeContact() {
        return typeContact;
    }

    /**
     * @param typeContact
     *            the typeContact to set
     */
    public void setTypeContact(TypeContact typeContact) {
        this.typeContact = typeContact;
    }

    /**
     * @return the delaiIntervention
     */
    public DelaiIntervention getDelaiIntervention() {
        return delaiIntervention;
    }

    /**
     * @param delaiIntervention
     *            the delaiIntervention to set
     */
    public void setDelaiIntervention(DelaiIntervention delaiIntervention) {
        this.delaiIntervention = delaiIntervention;
    }

    /**
     * @return the dateCreation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @return the dateMAJ
     */
    public Date getDateMAJ() {
        return dateMAJ;
    }

    /**
     * @param dateMAJ
     *            the dateMAJ to set
     */
    public void setDateMAJ(Date dateMAJ) {
        this.dateMAJ = dateMAJ;
    }

    /**
     * @return the nbConsultation
     */
    public Integer getNbConsultation() {
        return nbConsultation;
    }

    /**
     * @param nbConsultation
     *            the nbConsultation to set
     */
    public void setNbConsultation(Integer nbConsultation) {
        this.nbConsultation = nbConsultation;
    }

    /**
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo
     *            the photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return the etatAnnonce
     */
    public EtatAnnonce getEtatAnnonce() {
        return etatAnnonce;
    }

    /**
     * @param etatAnnonce
     *            the etatAnnonce to set
     */
    public void setEtatAnnonce(EtatAnnonce etatAnnonce) {
        this.etatAnnonce = etatAnnonce;
    }

    /**
     * @return the demandeur
     */
    public Client getDemandeur() {
        return demandeur;
    }

    /**
     * @return the notationAnnonce
     */
    public Notation getNotationAnnonce() {
        return notationAnnonce;
    }

    /**
     * @param notationAnnonce
     *            the notationAnnonce to set
     */
    public void setNotationAnnonce(Notation notationAnnonce) {
        this.notationAnnonce = notationAnnonce;
    }

    /**
     * @param demandeur
     *            the demandeur to set
     */
    public void setDemandeur(Client demandeur) {
        this.demandeur = demandeur;
    }

    /**
     * @return the adresseChantier
     */
    public Adresse getAdresseChantier() {
        return adresseChantier;
    }

    /**
     * @param adresseChantier
     *            the adresseChantier to set
     */
    public void setAdresseChantier(Adresse adresseChantier) {
        this.adresseChantier = adresseChantier;
    }

    /**
     * @return the categorieMetier
     */
    public Short getCategorieMetier() {
        return categorieMetier;
    }

    /**
     * @return the sousCategorieMetier
     */
    public String getSousCategorieMetier() {
        return sousCategorieMetier;
    }

    /**
     * @param categorieMetier
     *            the categorieMetier to set
     */
    public void setCategorieMetier(Short categorieMetier) {
        this.categorieMetier = categorieMetier;
    }

    /**
     * @param sousCategorieMetier
     *            the sousCategorieMetier to set
     */
    public void setSousCategorieMetier(String sousCategorieMetier) {
        this.sousCategorieMetier = sousCategorieMetier;
    }

    /**
     * @return the artisans
     */
    public List<Artisan> getArtisans() {
        return artisans;
    }

    /**
     * @param artisans
     *            the artisans to set
     */
    public void setArtisans(List<Artisan> artisans) {
        this.artisans = artisans;
    }

    /**
     * @return the notifications
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications
     *            the notifications to set
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the hashID
     */
    public String getHashID() {
        return hashID;
    }

    /**
     * @return the selHashID
     */
    public String getSelHashID() {
        return selHashID;
    }

    /**
     * @param hashID
     *            the hashID to set
     */
    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    /**
     * @return the typeTravaux
     */
    public TypeTravaux getTypeTravaux() {
        return typeTravaux;
    }

    /**
     * @param typeTravaux
     *            the typeTravaux to set
     */
    public void setTypeTravaux(TypeTravaux typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    /**
     * @param selHashID
     *            the selHashID to set
     */
    public void setSelHashID(String selHashID) {
        this.selHashID = selHashID;
    }

    /**
     * @return the entrepriseSelectionnee
     */
    public Entreprise getEntrepriseSelectionnee() {
        return entrepriseSelectionnee;
    }

    /**
     * @param entrepriseSelectionnee
     *            the entrepriseSelectionnee to set
     */
    public void setEntrepriseSelectionnee(Entreprise entrepriseSelectionnee) {
        this.entrepriseSelectionnee = entrepriseSelectionnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.description, this.categorieMetier, this.sousCategorieMetier,
                this.dateCreation));
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

        if (object instanceof Annonce) {
            Annonce other = (Annonce) object;
            return Objects.equals(this.description, other.description)
                    && Objects.equals(this.categorieMetier, other.categorieMetier)
                    && Objects.equals(this.sousCategorieMetier, other.sousCategorieMetier)
                    && Objects.equals(this.dateCreation, other.dateCreation);
        }
        return false;
    }
}
