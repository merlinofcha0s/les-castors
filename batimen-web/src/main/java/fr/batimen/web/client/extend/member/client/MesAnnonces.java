package fr.batimen.web.client.extend.member.client;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.web.app.constants.ParamsConstant;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.MesAnnoncesDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.FeedbackMessageLevel;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.LinkLabel;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.connected.Annonce;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ClientsServiceREST;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */

public final class MesAnnonces extends MasterPage {

    private static final long serialVersionUID = 1902734649854998120L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MesAnnonces.class);

    @Inject
    private ClientsServiceREST clientsServiceREST;

    @Inject
    private Authentication authentication;

    private List<AnnonceDTO> annonces;
    private List<NotificationDTO> notifications;

    public MesAnnonces() {
        super("Mes annonces ", "lol", "Bienvenue sur lescastors.fr", true, "img/bg_title1.jpg");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page mes annonces");
        }

        initStaticComposant();
        getMesInfosForPage();
        initRepeaterNotifications();
        initRepeaterAnnonces();
        this.setOutputMarkupId(true);
    }

    public MesAnnonces(String messageToFeedBack, FeedbackMessageLevel messageLevel) {
        this();
        this.feedBackPanelGeneral.sendMessage(messageToFeedBack, messageLevel);
    }

    private void initStaticComposant() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des composants statiques");
        }

        Profil profil = new Profil("profil");
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");
        this.add(profil);
        this.add(commentaire);
        this.add(contactezNous);
    }

    private void initRepeaterNotifications() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init du repeater des notifications");
        }

        Label nbNotification = new Label("nbNotification", notifications.size());

        ListView<NotificationDTO> listViewNotification = new ListView<NotificationDTO>("listNotification",
                notifications) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NotificationDTO> item) {
                final NotificationDTO notification = item.getModelObject();

                final Model<String> parQuiModel = new Model<>(parQui(notification));

                LinkLabel linkParQui = new LinkLabel("linkParQui", parQuiModel) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        onclickParQuiLink(notification);
                    }
                };

                StringBuilder contenuNotification = new StringBuilder(" ");
                contenuNotification.append(notification.getTypeNotification().getAffichage()).append(" ");
                Label typeNotification = new Label("typeNotification", contenuNotification.toString());

                final Model<String> objetNotification = new Model<>(getObjetNotification(notification));

                LinkLabel linkObjetNotification = new LinkLabel("linkObjetNotification", objetNotification) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        onclickObjetNotification(notification);
                    }
                };

                linkObjetNotification.setOutputMarkupId(true);

                SimpleDateFormat dateNotificationFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Label dateNotification = new Label("dateNotification", dateNotificationFormatter.format(notification
                        .getDateNotification()));

                item.add(linkParQui);
                item.add(typeNotification);
                item.add(linkObjetNotification);
                item.add(dateNotification);
            }

        };

        this.add(nbNotification);
        this.add(listViewNotification);

    }

    private void initRepeaterAnnonces() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init du repeater des annonces");
        }

        Label nbAnnonce = new Label("nbAnnonce", annonces.size());

        ListView<AnnonceDTO> listViewAnnonce = new ListView<AnnonceDTO>("listAnnonce", annonces) {
            private static final long serialVersionUID = 9041719964383711900L;

            @Override
            protected void populateItem(ListItem<AnnonceDTO> item) {
                final AnnonceDTO annonce = item.getModelObject();

                StringBuilder descriptionCutting = new StringBuilder();
                if (annonce.getDescription().length() > 30) {
                    descriptionCutting.append(annonce.getDescription().substring(0, 30));
                    descriptionCutting.append("...");
                } else {
                    descriptionCutting.append(annonce.getDescription());
                }

                WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
                StringBuilder classCssIcon = new StringBuilder("iconsMesDevis");
                classCssIcon.append(" ").append(CategorieLoader.getIconForCategorie(annonce.getCategorieMetier()));

                iconCategorie.add(new AttributeModifier("class", classCssIcon.toString()));

                Label categorie = new Label("categorie", CategorieLoader.getCategorieByCode(annonce
                        .getCategorieMetier()));
                Label description = new Label("description", descriptionCutting.toString());
                Label nbDevis = new Label("nbDevis", annonce.getNbDevis());
                Label etatAnnonce = new Label("etatAnnonce", annonce.getEtatAnnonce().getType());

                Link<Void> voirAnnonce = new Link<Void>("voirAnnonce") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add(ParamsConstant.ID_ANNONCE_PARAM, annonce.getHashID());
                        this.setResponsePage(Annonce.class, params);
                    }

                };

                voirAnnonce.setOutputMarkupId(true);

                item.add(iconCategorie);
                item.add(categorie);
                item.add(description);
                item.add(nbDevis);
                item.add(etatAnnonce);
                item.add(voirAnnonce);
            }
        };

        this.add(nbAnnonce);
        this.add(listViewAnnonce);
    }

    private void getMesInfosForPage() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Appel webservice pour la récupération des données a afficher");
        }
        MesAnnoncesDTO mesInfos = clientsServiceREST.getMesInfosAnnonce(authentication.getCurrentUserInfo().getLogin());

        annonces = mesInfos.getAnnonces();
        notifications = mesInfos.getNotifications();
    }

    private String parQui(NotificationDTO notificationDTO){
        if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.ARTISAN)){
            return notificationDTO.getNomEntreprise();
        } else if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.CLIENT)){
            return notificationDTO.getClientLogin();
        }
        return "";
    }

    private void onclickParQuiLink(NotificationDTO notificationDTO){
        if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.ARTISAN)){
            // URLEncoder.encode(notification.getArtisanNotifier().getEntreprise().getNomComplet(),
            // "UTF-8")
            // TODO A Completer quand la page entreprise sera prete
        } else if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.CLIENT)){
            PageParameters parameters = new PageParameters();
            parameters.add("login", notificationDTO.getClientLogin());
            this.setResponsePage(MonProfil.class, parameters);
        }
    }

    private String getObjetNotification(NotificationDTO notificationDTO){
        if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.ARTISAN)){
            return "annonce";
        } else if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.CLIENT)){
            return "entreprise";
        }else{
            return "";
        }
    }

    private void onclickObjetNotification(NotificationDTO notificationDTO){
        if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.ARTISAN)){
            PageParameters params = new PageParameters();
            params.add(ParamsConstant.ID_ANNONCE_PARAM, notificationDTO.getHashIDAnnonce());
            this.setResponsePage(Annonce.class, params);
        } else if(notificationDTO.getTypeNotification().getParQui().equals(TypeCompte.CLIENT)){
            // TODO A Completer quand la page entreprise sera prete
        }
    }
}