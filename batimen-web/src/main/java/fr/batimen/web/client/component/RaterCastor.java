package fr.batimen.web.client.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class RaterCastor extends Panel {

    private static final long serialVersionUID = 6704123311798304893L;

    private final WebMarkupContainer rater;

    private final AttributeModifier readOnly = new AttributeModifier("readOnly", "readOnly");

    public RaterCastor(String id) {
        super(id);
        rater = new WebMarkupContainer("rater");
        this.add(rater);
    }

    public RaterCastor(String id, Integer nbEtoile) {
        this(id);
        setNumberOfStars(nbEtoile);
    }

    public void setNumberOfStars(Integer nbEtoile) {
        rater.add(new AttributeModifier("value", nbEtoile.toString()));
    }

    public void setIsReadOnly(Boolean isReadOnly) {
        if (isReadOnly) {
            rater.add(readOnly);
        } else {
            if (rater.get("readOnly") != null) {
                rater.remove(readOnly);
            }
        }
    }

    public String getCommentaireScore(Integer nbEtoile) {

        switch (nbEtoile) {
        case 1:
            return "Pas satisfait";
        case 2:
            return "Peu mieux faire";
        case 3:
            return "Prestation correct";
        case 4:
            return "Bonne prestation";
        case 5:
            return "Très bonne prestation";
        default:
            return "";
        }

    }
}
