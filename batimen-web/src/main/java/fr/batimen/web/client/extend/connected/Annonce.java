package fr.batimen.web.client.extend.connected;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
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
import fr.batimen.web.client.extend.member.client.MesAnnonces;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.web.client.modal.InscriptionModal;
import fr.batimen.web.client.modal.SuppressionModal;
import fr.batimen.ws.client.service.AnnonceService;

/**
 * TODO : Mettre en place les quatres actions, modifier , supprimer(OK),
 * s'inscrire, envoyer devis <br/>
 * TODO : Rajouter un check si l'artisan est deja inscrit ne pas afficher
 * "s'inscrire a l'annonce"
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
    private WebMarkupContainer containerEnteprisesInscrites;
    private WebMarkupContainer containerEntrepriseSelectionnee;
    private WebMarkupContainer containerEntreprisesGlobales;

    private InscriptionModal inscriptionModal;

    private AnnonceAffichageDTO annonceAffichageDTO;

    private ClientDTO userConnected;

    private RolesUtils roleUtils;

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
                return roleUtils.checkClientAndAdminRoles();
            }
        };

        modifierAnnonce = new Link<Void>("modifierAnnonce") {

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

        supprimerAnnonce = new AjaxLink<Void>("supprimerAnnonce") {

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
                return roleUtils.checkRoles(TypeCompte.ARTISAN);
            }
        };

        inscrireAnnonce = new AjaxLink<Void>("inscrireAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                inscriptionModal.open(target);
            }
        };

        WebMarkupContainer envoyerDevisContainer = new WebMarkupContainer("envoyerDevisContainer") {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return roleUtils.checkRoles(TypeCompte.ARTISAN);
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
                } else if (roleUtils.checkRoles(TypeCompte.ARTISAN)) {
                    for (EntrepriseDTO entreprise : annonceAffichageDTO.getEntreprises()) {
                        if (userConnected.getLogin().equals(entreprise.getArtisan().getLogin())) {
                            return true;
                        }
                    }
                } else if (roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR_MANAGER)) {
                    return true;
                } else {
                    return false;
                }
                return false;
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
        Model<String> telephoneValue = null;
        Model<String> emailValue = null;
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

        Label telephone = new Label("telephone", telephoneValue);
        SmartLinkLabel email = new SmartLinkLabel("email", emailValue);

        containerContact.add(adresse, telephone, email);
        add(containerContact);
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
            DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
            demandeAnnonceDTO.setHashID(idAnnonce);
            demandeAnnonceDTO.setLoginDemandeur(userConnected.getLogin());
            AnnonceService.inscriptionUnArtisan(demandeAnnonceDTO);
            // TODO Ecrire la logique pour afficher les infos de contacts si
            // l'inscription s'est correctement déroulée.
        }
    }
}