package fr.batimen.web.client.component;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.batimen.dto.ClientDTO;
import fr.batimen.web.client.extend.member.client.MonProfil;

public class Profil extends Panel {

    private static final long serialVersionUID = -3775533895973607467L;

    private final Link<String> monProfil;

    public Profil(String id) {
        super(id);

        monProfil = new Link<String>("monProfil") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                PageParameters parameters = new PageParameters();
                ClientDTO client = (ClientDTO) SecurityUtils.getSubject().getSession().getAttribute("client");
                parameters.add("login", client.getLogin());
                this.setResponsePage(MonProfil.class, parameters);
            }
        };

        this.add(monProfil);
    }
}
