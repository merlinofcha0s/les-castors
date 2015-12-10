package fr.castor.web.client.component.breadcrumb;

import fr.castor.web.client.component.LinkLabel;
import fr.castor.web.client.component.link.MonProfilLink;
import fr.castor.web.client.extend.connected.Annonce;
import fr.castor.web.client.extend.connected.Entreprise;
import fr.castor.web.client.extend.member.client.MesAnnonces;
import fr.castor.web.client.extend.member.client.ModifierMonProfil;
import fr.castor.web.client.extend.member.client.MonProfil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Permet à l'utilisateur de connaitre sa position dans les pages. Il sait où il est.
 *
 * @author Casaucau Cyril
 */
public class BreadCrumbCastor extends Panel {

    private List<BreadCrumbLink> linkForBreadcrumb;
    private ListView<BreadCrumbLink> listViewLink;

    public static final String LINK_ID = "breadCrumbLink";

    public BreadCrumbCastor(String id, Class<? extends Page> clazz) {
        super(id);
        linkForBreadcrumb = buildLinkListBreadCrumb(clazz);
        initRepeaterLink();
    }

    public void initRepeaterLink() {
        listViewLink = new ListView<BreadCrumbLink>("listViewLink", linkForBreadcrumb) {
            @Override
            protected void populateItem(ListItem<BreadCrumbLink> item) {
                BreadCrumbLink lien = item.getModelObject();

                if (lien.isActive()) {
                    item.add(new AttributeModifier("class", "active"));
                    lien.getLink().setEnabled(false);
                }

                WebMarkupContainer divider = new WebMarkupContainer("divider") {
                    @Override
                    public boolean isVisible() {
                        return !lien.isActive();
                    }
                };
                item.add(divider);
                item.add(lien.getLink());
            }
        };
        add(listViewLink);
    }

    private List<BreadCrumbLink> buildLinkListBreadCrumb(Class<? extends Page> clazz) {

        List<BreadCrumbLink> links = new ArrayList<>();

        LinkLabel linkMesAnnonces = new LinkLabel(BreadCrumbCastor.LINK_ID, new Model<>("Mon compte")) {
            @Override
            public void onClick() {
                setResponsePage(MesAnnonces.class);
            }
        };

        BreadCrumbLink breadCrumbLinkMesAnnonces = new BreadCrumbLink(linkMesAnnonces, false);
        links.add(breadCrumbLinkMesAnnonces);

        if (clazz.equals(MonProfil.class)) {
            MonProfilLink monProfilLink = new MonProfilLink(BreadCrumbCastor.LINK_ID, new Model<>("Mon profil"));

            BreadCrumbLink breadCrumbLinkMonProfil = new BreadCrumbLink(monProfilLink, true);
            links.add(breadCrumbLinkMonProfil);
        }

        if (clazz.equals(ModifierMonProfil.class)) {
            LinkLabel linkModifProfil = new LinkLabel(BreadCrumbCastor.LINK_ID, new Model<>("Modifier le profil")) {
                @Override
                public void onClick() {
                    setResponsePage(ModifierMonProfil.class);
                }
            };

            BreadCrumbLink breadCrumbLinkModifierProfil = new BreadCrumbLink(linkModifProfil, true);
            links.add(breadCrumbLinkModifierProfil);
        }

        if (clazz.equals(Entreprise.class)) {
            LinkLabel linkEntreprise = new LinkLabel(BreadCrumbCastor.LINK_ID, new Model<>("Profil entreprise")) {
                @Override
                public void onClick() {
                    setResponsePage(Entreprise.class);
                }
            };

            BreadCrumbLink breadCrumbLinkEntreprise = new BreadCrumbLink(linkEntreprise, true);
            links.add(breadCrumbLinkEntreprise);
        }

        if (clazz.equals(Annonce.class)) {
            LinkLabel linkAnnonce = new LinkLabel(BreadCrumbCastor.LINK_ID, new Model<>("Consultation d'une annonce")) {
                @Override
                public void onClick() {
                    setResponsePage(Annonce.class);
                }
            };

            BreadCrumbLink breadCrumbLinkAnnonce = new BreadCrumbLink(linkAnnonce, true);
            links.add(breadCrumbLinkAnnonce);
        }

        return links;
    }

}