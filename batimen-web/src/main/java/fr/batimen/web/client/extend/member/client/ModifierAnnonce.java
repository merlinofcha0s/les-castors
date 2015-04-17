package fr.batimen.web.client.extend.member.client;

import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.nouveau.devis.Etape4InscriptionForm;
import fr.batimen.web.client.master.MasterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * TODO passer un property compound de annonce à modifier annnonce
 * TODO refactore etape3 de demande de devis.
 * Created by Casaucau on 17/04/2015.
 */
public class ModifierAnnonce extends MasterPage{

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierAnnonce.class);

    public ModifierAnnonce(){
        super("Modifier mon annonce", "lol", "Modifier mon annonce", true, "img/bg_title1.jpg");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page de modification de mon profil");
        }

        //initData();
        initComposant();
    }

    private void initComposant() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des composants de la page de modification de mon profil");
        }
        Profil profil = new Profil("profil");

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, contactezNous, commentaire);
    }
}
