package fr.batimen.ws.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

/**
 * Entité abstraite qui permet de factoriser le client et l'artisan
 * 
 * @author Casaucau Cyril
 * 
 */
@MappedSuperclass
public abstract class AbstractUser extends AbstractEntity {

    @Column(length = 20, nullable = false)
    protected String nom;
    @Column(length = 20, nullable = false)
    protected String prenom;
    @Column(length = 10, nullable = false)
    protected String numeroTel;
    @Column(length = 25, nullable = false)
    protected String login;
    @Column(length = 80, nullable = false)
    protected String password;
    @Column(length = 128, nullable = false)
    protected String email;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date dateInscription;
    @Column(nullable = false)
    protected Boolean isActive;
    @Column(length = 255)
    protected String cleActivation;

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the numeroTel
     */
    public String getNumeroTel() {
        return numeroTel;
    }

    /**
     * @param numeroTel
     *            the numeroTel to set
     */
    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login == null ? "" : login;
    }

    /**
     * @param login
     *            the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email == null ? "" : email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the dateInscription
     */
    public Date getDateInscription() {
        return dateInscription;
    }

    /**
     * @param dateInscription
     *            the dateInscription to set
     */
    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @return the cleActivation
     */
    public String getCleActivation() {
        return cleActivation;
    }

    /**
     * @param isActive
     *            the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @param cleActivation
     *            the cleActivation to set
     */
    public void setCleActivation(String cleActivation) {
        this.cleActivation = cleActivation;
    }

}
