package fr.batimen.web.client.extend.connected;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
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

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.AnnonceSelectEntrepriseDTO;
import fr.batimen.dto.aggregate.NbConsultationDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.app.security.RolesUtils;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.LinkLabel;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.event.InscriptionArtisanEvent;
import fr.batimen.web.client.event.SuppressionOpenEvent;
import fr.batimen.web.client.extend.error.AccesInterdit;
import fr.batimen.web.client.extend.member.client.MesAnnonces;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.modal.InscriptionModal;
import fr.batimen.web.client.modal.SuppressionModal;
import fr.batimen.ws.client.service.AnnonceService;

/**
 * TODO : Action qui reste : Modifier , Envoyer devis <br/>
 * 
 * @author Casaucau Cyril
 * 
 */
public class Annonce extends MasterPage {

    private static final long serialVersionUID = -3604005728078066454L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MesAnnonces.class);

    private String idAnnonce;
    private WebMarkupContainer containerEnteprisesInscrites;
    private WebMarkupContainer containerEntrepriseSelectionnee;
    private WebMarkupContainer containerEntreprisesGlobales;
    private WebMarkupContainer containerContactMaster;
    private WebMarkupContainer containerActions;

    private WebMarkupContainer envoyerDevisContainer;

    private InscriptionModal inscriptionModal;

    private AnnonceAffichageDTO annonceAffichageDTO;

    private ClientDTO userConnected;

    private RolesUtils roleUtils;

    private Label telephone;
    private SmartLinkLabel email;

    private Model<String> telephoneValue;
    private Model<String> emailValue;

    public Annonce() {
        super("", "", "Annonce particulier", true, "img/bg_title1.jpg");
    }

    public Annonce(PageParameters params) {
        this();
        idAnnonce = params.get("idAnnonce").toString();
        roleUtils = new RolesUtils();
        loadAnnonceInfos(idAnnonce);
        updateNbConsultation();
        initComposants();
        initPopupInscription();
        initAction();
        affichageDonneesAnnonce();
        affichageEntreprisesInscrites();
        affichageContactAnnonce();
        initPopupSuppression();
        affichageEntrepriseSelectionnee();
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

        annonceAffichageDTO = AnnonceService.getAnnonceByIDForAffichage(demandeAnnonceDTO);

        if (annonceAffichageDTO.getAnnonce() == null) {
            throw new RestartResponseAtInterceptPageException(AccesInterdit.class);
        }

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

        containerActions = new WebMarkupContainer("containerActions");
        containerActions.setOutputMarkupId(true);
        containerActions.setMarkupId("containerActions");

        WebMarkupContainer modifierAnnonceContainer = new WebMarkupContainer("modifierAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return roleUtils.checkClientAndAdminRoles();
            }
        };

        Link<Void> modifierAnnonce = new Link<Void>("modifierAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // TODO Plugger la modification de l'annonce une fois page faite
            }

        };

        WebMarkupContainer supprimerAnnonceContainer = new WebMarkupContainer("supprimerAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return roleUtils.checkClientAndAdminRoles();
            }
        };

        AjaxLink<Void> supprimerAnnonce = new AjaxLink<Void>("supprimerAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(target.getPage(), Broadcast.BREADTH, new SuppressionOpenEvent(target));
            }

        };

        supprimerAnnonce.setOutputMarkupId(true);
        supprimerAnnonce.setMarkupId("supprimerAnnonce");

        WebMarkupContainer inscrireAnnonceContainer = new WebMarkupContainer("inscrireAnnonceContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return afficherInscrireAnnonce();
            }
        };

        inscrireAnnonceContainer.setMarkupId("inscrireAnnonceContainer");

        AjaxLink<Void> inscrireAnnonce = new AjaxLink<Void>("inscrireAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                inscriptionModal.open(target);
            }

        };

        inscrireAnnonce.setOutputMarkupId(true);
        inscrireAnnonce.setMarkupId("inscrireAnnonce");

        envoyerDevisContainer = new WebMarkupContainer("envoyerDevisContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return afficherEnvoyerDevisAnnonce();
            }
        };

        envoyerDevisContainer.setMarkupId("envoyerDevisContainer");
        if (!inscrireAnnonce.isVisible()) {
            envoyerDevisContainer.add(new AttributeModifier("class", "containerAction"));
        } else {
            envoyerDevisContainer.add(new AttributeModifier("class", ""));
        }

        Link<Void> envoyerDevis = new Link<Void>("envoyerDevis") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // TODO A implementer
            }

        };

        envoyerDevis.setOutputMarkupId(true);
        envoyerDevis.setMarkupId("envoyerDevis");

        modifierAnnonceContainer.add(modifierAnnonce);
        supprimerAnnonceContainer.add(supprimerAnnonce);
        inscrireAnnonceContainer.add(inscrireAnnonce);
        envoyerDevisContainer.add(envoyerDevis);

        containerActions.add(modifierAnnonceContainer, supprimerAnnonceContainer, inscrireAnnonceContainer,
                envoyerDevisContainer);

        add(containerActions);
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
        Label etatAnnonce = new Label("etatAnnonce", annonceAffichageDTO.getAnnonce().getEtatAnnonce().getType());
        Label typeContact = new Label("typeContact", annonceAffichageDTO.getAnnonce().getTypeContact().getAffichage());

        add(description, iconCategorie, categorie, sousCategorie, typeTravaux, delaiIntervention, dateCreation,
                nbConsultation, etatAnnonce, typeContact);
    }

    private void affichageEntreprisesInscrites() {
        containerEnteprisesInscrites = new WebMarkupContainer("containerEnteprisesInscrites") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return (roleUtils.checkRoles(TypeCompte.CLIENT) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR))
                        && annonceAffichageDTO.getEntrepriseSelectionnee() == null;
            }

        };

        containerEnteprisesInscrites.setOutputMarkupId(true);
        containerEnteprisesInscrites.setMarkupId("containerEnteprisesInscrites");

        containerEntreprisesGlobales = new WebMarkupContainer("containerEntreprisesGlobales");
        containerEntreprisesGlobales.setOutputMarkupId(true);
        containerEntreprisesGlobales.add(containerEnteprisesInscrites);

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

                AjaxLink<Void> linkAcceptDevis = new AjaxLink<Void>("linkAcceptDevis") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        AnnonceSelectEntrepriseDTO demandeAnnonceSelectionEntreprise = new AnnonceSelectEntrepriseDTO();
                        demandeAnnonceSelectionEntreprise
                                .setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);
                        demandeAnnonceSelectionEntreprise.setHashID(idAnnonce);
                        demandeAnnonceSelectionEntreprise.setLoginArtisanChoisi(entreprise.getArtisan().getLogin());
                        demandeAnnonceSelectionEntreprise.setLoginDemandeur(annonceAffichageDTO.getAnnonce()
                                .getLoginOwner());
                        Integer codeRetour = AnnonceService.selectOneEnterprise(demandeAnnonceSelectionEntreprise);

                        if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                            annonceAffichageDTO.setEntrepriseSelectionnee(entreprise);

                            StringBuilder messageFeedback = new StringBuilder("L'entreprise ");
                            messageFeedback.append(entreprise.getNomComplet());
                            messageFeedback.append(" a été selectionnée avec succés");
                            feedBackPanelGeneral.success(messageFeedback.toString());
                        } else {
                            feedBackPanelGeneral.error("Problème lors de la selection d'une entreprise");
                        }
                        target.add(feedBackPanelGeneral, containerEntreprisesGlobales);
                    }

                };

                linkAcceptDevis.setMarkupId("linkAcceptDevis");

                AjaxLink<Void> linkRefusDevis = new AjaxLink<Void>("linkRefusDevis") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        // TODO Appeler le service pour la deselection d'un
                        // entreprise concernant une annonce, En suspens pour le
                        // moment.
                    }

                };

                itemEntreprise.add(linkEntreprise, linkAcceptDevis, linkRefusDevis);
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

        containerEnteprisesInscrites.add(listViewEntrepriseInscrite, aucuneEntreprise);
        add(containerEntreprisesGlobales);
    }

    private void affichageEntrepriseSelectionnee() {
        containerEntrepriseSelectionnee = new WebMarkupContainer("containerEntrepriseSelectionnee") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return (roleUtils.checkRoles(TypeCompte.CLIENT) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR))
                        && annonceAffichageDTO.getEntrepriseSelectionnee() != null;
            }
        };

        containerEntrepriseSelectionnee.setOutputMarkupId(true);
        containerEntrepriseSelectionnee.setMarkupId("containerEntrepriseSelectionnee");

        containerEntreprisesGlobales.add(containerEntrepriseSelectionnee);
    }

    private void affichageContactAnnonce() {

        // Container créé uniquement dans le but de pouvoir rafraichir avec un
        // appel ajax. En effet, le container fils n'est pas visible donc pas
        // possible de la rafraichir
        containerContactMaster = new WebMarkupContainer("containerContactMaster");
        containerContactMaster.setOutputMarkupId(true);
        containerContactMaster.setMarkupId("containerContactMaster");

        WebMarkupContainer containerContact = new WebMarkupContainer("containerContact") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (roleUtils.checkRoles(TypeCompte.CLIENT)
                        && annonceAffichageDTO.getAnnonce().getLoginOwner().equals(userConnected.getLogin())) {
                    return true;
                } else if (roleUtils.checkRoles(TypeCompte.ARTISAN) && annonceAffichageDTO.getIsArtisanInscrit()) {
                    return true;
                } else if (roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR_MANAGER)) {
                    return true;
                } else {
                    return false;
                }
            }

        };

        containerContact.setOutputMarkupId(true);
        containerContact.setMarkupId("containerContact");

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

        telephone = new Label("telephone");
        email = new SmartLinkLabel("email");

        initModelPhoneAndEmailContact();

        containerContact.add(adresse, telephone, email);
        containerContactMaster.add(containerContact);
        add(containerContactMaster);
    }

    private void initModelPhoneAndEmailContact() {
        // Tout dépends si c'est un artisan qui envoi les données => le
        // webservice renvoi le téléphone et l'adresse mail.
        // Sinon c'est que c'est le client
        if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR_MANAGER)) {
            telephoneValue = new Model<String>(annonceAffichageDTO.getTelephoneClient());
            emailValue = new Model<String>(annonceAffichageDTO.getEmailClient());
        } else {
            telephoneValue = new Model<String>(userConnected.getNumeroTel());
            emailValue = new Model<String>(userConnected.getEmail());
        }

        telephone.setDefaultModel(telephoneValue);
        email.setDefaultModel(emailValue);
    }

    private void updateNbConsultation() {
        if (roleUtils.checkRoles(TypeCompte.ARTISAN)) {
            NbConsultationDTO nbConsultationDTO = new NbConsultationDTO();
            nbConsultationDTO.setHashID(idAnnonce);
            nbConsultationDTO.setNbConsultation(annonceAffichageDTO.getAnnonce().getNbConsultation());
            AnnonceService.updateNbConsultationAnnonce(nbConsultationDTO);
            annonceAffichageDTO.getAnnonce().setNbConsultation(nbConsultationDTO.getNbConsultation() + 1);
        }
    }

    private void initPopupSuppression() {
        SuppressionModal suppressionModal = new SuppressionModal("suppressionModal", idAnnonce,
                "Suppression de mon annonce", "390");
        add(suppressionModal);
    }

    private void initPopupInscription() {
        inscriptionModal = new InscriptionModal("inscriptionModal");
        add(inscriptionModal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.batimen.web.client.master.MasterPage#onEvent(org.apache.wicket.event
     * .IEvent)
     */
    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        if (event.getPayload() instanceof InscriptionArtisanEvent) {
            InscriptionArtisanEvent inscriptionArtisanEvent = (InscriptionArtisanEvent) event.getPayload();

            DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
            demandeAnnonceDTO.setHashID(idAnnonce);
            demandeAnnonceDTO.setLoginDemandeur(userConnected.getLogin());
            Integer codeRetourInscription = AnnonceService.inscriptionUnArtisan(demandeAnnonceDTO);
            if (codeRetourInscription.equals(CodeRetourService.RETOUR_OK)) {
                feedBackPanelGeneral.success("Votre inscription a été prise en compte avec succés");
            } else if (codeRetourInscription.equals(CodeRetourService.RETOUR_KO)) {
                feedBackPanelGeneral
                        .error("Problème d'enregistrement avec votre inscription, veuillez réessayer, si le problème persiste");
            } else if (codeRetourInscription.equals(CodeRetourService.ANNONCE_RETOUR_ARTISAN_DEJA_INSCRIT)) {
                feedBackPanelGeneral.error("Vous êtes dèja inscrit, vous ne pouvez pas vous inscrire deux fois");
            } else if (codeRetourInscription.equals(CodeRetourService.ANNONCE_RETOUR_QUOTA_DEVIS_ATTEINT)) {
                feedBackPanelGeneral.error("Quotas d'inscription atteint pour cette annonce");
            }

            loadAnnonceInfos(idAnnonce);
            initModelPhoneAndEmailContact();

            envoyerDevisContainer.add(new AttributeModifier("class", ""));

            inscriptionArtisanEvent.getTarget().add(feedBackPanelGeneral);
            inscriptionArtisanEvent.getTarget().add(containerActions);
            inscriptionArtisanEvent.getTarget().add(containerContactMaster);
        }
    }

    /**
     * Principalement utilisé pour l'affichage des liens dans le menu action
     * 
     * Check si l'artisan a le bon role et que l'annonce est active
     * 
     * @return true si les verifications sont OK !!
     */
    private boolean basicCheckForArtisanLink() {
        if (!roleUtils.checkRoles(TypeCompte.ARTISAN)) {
            return false;
        }
        if (annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.ACTIVE)) {
            return true;
        }
        return false;
    }

    /**
     * Check pour savoir si on affiche le lien d'inscription a l'annonce
     * 
     * @return true si le lien doit etre affiché
     */
    private boolean afficherInscrireAnnonce() {
        if (basicCheckForArtisanLink() && !annonceAffichageDTO.getIsArtisanInscrit()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check pour savoir si on affiche le lien d'envoi du devis
     * 
     * @return true si le lien doit etre affiché
     */
    private boolean afficherEnvoyerDevisAnnonce() {
        if (basicCheckForArtisanLink() && annonceAffichageDTO.getIsArtisanInscrit()) {
            return true;
        } else {
            return false;
        }
    }
}