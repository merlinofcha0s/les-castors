package fr.batimen.web.client.component;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import fr.batimen.web.client.extend.Accueil;
import fr.batimen.web.client.extend.member.client.MesAnnonces;

public class CastorMenu extends Panel {

    private static final long serialVersionUID = -5996934383228198804L;

    public CastorMenu(String id) {
        super(id);

        initLink();
    }

    private void initLink() {
        Link<Void> mesDevisLink = new Link<Void>("mesDevis") {

            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick() {
                this.setResponsePage(MesAnnonces.class);
            }
        };

        Link<Void> logout = new Link<Void>("logout") {
            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick() {
                Session.get().invalidate();
                this.setResponsePage(Accueil.class);
            }

        };

        this.add(mesDevisLink);
        this.add(logout);
    }

}
