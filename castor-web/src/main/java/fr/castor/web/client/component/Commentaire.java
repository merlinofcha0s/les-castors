package fr.castor.web.client.component;

import fr.castor.web.client.extend.Contact;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commentaire extends Panel {

    private static final long serialVersionUID = 270201181732531769L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Commentaire.class);

    public Commentaire(String id) {
        super(id);
        initLink();
    }

    private void initLink() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des liens du composant de commentaire");
        }

        Link<Void> contactLink = new Link<Void>("contact") {

            private static final long serialVersionUID = 9041719967383711900L;

            @Override
            public void onClick() {
                this.setResponsePage(Contact.class);
            }
        };
        this.add(contactLink);
    }

}
