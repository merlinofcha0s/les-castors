package fr.batimen.web.client.extend.connected;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.LinkLabel;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.member.client.MesAnnonces;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.AnnonceService;

/**
 * TODO Terminer de brancher les contacts, faire la couche de sécurité TODO :
 * Mettre un bouton "Selectionner cet artisan" sur l'annonce ?
 * 
 * @author Casaucau Cyril
 * 
 */
public class Annonce extends MasterPage {

    private static final long serialVersionUID = -3604005728078066454L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MesAnnonces.class);

    private String idAnnonce;
    private Link<Void> modifierAnnonce;
    private AjaxLink<Void> supprimerAnnonce;
    private AjaxLink<Void> inscrireAnnonce;
    private Link<Void> envoyerDevis;

    private AnnonceAffichageDTO annonceAffichageDTO;

    private Boolean modeInscris;

    private ClientDTO userConnected;

    public Annonce() {
        super("", "", "Annonce particulier", true, "img/bg_title1.jpg");
    }

    public Annonce(PageParameters params) {
        this();
        idAnnonce = params.get("idAnnonce").toString();
        loadAnnonceInfos(idAnnonce);
        initComposants();
        initAction();
        affichageDonneesAnnonce();
        affichageEntreprisesInscrites();
        affichageContactAnnonce();
    }

    private void loadAnnonceInfos(String idAnnonce) {
        Authentication authentication = new Authentication();
        userConnected = authentication.getCurrentUserInfo();
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(idAnnonce.toString());
        demandeAnnonceDTO.setLoginDemandeur(userConnected.getLogin());

        for (PermissionDTO permissionDTO : userConnected.getPermissions()) {
            demandeAnnonceDTO.setTypeCompteDemandeur(permissionDTO.getTypeCompte());
        }

        annonceAffichageDTO = AnnonceService.getAnnonceByID(demandeAnnonceDTO);

    }

    private void initComposants() {
        Profil profil = new Profil("profil");

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil);
        this.add(contactezNous);
        this.add(commentaire);
    }

    private void initAction() {

        WebMarkupContainer modifierAnnonceContainer = new WebMarkupContainer("modifierAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return checkClientAndAdminRoles();
            }
        };

        modifierAnnonce = new Link<Void>("modifierAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // TODO Pluger la modification de l'annonce une fois page faite
            }

        };

        WebMarkupContainer supprimerAnnonceContainer = new WebMarkupContainer("supprimerAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return checkClientAndAdminRoles();
            }
        };

        supprimerAnnonce = new AjaxLink<Void>("supprimerAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.setResponsePage(MesAnnonces.class);
            }

        };

        WebMarkupContainer inscrireAnnonceContainer = new WebMarkupContainer("inscrireAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                try {
                    SecurityUtils.getSubject().checkRole(TypeCompte.ARTISAN.getRole());
                    return true;
                } catch (AuthorizationException ae) {
                    return false;
                }
            }
        };

        inscrireAnnonce = new AjaxLink<Void>("inscrireAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                // TODO Auto-generated method stub
            }
        };

        WebMarkupContainer envoyerDevisContainer = new WebMarkupContainer("envoyerDevisContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                try {
                    SecurityUtils.getSubject().checkRole(TypeCompte.ARTISAN.getRole());
                    return true;
                } catch (AuthorizationException ae) {
                    return false;
                }
            }
        };

        envoyerDevis = new Link<Void>("envoyerDevis") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // TODO A implementer
            }

        };

        modifierAnnonceContainer.add(modifierAnnonce);
        supprimerAnnonceContainer.add(supprimerAnnonce);
        inscrireAnnonceContainer.add(inscrireAnnonce);
        envoyerDevisContainer.add(envoyerDevis);

        add(modifierAnnonceContainer, supprimerAnnonceContainer, inscrireAnnonceContainer, envoyerDevisContainer);
    }

    private void affichageDonneesAnnonce() {
        Label description = new Label("description", annonceAffichageDTO.getAnnonce().getDescription());

        WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
        StringBuilder classCssIcon = new StringBuilder("glyphAnnonce");
        classCssIcon.append(" ").append(
                CategorieLoader.getIconForCategorie(annonceAffichageDTO.getAnnonce().getCategorieMetier()));
        iconCategorie.add(new AttributeModifier("class", classCssIcon.toString()));

        Label categorie = new Label("categorie", CategorieLoader.getCategorieByCode(annonceAffichageDTO.getAnnonce()
                .getCategorieMetier()));
        Label sousCategorie = new Label("sousCategorie", annonceAffichageDTO.getAnnonce().getSousCategorieMetier());
        Label typeTravaux = new Label("typeTravaux", annonceAffichageDTO.getAnnonce().getTypeTravaux().getType());
        Label delaiIntervention = new Label("delaiIntervention", annonceAffichageDTO.getAnnonce()
                .getDelaiIntervention().getType());
        SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/yyyy");
        Label dateCreation = new Label("dateCreation", sdf.format(annonceAffichageDTO.getAnnonce().getDateCreation()));
        Label nbConsultation = new Label("nbConsultation", annonceAffichageDTO.getAnnonce().getNbConsultation());

        add(description, iconCategorie, categorie, sousCategorie, typeTravaux, delaiIntervention, dateCreation,
                nbConsultation);
    }

    private void affichageEntreprisesInscrites() {
        ListView<EntrepriseDTO> listViewEntrepriseInscrite = new ListView<EntrepriseDTO>("entreprises",
                annonceAffichageDTO.getEntreprises()) {
            /**
                     * 
                     */
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<EntrepriseDTO> itemEntreprise) {
                final EntrepriseDTO entreprise = itemEntreprise.getModelObject();

                final Model<String> nomEntrepriseModelForLbl = new Model<String>(entreprise.getNomComplet());

                LinkLabel linkEntreprise = new LinkLabel("linkEntreprise", nomEntrepriseModelForLbl) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
                        // "UTF-8")
                        // TODO A Completer quand la page entreprise sera prete
                    }
                };

                itemEntreprise.add(linkEntreprise);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                try {
                    SecurityUtils.getSubject().checkRole(TypeCompte.ARTISAN.getRole());
                    return false;
                } catch (AuthorizationException ae) {
                    return true;
                }
            }
        };

        Label aucuneEntreprise = new Label("aucuneEntreprise", "Aucune entreprise ne s'est inscrite pour le moment") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getEntreprises().isEmpty();
            }
        };

        add(listViewEntrepriseInscrite, aucuneEntreprise);
    }

    private void affichageContactAnnonce() {

        String complementAdresse = annonceAffichageDTO.getAdresse().getComplementAdresse();

        StringBuilder adresseComplete = new StringBuilder(annonceAffichageDTO.getAdresse().getAdresse());
        adresseComplete.append(" ");

        if (!complementAdresse.isEmpty()) {
            adresseComplete.append(complementAdresse).append(" ");
        }

        adresseComplete.append(annonceAffichageDTO.getAdresse().getCodePostal()).append(" ")
                .append(annonceAffichageDTO.getAdresse().getVille()).append(" ")
                .append(annonceAffichageDTO.getAdresse().getDepartement());

        Label adresse = new Label("adresse", adresseComplete.toString());
        Model<String> telephoneValue = null;
        Model<String> emailValue = null;
        // Tout dépends si c'est un artisan qui envoi les données => le
        // webservice renvoi le téléphone et l'adresse mail.
        // Sinon c'est que c'est le client
        try {
            SecurityUtils.getSubject().checkRoles(TypeCompte.ARTISAN.getRole());
            telephoneValue = new Model<String>(annonceAffichageDTO.getTelephoneClient());
            emailValue = new Model<String>(annonceAffichageDTO.getEmailClient());
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(SecurityUtils.getSubject().getPrincipal() + " ne possede pas le role : "
                        + TypeCompte.ARTISAN.getRole());
            }
        }
        try {
            SecurityUtils.getSubject().checkRoles(TypeCompte.CLIENT.getRole());
            telephoneValue = new Model<String>(userConnected.getNumeroTel());
            emailValue = new Model<String>(userConnected.getEmail());
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(SecurityUtils.getSubject().getPrincipal() + " ne possede pas le role : "
                        + TypeCompte.CLIENT.getRole());
            }
        }

        Label telephone = new Label("telephone", telephoneValue);
        SmartLinkLabel email = new SmartLinkLabel("email", emailValue);

        add(adresse, telephone, email);

    }

    private Boolean checkClientAndAdminRoles() {
        try {
            SecurityUtils.getSubject().checkRoles(TypeCompte.CLIENT.getRole());
            return Boolean.TRUE;
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(SecurityUtils.getSubject().getPrincipal() + " ne possede pas le role : "
                        + TypeCompte.CLIENT.getRole());
            }
        }

        try {
            SecurityUtils.getSubject().checkRoles(TypeCompte.ADMINISTRATEUR_MANAGER.getRole());
            return Boolean.TRUE;
        } catch (AuthorizationException ae) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(SecurityUtils.getSubject().getPrincipal() + " ne possede pas le role : "
                        + TypeCompte.ADMINISTRATEUR_MANAGER.getRole());
            }
        }
        return Boolean.FALSE;
    }
}