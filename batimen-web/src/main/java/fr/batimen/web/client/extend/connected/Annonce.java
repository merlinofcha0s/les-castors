package fr.batimen.web.client.extend.connected;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.master.MasterPage;

public class Annonce extends MasterPage {

    private static final long serialVersionUID = -3604005728078066454L;

    private Annonce() {
        super("", "", "Annonce particulier", true, "img/bg_title1.jpg");
    }

    public Annonce(PageParameters params) {
        this();
        StringValue idAnnonce = params.get("idAnnonce");
        // TODO : Passer l'id au webservice pour charger les données de
        // l'annonce
        // TODO : Pour l'acces complet : Controle d'accés : Celui qui possede
        // l'annonce, celui qui
        // s'est inscrit a l'annonce (l'a payé)
        // TODO : Acces en mode degradé : Quelles données sont affichés ? est ce
        // qu'il va juste avoir accés a la liste d'annonce ?

        initComposants();
    }

    private void initComposants() {
        Profil profil = new Profil("profil");

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil);
        this.add(contactezNous);
        this.add(commentaire);
    }

}
