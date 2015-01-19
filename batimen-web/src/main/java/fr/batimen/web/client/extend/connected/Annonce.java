package fr.batimen.web.client.extend.connected;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.error.AccesInterdit;
import fr.batimen.web.client.master.MasterPage;

public class Annonce extends MasterPage {

    private static final long serialVersionUID = -3604005728078066454L;

    private Link<Void> modifierAnnonce;
    private AjaxLink<Void> supprimerAnnonce;
    private AjaxLink<Void> inscrireAnnonce;
    private Link<Void> envoyerDevis;

    private AnnonceDTO annonceDTO;

    private Boolean modeInscris;

    private Annonce() {
        super("", "", "Annonce particulier", true, "img/bg_title1.jpg");
        // verifyRightForAnnonce();
        initComposants();
        initAction();
    }

    public Annonce(PageParameters params) {
        this();
        StringValue idAnnonce = params.get("idAnnonce");
        annonceDTO = new AnnonceDTO();
        // TODO : Passer l'id au webservice pour charger les données de
        // l'annonce

    }

    private void verifyRightForAnnonce() {
        Authentication auth = new Authentication();
        String login = auth.getCurrentUserInfo().getLogin();

        if (!login.equals(annonceDTO.getLoginOwner())) {
            this.setResponsePage(AccesInterdit.class);
        }

        // TODO : Acces en mode degradé si pas inscrit à l'annonce : affichage
        // de tout sauf des
        // informations de contacts et des autres artisans inscrits dans le cas
        // d'un partenaire.

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
        modifierAnnonce = new Link<Void>("modifierAnnonce") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                // TODO Pluger la modification de l'annonce une fois page faite
            }

            @Override
            public boolean isVisible() {
                try {
                    SecurityUtils.getSubject().checkRole(TypeCompte.CLIENT.getRole());
                    return true;
                } catch (AuthorizationException ae) {
                    return false;
                }
            }
        };
        add(modifierAnnonce);
    }
}
