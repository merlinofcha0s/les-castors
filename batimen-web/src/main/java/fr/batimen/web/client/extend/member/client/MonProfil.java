package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.master.MasterPage;

public class MonProfil extends MasterPage {

    private static final long serialVersionUID = -7816716629862060521L;

    public MonProfil(String id) {
        this();
    }

    private MonProfil() {
        super("", "", "Mon Profil", true, "");
        initComposants();
    }

    public MonProfil(PageParameters params) {
        this();
    }

    private void initComposants() {
        Profil profil = new Profil("profil");
        this.add(profil);
    }

}
