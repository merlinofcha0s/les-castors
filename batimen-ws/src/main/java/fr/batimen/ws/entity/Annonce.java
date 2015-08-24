package fr.batimen.ws.entity;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.dto.enums.TypeTravaux;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité Annonce, est utilisée pour symboliser l'annonce d'un particulier en
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
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_DEMANDEUR_LOGIN_FETCH_ARTISAN,
                query = "SELECT a.categorieMetier, a.description, a.etatAnnonce, count(art), a.hashID FROM Annonce AS a LEFT OUTER JOIN a.artisans AS art WHERE a.demandeur.login = :login AND a.etatAnnonce != 4 AND a.etatAnnonce != 1 GROUP BY a ORDER BY dateCreation ASC"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ARTISAN_LOGIN_FETCH_ARTISAN,
                query = "SELECT a.categorieMetier, a.description, a.etatAnnonce, count(art), a.hashID FROM Annonce AS a LEFT OUTER JOIN a.artisans AS art WHERE art.login = :login AND a.etatAnnonce != 4 AND a.etatAnnonce != 1 GROUP BY a ORDER BY dateCreation ASC"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_TITLE_AND_DESCRIPTION,
                query = "SELECT a FROM Annonce AS a WHERE a.description = :description AND a.demandeur.login = :login AND a.etatAnnonce != 4 AND a.etatAnnonce != 1"),
        @NamedQuery(name = QueryJPQL.NB_ANNONCE_BY_LOGIN,
                query = "SELECT count(a) FROM Annonce AS a WHERE a.demandeur.login = :login AND a.etatAnnonce != 4 AND a.etatAnnonce != 1"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID_FETCH_ARTISAN_ENTREPRISE_CLIENT_ADRESSE,
                query = "SELECT a FROM Annonce AS a LEFT OUTER JOIN FETCH a.artisans AS art LEFT OUTER JOIN FETCH art.entreprise AS ent LEFT OUTER JOIN FETCH a.adresseChantier AS adr LEFT OUTER JOIN FETCH a.demandeur AS dem LEFT OUTER JOIN FETCH a.images AS im WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID,
                query = "SELECT a FROM Annonce AS a WHERE a.hashID = :hashID AND a.etatAnnonce != 4 AND a.etatAnnonce != 1"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_ID_ADMIN, query = "SELECT a FROM Annonce AS a WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_UPDATE_NB_CONSULTATION,
                query = "UPDATE Annonce AS a set a.nbConsultation = :nbConsultation, a.dateMAJ = CURRENT_DATE WHERE a.hashID = :hashID"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_CLIENT,
                query = "UPDATE Annonce a SET a.etatAnnonce = :etatAnnonce, a.dateMAJ = CURRENT_DATE WHERE a IN (SELECT a FROM Annonce AS a WHERE a.demandeur.login  = :login AND a.hashID = :hashID)"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SUPRESS_ANNONCE_FOR_ADMIN,
                query = "UPDATE Annonce a SET a.etatAnnonce = :etatAnnonce, a.dateMAJ = CURRENT_DATE WHERE a IN (SELECT a FROM Annonce AS a WHERE a.hashID = :hashID)"),
        @NamedQuery(name = QueryJPQL.ANNONCE_SELECTION_ENTREPRISE_FOR_CLIENT,
                query = "UPDATE Annonce a SET a.entrepriseSelectionnee = (SELECT ent FROM Entreprise AS ent WHERE ent.artisan.login = :artisanLoginChoisi), a.dateMAJ = CURRENT_DATE WHERE a.hashID = :hashID AND a.demandeur.login = :login"),
        @NamedQuery(name = QueryJPQL.ANNONCE_DESACTIVE_PERIMEE,
                query = "UPDATE Annonce a SET a.etatAnnonce = 1, a.dateMAJ = CURRENT_DATE WHERE a.dateCreation < :todayMinusXDays AND a.artisans.size = :nbArtisanMax"),
        @NamedQuery(name = QueryJPQL.ANNONCE_BY_HASHID_AND_DEMANDEUR,
                query = "SELECT a FROM Annonce AS a WHERE a.hashID = :hashID AND a.demandeur.login = :loginDemandeur") })
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
    private Avis avis;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Adresse adresseChantier;
    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Artisan.class)
    @JoinTable(name = "annonce_artisan",
            joinColumns = @JoinColumn(name = "annonce_id"),
            inverseJoinColumns = @JoinColumn(name = "artisans_id"))
    private Set<Artisan> artisans = new HashSet<Artisan>();
    @OneToMany(mappedBy = "annonce",
            targetEntity = Notification.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<Notification>();
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "annonce", targetEntity = Image.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<Image>();
    @OneToMany(mappedBy = "annonce",
            targetEntity = CategorieMetier.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private Set<MotCle> motcles;

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
     * @param demandeur
     *            the demandeur to set
     */
    public void setDemandeur(Client demandeur) {
        this.demandeur = demandeur;
    }

    public Avis getAvis() {
        return avis;
    }

    public void setAvis(Avis avis) {
        this.avis = avis;
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

    public Set<MotCle> getMotcles() {
        return motcles;
    }

    public void setMotcles(Set<MotCle> motcles) {
        this.motcles = motcles;
    }

    /**
     * @return the artisans
     */
    public Set<Artisan> getArtisans() {
        return artisans;
    }

    /**
     * @param artisans
     *            the artisans to set
     */
    public void setArtisans(Set<Artisan> artisans) {
        this.artisans = artisans;
    }

    /**
     * @return the notifications
     */
    public Set<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications
     *            the notifications to set
     */
    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the hashID
     */
    public String getHashID() {
        return hashID;
    }

    /**
     * @param hashID
     *            the hashID to set
     */
    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    /**
     * @return the selHashID
     */
    public String getSelHashID() {
        return selHashID;
    }

    /**
     * @param selHashID
     *            the selHashID to set
     */
    public void setSelHashID(String selHashID) {
        this.selHashID = selHashID;
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

    /**
     * @return the images
     */
    public Set<Image> getImages() {
        return images;
    }

    /**
     * @param images
     *            the images to set
     */
    public void setImages(Set<Image> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Annonce annonce = (Annonce) o;
        return Objects.equals(id, annonce.id) &&
                Objects.equals(description, annonce.description) &&
                Objects.equals(dateCreation, annonce.dateCreation) &&
                Objects.equals(dateMAJ, annonce.dateMAJ) &&
                Objects.equals(nbConsultation, annonce.nbConsultation) &&
                Objects.equals(etatAnnonce, annonce.etatAnnonce) &&
                Objects.equals(hashID, annonce.hashID) &&
                Objects.equals(selHashID, annonce.selHashID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dateCreation, dateMAJ, nbConsultation, etatAnnonce, hashID, selHashID);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Annonce{");
        sb.append("id=").append(id);
        sb.append(", description='").append(description).append('\'');
        sb.append(", typeContact=").append(typeContact);
        sb.append(", delaiIntervention=").append(delaiIntervention);
        sb.append(", dateCreation=").append(dateCreation);
        sb.append(", dateMAJ=").append(dateMAJ);
        sb.append(", nbConsultation=").append(nbConsultation);
        sb.append(", etatAnnonce=").append(etatAnnonce);
        sb.append(", hashID='").append(hashID).append('\'');
        sb.append(", selHashID='").append(selHashID).append('\'');
        sb.append(", typeTravaux=").append(typeTravaux);
        sb.append('}');
        return sb.toString();
    }
}