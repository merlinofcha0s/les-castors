package fr.castor.web.client.extend.member.client;

import fr.castor.dto.ClientDTO;
import fr.castor.dto.AvisDTO;
import fr.castor.dto.aggregate.MonProfilDTO;
import fr.castor.web.app.security.Authentication;
import fr.castor.web.client.component.Commentaire;
import fr.castor.web.client.component.RaterCastor;
import fr.castor.web.client.master.MasterPage;
import fr.castor.web.client.component.ContactezNous;
import fr.castor.web.client.component.Profil;
import fr.castor.ws.client.service.ClientServiceREST;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

/**
 * Page où les utilisateurs pourront voir un resumé de leurs activités sur le
 * site
 * 
 * @author Casaucau Cyril
 * 
 */
public class MonProfil extends MasterPage {

    private static final long serialVersionUID = -7816716629862060521L;

    @Inject
    private ClientServiceREST clientsServiceREST;

    @Inject
    private Authentication authentication;

    private MonProfilDTO monProfilDTO;
    private ClientDTO client;

    private MonProfil() {
        super("", "", "Mon Profil", true, "img/bg_title1.jpg", true);
        getDataMonProfil();
        initComposants();
        initLabel();
        initRepeaterAvis();
    }

    public MonProfil(PageParameters params) {
        this();
    }

    private void initComposants() {
        Profil profil = new Profil("profil", false);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil);
        this.add(contactezNous);
        this.add(commentaire);
    }

    private void getDataMonProfil() {
        client = authentication.getCurrentUserInfo();
        monProfilDTO = clientsServiceREST.getMesInfosForMonProfil(client.getLogin());
    }

    private void initLabel() {
        Label login = new Label("login", client.getLogin());
        Label nbAnnonce = new Label("nbAnnonce", monProfilDTO.getNbAnnonce());
        Label isAvisEmpty = new Label("avisEmptyLabel", "Pas d'avis pour le moment") {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (monProfilDTO.getNotations().isEmpty()) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

        };
        add(login);
        add(nbAnnonce);
        add(isAvisEmpty);
    }

    private void initRepeaterAvis() {

        ListView<AvisDTO> listViewNotation = new ListView<AvisDTO>("listNotation", monProfilDTO.getNotations()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<AvisDTO> item) {
                RaterCastor rater = new RaterCastor("rater",  item.getModelObject(), true);
                item.add(rater);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                if (monProfilDTO.getNotations().isEmpty()) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }

        };

        add(listViewNotation);
    }
}