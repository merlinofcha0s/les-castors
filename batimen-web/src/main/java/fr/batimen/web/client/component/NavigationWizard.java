package fr.batimen.web.client.component;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import fr.batimen.core.exception.FrontEndException;

public class NavigationWizard extends Panel {

    private static final long serialVersionUID = -4561122813846106343L;

    private final Label etape1;
    private final Label etape2;
    private final Label etape3;
    private final Label etape4;
    private final Label etape5;

    public NavigationWizard(String id) {
        super(id);
        etape1 = new Label("etape1", "");
        etape2 = new Label("etape2", "");
        etape3 = new Label("etape3", "");
        etape4 = new Label("etape4", "");
        etape5 = new Label("etape5", "");

        this.add(etape1);
        this.add(etape2);
        this.add(etape3);
        this.add(etape4);
        this.add(etape5);

    }

    public NavigationWizard(String id, List<String> etapes) throws FrontEndException {
        this(id);

        for (int i = 0; i < etapes.size(); i++) {
            switch (i) {
            case 0:
                etape1.setDefaultModelObject(etapes.get(i));
                break;
            case 1:
                etape2.setDefaultModelObject(etapes.get(i));
                break;
            case 2:
                etape3.setDefaultModelObject(etapes.get(i));
                break;
            case 3:
                etape4.setDefaultModelObject(etapes.get(i));
                break;
            case 4:
                etape5.setDefaultModelObject(etapes.get(i));
                break;
            default:
                throw new FrontEndException("Trop d'etape pour le composant Navigation wizard (max 5 etapes)");
            }
        }
    }

}
