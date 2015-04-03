package fr.batimen.web.client.extend.connected;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
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
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.AnnonceSelectEntrepriseDTO;
import fr.batimen.dto.aggregate.DesinscriptionAnnonceDTO;
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
import fr.batimen.web.client.event.DesinscriptionArtisanAnnonceEvent;
import fr.batimen.web.client.event.InscriptionArtisanEvent;
import fr.batimen.web.client.event.NoterArtisanEventOpen;
import fr.batimen.web.client.event.SelectionEntrepriseEvent;
import fr.batimen.web.client.event.SuppressionOpenEvent;
import fr.batimen.web.client.extend.error.AccesInterdit;
import fr.batimen.web.client.extend.error.NonTrouvee;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.modal.DesincriptionArtisanModal;
import fr.batimen.web.client.modal.InscriptionModal;
import fr.batimen.web.client.modal.NotationArtisanModal;
import fr.batimen.web.client.modal.SelectionEntrepriseModal;
import fr.batimen.web.client.modal.SuppressionModal;
import fr.batimen.ws.client.service.AnnonceServiceREST;

/**
 * TODO : Action qui reste : Modifier , Envoyer devis <br/>
 * 
 * @author Casaucau Cyril
 * 
 */
public class Annonce extends MasterPage {

    private static final long serialVersionUID = -3604005728078066454L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Annonce.class);

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    @Inject
    private Authentication authentication;

    private String idAnnonce;
    private WebMarkupContainer containerEnteprisesInscrites;
    private WebMarkupContainer containerEntrepriseSelectionnee;
    private WebMarkupContainer containerEntreprisesGlobales;
    private WebMarkupContainer containerContactMaster;
    private WebMarkupContainer containerActions;

    private WebMarkupContainer envoyerDevisContainer;

    private InscriptionModal inscriptionModal;
    private SelectionEntrepriseModal selectionEntrepriseModal;
    private DesincriptionArtisanModal desincriptionArtisanModal;
    private NotationArtisanModal notationArtisanModal;

    private AnnonceAffichageDTO annonceAffichageDTO;

    private ClientDTO userConnected;

    private RolesUtils roleUtils;

    private Label telephone;
    private Label etatAnnonce;
    private SmartLinkLabel email;

    private Model<String> telephoneValue;
    private Model<String> emailValue;
    private Model<String> nomEntrepriseSelectionnee;
    private Model<String> etatAnnonceValue;

    private ListView<EntrepriseDTO> listViewEntrepriseInscrite;

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
        initPopupSelectionEntreprise();
        initPopupSuppression();
        initPopupDesinscriptionEntreprise();
        initPopupNotationArtisan();
        initAction();
        initContainerPhoto();
        affichageDonneesAnnonce();
        affichageEntreprisesInscrites();
        affichageContactAnnonce();

        affichageEntrepriseSelectionnee();
    }

    private void loadAnnonceInfos(String idAnnonce) {
        userConnected = authentication.getCurrentUserInfo();
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(idAnnonce);
        demandeAnnonceDTO.setLoginDemandeur(userConnected.getLogin());

        for (PermissionDTO permissionDTO : userConnected.getPermissions()) {
            demandeAnnonceDTO.setTypeCompteDemandeur(permissionDTO.getTypeCompte());
        }

        annonceAffichageDTO = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);

        if (annonceAffichageDTO.getAnnonce() == null) {
            throw new RestartResponseAtInterceptPageException(AccesInterdit.class);
        }

        if (annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.SUPPRIMER)) {
            throw new RestartResponseAtInterceptPageException(NonTrouvee.class);
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

        if (annonceAffichageDTO.getAnnonce().getCategorieMetier().equals(CategorieLoader.PLOMBERIE_CODE)
                || annonceAffichageDTO.getAnnonce().getCategorieMetier().equals(CategorieLoader.ESPACE_VERT_CODE)) {
            categorie.add(new AttributeModifier("class", "labelAnnonce-icon8"));
        }

        Label sousCategorie = new Label("sousCategorie", annonceAffichageDTO.getAnnonce().getSousCategorieMetier());
        Label typeTravaux = new Label("typeTravaux", annonceAffichageDTO.getAnnonce().getTypeTravaux().getType());
        Label delaiIntervention = new Label("delaiIntervention", annonceAffichageDTO.getAnnonce()
                .getDelaiIntervention().getType());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Label dateCreation = new Label("dateCreation", sdf.format(annonceAffichageDTO.getAnnonce().getDateCreation()));
        Label nbConsultation = new Label("nbConsultation", annonceAffichageDTO.getAnnonce().getNbConsultation());

        etatAnnonceValue = new Model<String>(annonceAffichageDTO.getAnnonce().getEtatAnnonce().getType());

        etatAnnonce = new Label("etatAnnonce", etatAnnonceValue);
        etatAnnonce.setMarkupId("etatAnnonce");
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

        listViewEntrepriseInscrite = new ListView<EntrepriseDTO>("entreprises", annonceAffichageDTO.getEntreprises()) {
            /**
                     * 
                     */
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<EntrepriseDTO> itemEntreprise) {
                final EntrepriseDTO entreprise = itemEntreprise.getModelObject();

                final Model<String> nomEntrepriseModelForLbl = new Model<String>(entreprise.getNomComplet());

                Label labelEntreprise = new Label("labelEntreprise", nomEntrepriseModelForLbl);

                LinkLabel voirProfilEntreprise = new LinkLabel("voirProfilEntreprise", new Model<String>("Voir profil")) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
                        // "UTF-8")
                        // TODO A Completer quand la page entreprise sera prete
                    }
                };

                voirProfilEntreprise.setOutputMarkupId(true);

                LinkLabel downloadDevis = new LinkLabel("downloadDevis", new Model<String>("Devis indisponible")) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
                        // "UTF-8")
                        // TODO A Completer quand la page entreprise sera prete
                    }
                };

                downloadDevis.setOutputMarkupId(true);

                AjaxLink<Void> linkAcceptDevis = new AjaxLink<Void>("linkAcceptDevis") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        selectionEntrepriseModal.open(target, entreprise);
                    }

                };

                linkAcceptDevis.setOutputMarkupId(true);

                AjaxLink<Void> linkRefusDevis = new AjaxLink<Void>("linkRefusDevis") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        desincriptionArtisanModal.open(target, entreprise.getArtisan());
                    }

                };

                itemEntreprise.add(labelEntreprise, linkAcceptDevis, linkRefusDevis, voirProfilEntreprise,
                        downloadDevis);
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

        if (annonceAffichageDTO.getEntrepriseSelectionnee() != null) {
            nomEntrepriseSelectionnee = new Model<String>(annonceAffichageDTO.getEntrepriseSelectionnee()
                    .getNomComplet());
        } else {
            nomEntrepriseSelectionnee = new Model<String>("");
        }

        Label entrepriseSelectionnee = new Label("entrepriseSelectionnee", nomEntrepriseSelectionnee);

        LinkLabel voirProfilEntrepriseEntrepriseSelectionnee = new LinkLabel("voirProfilEntrepriseSelectionnee",
                new Model<String>("Voir profil")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
                // "UTF-8")
                // TODO A Completer quand la page entreprise sera prete
            }
        };

        voirProfilEntrepriseEntrepriseSelectionnee.setOutputMarkupId(true);

        LinkLabel downloadDevisEntrepriseSelectionnee = new LinkLabel("downloadDevisEntrepriseSelectionnee",
                new Model<String>("Devis indisponible")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
                // "UTF-8")
                // TODO A Completer quand la page entreprise sera prete
            }
        };

        downloadDevisEntrepriseSelectionnee.setOutputMarkupId(true);

        AjaxLink<Void> notationAnnonceParClient = new AjaxLink<Void>("notationAnnonceParClient") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                this.send(target.getPage(), Broadcast.BREADTH, new NoterArtisanEventOpen(target));
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.A_NOTER);
            }

        };

        notationAnnonceParClient.setOutputMarkupId(true);

        containerEntrepriseSelectionnee.add(entrepriseSelectionnee, voirProfilEntrepriseEntrepriseSelectionnee,
                downloadDevisEntrepriseSelectionnee, notationAnnonceParClient);
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
                return ((roleUtils.checkRoles(TypeCompte.CLIENT) && annonceAffichageDTO.getAnnonce().getLoginOwner()
                        .equals(userConnected.getLogin()))
                        || (roleUtils.checkRoles(TypeCompte.ARTISAN) && annonceAffichageDTO.getIsArtisanInscrit()) || (roleUtils
                        .checkRoles(TypeCompte.ADMINISTRATEUR)));
            }

        };

        containerContact.setOutputMarkupId(true);
        containerContact.setMarkupId("containerContact");

        WebMarkupContainer containerTelContact = new WebMarkupContainer("containerTelContact") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
                    return annonceAffichageDTO.getTelephoneClient() != null;
                } else {
                    return userConnected.getNumeroTel() != null;
                }
            }

        };

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
        containerTelContact.add(telephone);

        email = new SmartLinkLabel("email");

        initModelPhoneAndEmailContact();

        containerContact.add(adresse, email, containerTelContact);
        containerContactMaster.add(containerContact);
        add(containerContactMaster);
    }

    private void initModelPhoneAndEmailContact() {
        // Tout dépends si c'est un artisan qui envoi les données => le
        // webservice renvoi le téléphone et l'adresse mail.
        // Sinon c'est que c'est le client
        if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
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
            annonceServiceREST.updateNbConsultationAnnonce(nbConsultationDTO);
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

    private void initPopupSelectionEntreprise() {
        selectionEntrepriseModal = new SelectionEntrepriseModal("selectionEntrepriseModal");
        add(selectionEntrepriseModal);
    }

    private void initPopupDesinscriptionEntreprise() {
        desincriptionArtisanModal = new DesincriptionArtisanModal("desincriptionArtisanModal");
        add(desincriptionArtisanModal);
    }

    private void initPopupNotationArtisan() {
        notationArtisanModal = new NotationArtisanModal("notationArtisanModal");
        add(notationArtisanModal);
    }

    private void initContainerPhoto() {

        WebMarkupContainer photosContainer = new WebMarkupContainer("photosContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return !annonceAffichageDTO.getImages().isEmpty();
            }
        };

        ListView<ImageDTO> imagesView = new ListView<ImageDTO>("imagesView", annonceAffichageDTO.getImages()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ImageDTO> item) {
                final ImageDTO imageDTO = item.getModelObject();
                ExternalLink linkOnPhoto = new ExternalLink("thumbnails", imageDTO.getUrl());
                Image imageHtml = new Image("photo", new Model<String>(imageDTO.getUrl()));
                imageHtml.add(new AttributeModifier("src", imageDTO.getUrl()));
                linkOnPhoto.add(imageHtml);
                item.add(linkOnPhoto);
            }
        };

        photosContainer.add(imagesView);

        WebMarkupContainer aucunePhotoContainer = new WebMarkupContainer("aucunePhotoContainer") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getImages().isEmpty();
            }
        };

        Label aucunePhoto = new Label("aucunePhoto", "Aucune photo du chantier pour le moment :(");
        aucunePhotoContainer.add(aucunePhoto);

        add(photosContainer, aucunePhotoContainer);
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
            Integer codeRetourInscription = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);
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

        if (event.getPayload() instanceof SelectionEntrepriseEvent) {
            SelectionEntrepriseEvent selectionEntrepriseEvent = (SelectionEntrepriseEvent) event.getPayload();

            EntrepriseDTO entreprise = selectionEntrepriseEvent.getEntreprise();

            AnnonceSelectEntrepriseDTO demandeAnnonceSelectionEntreprise = new AnnonceSelectEntrepriseDTO();
            demandeAnnonceSelectionEntreprise.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);
            demandeAnnonceSelectionEntreprise.setHashID(idAnnonce);
            demandeAnnonceSelectionEntreprise.setLoginArtisanChoisi(entreprise.getArtisan().getLogin());
            demandeAnnonceSelectionEntreprise.setLoginDemandeur(annonceAffichageDTO.getAnnonce().getLoginOwner());
            Integer codeRetour = annonceServiceREST.selectOneEnterprise(demandeAnnonceSelectionEntreprise);

            if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                annonceAffichageDTO.setEntrepriseSelectionnee(entreprise);
                nomEntrepriseSelectionnee.setObject(entreprise.getNomComplet());

                annonceAffichageDTO.getAnnonce().setEtatAnnonce(EtatAnnonce.A_NOTER);
                etatAnnonceValue.setObject(annonceAffichageDTO.getAnnonce().getEtatAnnonce().getType());

                StringBuilder messageFeedback = new StringBuilder("L'entreprise ");
                messageFeedback.append(entreprise.getNomComplet());
                messageFeedback.append(" a été selectionnée avec succés");
                feedBackPanelGeneral.success(messageFeedback.toString());
            } else {
                feedBackPanelGeneral.error("Problème lors de la selection d'une entreprise");
            }

            // On set le model pour que le nom de l'entreprise soit
            // rafraichi par la requette ajax

            selectionEntrepriseEvent.getTarget().add(feedBackPanelGeneral, containerEntreprisesGlobales, etatAnnonce);
        }

        if (event.getPayload() instanceof DesinscriptionArtisanAnnonceEvent) {
            DesinscriptionArtisanAnnonceEvent desinscriptionArtisanAnnonceEvent = (DesinscriptionArtisanAnnonceEvent) event
                    .getPayload();

            ClientDTO artisanToSuppress = desinscriptionArtisanAnnonceEvent.getArtisan();

            DesinscriptionAnnonceDTO desinscriptionAnnonceDTO = new DesinscriptionAnnonceDTO();
            desinscriptionAnnonceDTO.setHashID(idAnnonce);
            desinscriptionAnnonceDTO.setLoginDemandeur(userConnected.getLogin());
            desinscriptionAnnonceDTO.setLoginArtisan(artisanToSuppress.getLogin());

            Integer codeRetourDesInscription = annonceServiceREST.desinscriptionArtisan(desinscriptionAnnonceDTO);

            if (codeRetourDesInscription.equals(CodeRetourService.RETOUR_OK)) {
                // On met a jour le model du coté du frontend sans refaire
                // d'appel au webservice pour recharger entierement l'annonce
                for (int i = 0; i < annonceAffichageDTO.getEntreprises().size(); i++) {
                    ClientDTO artisanToSuppressListView = annonceAffichageDTO.getEntreprises().get(i).getArtisan();
                    if (artisanToSuppressListView.equals(artisanToSuppress)) {
                        annonceAffichageDTO.getEntreprises().remove(i);
                    }
                }

                listViewEntrepriseInscrite.setList(annonceAffichageDTO.getEntreprises());
                feedBackPanelGeneral.success("L'entreprise a été desinscrite avec succés");
            } else {
                feedBackPanelGeneral
                        .error("Problème lors de la suppression d'une entreprise, veuillez réessayer ultérieurement");
            }

            // On set le model pour que le nom de l'entreprise soit
            // rafraichi par la requette ajax

            desinscriptionArtisanAnnonceEvent.getTarget().add(feedBackPanelGeneral, containerEntreprisesGlobales);
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
     * Check pour savoir si on affiche le lien d'inscription à l'annonce
     * 
     * @return true si le lien doit etre affiché
     */
    private boolean afficherInscrireAnnonce() {
        return (basicCheckForArtisanLink() && !annonceAffichageDTO.getIsArtisanInscrit());
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