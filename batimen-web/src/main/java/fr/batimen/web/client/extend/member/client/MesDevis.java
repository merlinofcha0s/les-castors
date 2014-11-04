package fr.batimen.web.client.extend.member.client;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.security.Authentication;
import fr.batimen.web.client.component.CastorMenu;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.extend.Contact;
import fr.batimen.web.client.master.MasterPage;

/**
 * Page ou l'utilisateur pourra consulter son compte ainsi que l'avancement de
 * ces differents boulot/devis/notes, etc
 * 
 * @author Casaucau Cyril
 * 
 */

public final class MesDevis extends MasterPage {

    private static final long serialVersionUID = 1902734649854998120L;

    public MesDevis() {
        super("Page accueil de batimen", "lol", "Bienvenue sur lescastors.fr", true, "img/bg_title1.jpg");
        Authentication authentication = new Authentication();

        initLink();
        initStaticComposant();
        initRepeaterDevis();
        this.setOutputMarkupId(true);
    }

    private void initStaticComposant() {
        CastorMenu menu = new CastorMenu("menu");
        ContactezNous contactezNous = new ContactezNous("contactezNous");

        this.add(menu);
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

    private void initRepeaterDevis() {
        ListView<AnnonceDTO> listViewAnnonce = new ListView<AnnonceDTO>("listAnnonce") {
            private static final long serialVersionUID = 9041719964383711900L;

            @Override
            protected void populateItem(ListItem<AnnonceDTO> item) {
                AnnonceDTO annonce = item.getModelObject();

                StringBuilder descriptionCut = new StringBuilder(annonce.getDescription().substring(0, 20));
                descriptionCut.append("...");

                WebMarkupContainer iconCategorie = new WebMarkupContainer("iconCategorie");
                // TODO : Modifier l'argument de gitIconForCategorie une fois
                // que la categorie sera refactorer dans la BDD.
                iconCategorie.add(new AttributeModifier("class", CategorieLoader.getIconForCategorie((short) 0)));

                Label categorie = new Label("categorie", annonce.getCategorieMetier());
                Label description = new Label("description", descriptionCut.toString());
                Label nbDevis = new Label("nbDevis", annonce.getNbDevis());
                Label etatAnnonce = new Label("etatAnnonce", annonce.getEtatAnnonce().getType());

                item.add(categorie);
                item.add(description);
                item.add(nbDevis);
                item.add(etatAnnonce);
                item.add(iconCategorie);
            }
        };

        this.add(listViewAnnonce);
    }
}
