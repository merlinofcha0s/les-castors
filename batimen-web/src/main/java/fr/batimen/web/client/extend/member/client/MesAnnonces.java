package fr.batimen.web.client.extend.member.client;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.MesAnnoncesPageDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.LinkLabel;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ClientsService;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */

public final class MesAnnonces extends MasterPage {

    private static final long serialVersionUID = 1902734649854998120L;

    private List<AnnonceDTO> annonces;
    private List<NotificationDTO> notifications;

    public MesAnnonces() {
        super("Page accueil de batimen", "lol", "Bienvenue sur lescastors.fr", true, "img/bg_title1.jpg");

        initLink();
        initStaticComposant();
        getMesInfosForPage();
        initRepeaterNotifications();
        initRepeaterDevis();
        this.setOutputMarkupId(true);
    }

    private void initStaticComposant() {
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        this.add(contactezNous);
    }

    private void initLink() {
        Link<Void> contactLink = new Link<Void>("contact") {

            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick() {
                this.setResponsePage(Contact.class);
            }
        };
        this.add(contactLink);
    }

    private void initRepeaterNotifications() {
        Label nbNotification = new Label("nbNotification", notifications.size());

        ListView<NotificationDTO> listViewNotification = new ListView<NotificationDTO>("listNotification",
                notifications) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NotificationDTO> item) {
                NotificationDTO notification = item.getModelObject();

                final Model<String> nomEntrepriseModelForLbl = new Model<String>(notification.getNomEntreprise());

                LinkLabel linkEntreprise = new LinkLabel("linkEntreprise", nomEntrepriseModelForLbl) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // TODO A Completer quand la page entreprise sera prete
                    }

                };

                StringBuilder contenuNotification = new StringBuilder(" ");
                contenuNotification.append(notification.getTypeNotification().getAffichage()).append(" ");
                Label typeNotification = new Label("typeNotification", contenuNotification.toString());

                Link<Void> linkAnnonce = new Link<Void>("linkAnnonce") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // TODO A Completer quand la page consultation annonce
                        // sera prete
                    }

                };
                SimpleDateFormat dateNotificationFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Label dateNotification = new Label("dateNotification", dateNotificationFormatter.format(notification
                        .getDateNotification()));

                item.add(linkEntreprise);
                item.add(typeNotification);
                item.add(linkAnnonce);
                item.add(dateNotification);
            }

        };

        this.add(nbNotification);
        this.add(listViewNotification);

    }

    private void initRepeaterDevis() {

        Label nbAnnonce = new Label("nbAnnonce", annonces.size());

        ListView<AnnonceDTO> listViewAnnonce = new ListView<AnnonceDTO>("listAnnonce", annonces) {
            private static final long serialVersionUID = 9041719964383711900L;

            @Override
            protected void populateItem(ListItem<AnnonceDTO> item) {
                AnnonceDTO annonce = item.getModelObject();

                StringBuilder descriptionCutting = new StringBuilder(annonce.getDescription().substring(0, 30));
                descriptionCutting.append("...");

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
                        // TODO Mettre en place quand la page annonce sera prete
                    }

                };

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
        Authentication authentication = new Authentication();

        MesAnnoncesPageDTO mesInfos = ClientsService.getMesInfosAnnoncePage(authentication.getCurrentUserInfo()
                .getLogin());

        annonces = mesInfos.getAnnonces();
        notifications = mesInfos.getNotifications();
        /*
         * annonces = new ArrayList<AnnonceDTO>();
         * 
         * AnnonceDTO annonce = new AnnonceDTO(); annonce.setDateCreation(new
         * Date()); annonce.setDateMAJ(new Date());
         * annonce.setDelaiIntervention(
         * DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE);
         * annonce.setDescription
         * ("Une description de la plus pure des descriptions");
         * annonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
         * annonce.setNbConsultation(0); annonce.setNbDevis(0);
         * annonce.setCategorieMetier((short) 0);
         * annonce.setSousCategorieMetier("La sous cateogrie du lol");
         * annonce.setTypeContact(TypeContact.EMAIL);
         * 
         * annonces.add(annonce);
         */

    }
}