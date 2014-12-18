package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.NotationDTO;
import fr.batimen.dto.aggregate.MonProfilDTO;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.component.RaterCastor;
import fr.batimen.web.client.master.MasterPage;
import fr.batimen.ws.client.service.ClientsService;

public class MonProfil extends MasterPage {

    private static final long serialVersionUID = -7816716629862060521L;

    private MonProfilDTO monProfilDTO;
    private ClientDTO client;

    private MonProfil() {
        super("", "", "Mon Profil", true, "img/bg_title1.jpg");
        getDataMonProfil();
        initComposants();
        initLabel();
        initRepeaterAvis();
    }

    public MonProfil(PageParameters params) {
        this();
    }

    private void initComposants() {
        Profil profil = new Profil("profil");

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil);
        this.add(contactezNous);
        this.add(commentaire);
    }

    private void getDataMonProfil() {
        Authentication authentication = new Authentication();
        client = authentication.getCurrentUserInfo();
        monProfilDTO = ClientsService.getMesInfosForMonProfil(client.getLogin());
    }

    private void initLabel() {
        Label login = new Label("login", client.getLogin());
        Label nbAnnonce = new Label("nbAnnonce", monProfilDTO.getNbAnnonce());
        Label isAvisEmpty = new Label("avisEmpty", "Pas d'avis pour le moment") {

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

        ListView<NotationDTO> listViewNotation = new ListView<NotationDTO>("listNotation", monProfilDTO.getNotations()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NotationDTO> item) {
                NotationDTO notation = item.getModelObject();

                RaterCastor rater = new RaterCastor("raterCastor");
                rater.setIsReadOnly(Boolean.TRUE);
                rater.setNumberOfStars(notation.getScore().intValue());

                Label commentaireNotation = new Label("commentaireRater", rater.getCommentaireScore(notation.getScore()
                        .intValue()));
                Label nomEntreprise = new Label("nomEntreprise", notation.getNomEntreprise());
                Label commentaireClient = new Label("commentaireClient", notation.getCommentaire());

                item.add(rater);
                item.add(commentaireNotation);
                item.add(nomEntreprise);
                item.add(nomEntreprise);
                item.add(commentaireClient);
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
