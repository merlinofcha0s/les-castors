package fr.castor.web.client.extend.connected;

import fr.castor.core.constant.CodeRetourService;
import fr.castor.dto.*;
import fr.castor.dto.aggregate.*;
import fr.castor.dto.constant.Categorie;
import fr.castor.dto.enums.EtatAnnonce;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.enums.TypeContact;
import fr.castor.web.app.enums.FeedbackMessageLevel;
import fr.castor.web.app.constants.ParamsConstant;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.app.security.RolesUtils;
import fr.castor.web.client.component.*;
import fr.castor.web.client.event.*;
import fr.castor.web.client.extend.error.AccesInterdit;
import fr.castor.web.client.extend.error.NonTrouvee;
import fr.castor.web.client.extend.member.client.ModifierAnnonce;
import fr.castor.web.client.master.MasterPage;
import fr.castor.web.client.modal.*;
import fr.castor.ws.client.service.AnnonceServiceREST;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Page d'affichage d'une annonce .
 *
 * @author Casaucau Cyril
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
    private WebMarkupContainer notationAnnonceParClientContainer;
    private WebMarkupContainer containerPopupAvisArtisan;
    private WebMarkupContainer modifierAnnonceContainer;
    private WebMarkupContainer supprimerAnnonceContainer;

    private WebMarkupContainer envoyerDevisContainer;

    private InscriptionModal inscriptionModal;
    private SelectionEntrepriseModal selectionEntrepriseModal;
    private DesincriptionArtisanModal desincriptionArtisanModal;
    private DonnerAvisArtisanModal donnerAvisArtisanModal;

    private AnnonceAffichageDTO annonceAffichageDTO;

    private ClientDTO userConnected;

    private RolesUtils roleUtils;

    private Label telephone;
    private Label etatAnnonce;
    private SmartLinkLabel email;

    private LoadableDetachableModel<String> telephoneValue;
    private LoadableDetachableModel<String> emailValue;
    private LoadableDetachableModel<String> nomEntrepriseSelectionnee;
    private LoadableDetachableModel<String> etatAnnonceValue;

    private ListView<EntrepriseDTO> listViewEntrepriseInscrite;

    public Annonce() {
        super("", "", "Annonce particulier", true, "img/bg_title1.jpg");
    }

    public Annonce(PageParameters params) {
        this();
        idAnnonce = params.get(ParamsConstant.ID_ANNONCE_PARAM).toString();
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

        if (params.getNamedKeys().contains(ParamsConstant.IS_MODIF_PARAM)) {
            String modifOK = params.get(ParamsConstant.IS_MODIF_PARAM).toString();
            if (!modifOK.isEmpty() && modifOK.equals("OK")) {
                feedBackPanelGeneral.sendMessage("Votre annonce a été modifiée avec succés !", FeedbackMessageLevel.SUCCESS);
            }
        }
    }

    private void loadAnnonceInfos(String idAnnonce) {
        userConnected = authentication.getCurrentUserInfo();
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setId(idAnnonce);
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
        Profil profil = new Profil("profil", false);

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

        modifierAnnonceContainer = new WebMarkupContainer("modifierAnnonceContainer") {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return roleUtils.checkClientAndAdminRoles() && !(annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.DESACTIVE)
                        || annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.DONNER_AVIS)
                        || annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.SUPPRIMER)
                        || annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.TERMINER));
            }
        };

        modifierAnnonceContainer.setOutputMarkupId(true);

        Link<Void> modifierAnnonce = new Link<Void>("modifierAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                ModifierAnnonce modifierAnnoncePage = new ModifierAnnonce(idAnnonce, annonceAffichageDTO);
                this.setResponsePage(modifierAnnoncePage);
            }

        };

        supprimerAnnonceContainer = new WebMarkupContainer("supprimerAnnonceContainer") {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return roleUtils.checkClientAndAdminRoles();
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (!modifierAnnonceContainer.isVisible()) {
                    tag.remove("class");
                } else {
                    tag.put("class", "containerAction");
                }
            }
        };

        supprimerAnnonceContainer.setOutputMarkupId(true);

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

        Set<Short> categories = new HashSet<>();

        for (MotCleDTO motCle : annonceAffichageDTO.getAnnonce().getMotCles()) {
            for (CategorieMetierDTO categorieMetier : motCle.getCategoriesMetier()) {
                categories.add(categorieMetier.getCategorieMetier());
            }
        }

        List<Short> categorieList = new ArrayList<>(categories);

        ListView<Short> categorieRepeater = new ListView<Short>("categorieRepeater", categorieList) {
            @Override
            protected void populateItem(ListItem<Short> item) {
                StringBuilder classCssIcon = new StringBuilder("glyphAnnonce");
                classCssIcon.append(" ").append(
                        Categorie.getIcon(item.getModelObject()));
                WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
                iconCategorie.add(new AttributeModifier("class", classCssIcon.toString()));

                Label categorie = new Label("categorie", Categorie.getNameByCode(item.getModelObject()));

                if (item.getModelObject().equals(Categorie.PLOMBERIE_CODE)
                        || item.getModelObject().equals(Categorie.ESPACE_VERT_CODE)) {
                    categorie.add(new AttributeModifier("class", "labelAnnonce-icon8"));
                }

                item.add(categorie, iconCategorie);
            }
        };

        String motcles = annonceAffichageDTO.getAnnonce().getMotCles().stream().map(motCle -> motCle.getMotCle()).collect(Collectors.joining(", "));

        Label motClesLbl = new Label("motClesLbl", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return motcles;
            }
        });
        Label typeTravaux = new Label("typeTravaux", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return annonceAffichageDTO.getAnnonce().getTypeTravaux().getText();
            }
        });
        Label delaiIntervention = new Label("delaiIntervention", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return  annonceAffichageDTO.getAnnonce().getDelaiIntervention().getText();
            }
        });
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Label dateCreation = new Label("dateCreation", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return sdf.format(annonceAffichageDTO.getAnnonce().getDateCreation());
            }
        });
        Label nbConsultation = new Label("nbConsultation", new LoadableDetachableModel<Integer>() {
            @Override
            protected Integer load() {
                return annonceAffichageDTO.getAnnonce().getNbConsultation();
            }
        });

        etatAnnonceValue = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return annonceAffichageDTO.getAnnonce().getEtatAnnonce().getType();
            }
        };

        etatAnnonce = new Label("etatAnnonce", etatAnnonceValue);
        etatAnnonce.setMarkupId("etatAnnonce");
        Label typeContact = new Label("typeContact", annonceAffichageDTO.getAnnonce().getTypeContact().getAffichage());

        add(description, categorieRepeater, motClesLbl, typeTravaux, delaiIntervention, dateCreation,
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
                return visibilityEntreprisesIncritesField();
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

                final LoadableDetachableModel<String> nomEntrepriseModelForLbl = new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        return entreprise.getNomComplet();
                    }
                };

                Label labelEntreprise = new Label("labelEntreprise", nomEntrepriseModelForLbl);

                LinkLabel voirProfilEntreprise = new LinkLabel("voirProfilEntreprise", new Model<>("Voir profil")) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add(ParamsConstant.ID_ENTREPRISE_PARAM, entreprise.getSiret());
                        this.setResponsePage(Entreprise.class, params);
                    }
                };

                voirProfilEntreprise.setOutputMarkupId(true);

                LinkLabel downloadDevis = new LinkLabel("downloadDevis", new Model<>("Devis indisponible")) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        //TODO : A implementer
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

    private boolean visibilityEntreprisesIncritesField() {
        return (roleUtils.checkRoles(TypeCompte.CLIENT) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR))
                && annonceAffichageDTO.getEntrepriseSelectionnee() == null;
    }

    private boolean visibilityEntrepriseSelectionneeField() {
        return (roleUtils.checkRoles(TypeCompte.CLIENT) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR))
                && annonceAffichageDTO.getEntrepriseSelectionnee() != null;
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
                return visibilityEntrepriseSelectionneeField();
            }
        };

        containerEntrepriseSelectionnee.setOutputMarkupId(true);
        containerEntrepriseSelectionnee.setMarkupId("containerEntrepriseSelectionnee");

        nomEntrepriseSelectionnee = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (annonceAffichageDTO.getEntrepriseSelectionnee() != null) {
                    return annonceAffichageDTO.getEntrepriseSelectionnee()
                            .getNomComplet();
                } else {
                    return "";
                }
            }
        };

        Label entrepriseSelectionnee = new Label("entrepriseSelectionnee", nomEntrepriseSelectionnee);

        LinkLabel voirProfilEntrepriseEntrepriseSelectionnee = new LinkLabel("voirProfilEntrepriseSelectionnee",
                new Model<>("Voir profil")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                PageParameters params = new PageParameters();
                params.add(ParamsConstant.ID_ENTREPRISE_PARAM, annonceAffichageDTO.getEntrepriseSelectionnee().getSiret());
                this.setResponsePage(Entreprise.class, params);
            }
        };

        voirProfilEntrepriseEntrepriseSelectionnee.setOutputMarkupId(true);

        LinkLabel downloadDevisEntrepriseSelectionnee = new LinkLabel("downloadDevisEntrepriseSelectionnee",
                new Model<String>("Devis indisponible")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
            }
        };

        downloadDevisEntrepriseSelectionnee.setOutputMarkupId(true);

        AjaxLink<Void> notationAnnonceParClient = new AjaxLink<Void>("notationAnnonceParClient") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (feedBackPanelGeneral.hasFeedbackMessage()) {
                    feedBackPanelGeneral.getFeedbackMessages().clear();
                }
                target.add(feedBackPanelGeneral);
                target.add(containerPopupAvisArtisan);
                this.send(target.getPage(), Broadcast.BREADTH, new NoterArtisanEventOpen(target));
            }

        };

        notationAnnonceParClientContainer = new WebMarkupContainer("notationAnnonceParClientContainer") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.DONNER_AVIS);
            }
        };

        notationAnnonceParClientContainer.add(notationAnnonceParClient);
        notationAnnonceParClientContainer.setOutputMarkupId(true);

        containerEntrepriseSelectionnee.add(entrepriseSelectionnee, voirProfilEntrepriseEntrepriseSelectionnee,
                downloadDevisEntrepriseSelectionnee, notationAnnonceParClientContainer);
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
                return (roleUtils.checkRoles(TypeCompte.CLIENT) && annonceAffichageDTO.getAnnonce().getLoginOwner()
                        .equals(userConnected.getLogin())
                        || roleUtils.checkRoles(TypeCompte.ARTISAN) && annonceAffichageDTO.getIsArtisanInscrit()) || (roleUtils
                        .checkRoles(TypeCompte.ADMINISTRATEUR));
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
                if (annonceAffichageDTO.getAnnonce().getTypeContact().equals(TypeContact.TELEPHONE)) {
                    if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
                        return annonceAffichageDTO.getTelephoneClient() != null;
                    } else {
                        return userConnected.getNumeroTel() != null;
                    }
                } else {
                    return false;
                }
            }
        };

        WebMarkupContainer containerEmailContact = new WebMarkupContainer("containerEmailContact") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getAnnonce().getTypeContact().equals(TypeContact.EMAIL);
            }
        };

        String adresseValeur = annonceAffichageDTO.getAdresse().getAdresse();
        String complementAdresse = annonceAffichageDTO.getAdresse().getComplementAdresse();

        StringBuilder adresseComplete = new StringBuilder();

        if (adresseValeur != null && !adresseValeur.isEmpty()) {
            adresseComplete.append(annonceAffichageDTO.getAdresse().getAdresse());
        }

        adresseComplete.append(" ");

        if (complementAdresse != null && !complementAdresse.isEmpty()) {
            adresseComplete.append(complementAdresse).append(" ");
        }

        adresseComplete.append(annonceAffichageDTO.getAdresse().getCodePostal()).append(" ")
                .append(annonceAffichageDTO.getAdresse().getVille()).append(" ")
                .append(annonceAffichageDTO.getAdresse().getDepartement());

        Label adresse = new Label("adresse", adresseComplete.toString());

        initModelPhoneAndEmailContact();

        telephone = new Label("telephone", telephoneValue);
        containerTelContact.add(telephone);

        email = new SmartLinkLabel("email", emailValue);
        containerEmailContact.add(email);

        containerContact.add(adresse, containerEmailContact, containerTelContact);
        containerContactMaster.add(containerContact);
        add(containerContactMaster);
    }

    private void initModelPhoneAndEmailContact() {
        // Tout dépends si c'est un artisan qui envoi les données => le
        // webservice renvoi le téléphone et l'adresse mail.
        // Sinon c'est que c'est le client
        telephoneValue = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
                    return annonceAffichageDTO.getTelephoneClient();
                } else {
                    return userConnected.getNumeroTel();
                }
            }
        };

        emailValue = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (roleUtils.checkRoles(TypeCompte.ARTISAN) || roleUtils.checkRoles(TypeCompte.ADMINISTRATEUR)) {
                    return annonceAffichageDTO.getEmailClient();
                } else {
                    return userConnected.getEmail();
                }
            }
        };
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
                "Suppression de mon annonce", "390") {
            /**
             * Gets whether this component and any children are visible.
             * <p/>
             * WARNING: this method can be called multiple times during a request. If you override this
             * method, it is a good idea to keep it cheap in terms of processing. Alternatively, you can
             * call {@link #setVisible(boolean)}.
             * <p/>
             *
             * @return True if component and any children are visible
             */
            @Override
            public boolean isVisible() {
                return roleUtils.checkClientAndAdminRoles();
            }
        };
        add(suppressionModal);
    }

    private void initPopupInscription() {
        inscriptionModal = new InscriptionModal("inscriptionModal") {

            @Override
            public boolean isVisible() {
                return afficherInscrireAnnonce();
            }
        };
        add(inscriptionModal);
    }

    private void initPopupSelectionEntreprise() {
        selectionEntrepriseModal = new SelectionEntrepriseModal("selectionEntrepriseModal") {
            @Override
            public boolean isVisible() {
                return visibilityEntreprisesIncritesField();
            }
        };
        add(selectionEntrepriseModal);
    }

    private void initPopupDesinscriptionEntreprise() {
        desincriptionArtisanModal = new DesincriptionArtisanModal("desincriptionArtisanModal") {
            @Override
            public boolean isVisible() {
                return visibilityEntreprisesIncritesField();
            }
        };
        add(desincriptionArtisanModal);
    }

    private void initPopupNotationArtisan() {
        containerPopupAvisArtisan = new WebMarkupContainer("containerPopupAvisArtisan");
        containerPopupAvisArtisan.setOutputMarkupId(true);
        donnerAvisArtisanModal = new DonnerAvisArtisanModal("donnerAvisArtisanModal") {
            @Override
            public boolean isVisible() {
                return annonceAffichageDTO.getAnnonce().getEtatAnnonce().equals(EtatAnnonce.DONNER_AVIS) && visibilityEntrepriseSelectionneeField();
            }
        };

        containerPopupAvisArtisan.add(donnerAvisArtisanModal);
        add(containerPopupAvisArtisan);
    }

    private void initContainerPhoto() {
        PhotosContainer photosContainer = new PhotosContainer("containerPhotos", annonceAffichageDTO.getImages(), "Photos du chantier", "h2", false);
        add(photosContainer);
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
            demandeAnnonceDTO.setId(idAnnonce);
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
            demandeAnnonceSelectionEntreprise.setId(idAnnonce);
            demandeAnnonceSelectionEntreprise.setLoginArtisanChoisi(entreprise.getArtisan().getLogin());
            demandeAnnonceSelectionEntreprise.setLoginDemandeur(annonceAffichageDTO.getAnnonce().getLoginOwner());
            Integer codeRetour = annonceServiceREST.selectOneEnterprise(demandeAnnonceSelectionEntreprise);

            if (codeRetour.equals(CodeRetourService.RETOUR_OK)) {
                annonceAffichageDTO.setEntrepriseSelectionnee(entreprise);
                nomEntrepriseSelectionnee.setObject(entreprise.getNomComplet());

                annonceAffichageDTO.getAnnonce().setEtatAnnonce(EtatAnnonce.DONNER_AVIS);
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

            selectionEntrepriseEvent.getTarget().add(feedBackPanelGeneral, containerEntreprisesGlobales, etatAnnonce, containerPopupAvisArtisan, modifierAnnonceContainer, supprimerAnnonceContainer);
        }

        if (event.getPayload() instanceof DesinscriptionArtisanAnnonceEvent) {
            DesinscriptionArtisanAnnonceEvent desinscriptionArtisanAnnonceEvent = (DesinscriptionArtisanAnnonceEvent) event
                    .getPayload();

            ClientDTO artisanToSuppress = desinscriptionArtisanAnnonceEvent.getArtisan();

            DesinscriptionAnnonceDTO desinscriptionAnnonceDTO = new DesinscriptionAnnonceDTO();
            desinscriptionAnnonceDTO.setId(idAnnonce);
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

        if (event.getPayload() instanceof NoterArtisanEventClose) {
            NoterArtisanEventClose noterArtisanEventClose = (NoterArtisanEventClose) event.getPayload();

            AvisDTO notationDTO = new AvisDTO();
            notationDTO.setNomEntreprise(annonceAffichageDTO.getEntrepriseSelectionnee().getNomComplet());
            notationDTO.setCommentaire(noterArtisanEventClose.getCommentaireNotation());
            notationDTO.setScore(noterArtisanEventClose.getNbEtoiles());
            notationDTO.setNomPrenomOrLoginClient(userConnected.getLogin());

            AvisArtisanDTO noterArtisanDTO = new AvisArtisanDTO();
            noterArtisanDTO.setHashID(idAnnonce);
            noterArtisanDTO.setLoginArtisan(annonceAffichageDTO.getEntrepriseSelectionnee().getArtisan().getLogin());
            noterArtisanDTO.setLoginDemandeur(userConnected.getLogin());
            noterArtisanDTO.setNotation(notationDTO);

            Integer codeService = annonceServiceREST.noterUnArtisan(noterArtisanDTO);

            annonceAffichageDTO.getAnnonce().setEtatAnnonce(EtatAnnonce.TERMINER);
            etatAnnonceValue.setObject(annonceAffichageDTO.getAnnonce().getEtatAnnonce().getType());

            if (codeService.equals(CodeRetourService.RETOUR_OK)) {
                feedBackPanelGeneral.success("Artisan noté avec succés, merci de votre retour");
            } else {
                feedBackPanelGeneral
                        .error("Problème lors de la notation d'une entreprise, veuillez réessayer ultérieurement");
            }

            noterArtisanEventClose.getTarget()
                    .add(feedBackPanelGeneral, etatAnnonce, notationAnnonceParClientContainer);
        }
    }

    /**
     * Principalement utilisé pour l'affichage des liens dans le menu action
     * <p/>
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
        return basicCheckForArtisanLink() && !annonceAffichageDTO.getIsArtisanInscrit();
    }

    /**
     * Check pour savoir si on affiche le lien d'envoi du devis
     *
     * @return true si le lien doit etre affiché
     */
    private boolean afficherEnvoyerDevisAnnonce() {
        return basicCheckForArtisanLink() && annonceAffichageDTO.getIsArtisanInscrit();
    }
}