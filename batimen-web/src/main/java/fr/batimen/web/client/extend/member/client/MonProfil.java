package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.component.RaterCastor;
import fr.batimen.web.client.master.MasterPage;

public class MonProfil extends MasterPage {

    private static final long serialVersionUID = -7816716629862060521L;

    private MonProfil() {
        super("", "", "Mon Profil", true, "img/bg_title1.jpg");
        initComposants();
    }

    public MonProfil(PageParameters params) {
        this();
    }

    private void initComposants() {
        Profil profil = new Profil("profil");

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        RaterCastor rater = new RaterCastor("raterCastor", 3);
        rater.setIsReadOnly(true);

        this.add(profil);
        this.add(contactezNous);
        this.add(commentaire);
        this.add(rater);
    }
}
