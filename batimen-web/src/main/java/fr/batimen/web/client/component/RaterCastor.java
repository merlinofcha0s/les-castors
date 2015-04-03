package fr.batimen.web.client.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;

public class RaterCastor extends Panel {

    private static final long serialVersionUID = 6704123311798304893L;

    private final HiddenField<String> rater;

    private final AttributeModifier readOnly = new AttributeModifier("readOnly", "readOnly");

    public RaterCastor(String id) {
        super(id);
        rater = new HiddenField<String>("rater", new Model<String>());
        this.add(rater);
    }

    public RaterCastor(String id, Integer nbEtoile, boolean required) {
        this(id);
        setNumberOfStars(nbEtoile);

        rater.setModel(new Model<String>(nbEtoile.toString()));
        if (required) {
            rater.setRequired(required);
            rater.add(new RequiredBorderBehaviour());
        }

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

    public Double getScore() {
        return Double.parseDouble(rater.getConvertedInput());
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
