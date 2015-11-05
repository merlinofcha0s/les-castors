package fr.castor.web.client.component;

import fr.castor.web.app.constants.JSConstant;
import fr.castor.web.client.behaviour.border.RequiredBorderBehaviour;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Composant qui affichage 1 a 5 etoiles permettant de noter un artisan
 *
 * @author Casaucau Cyril
 *
 */
public class RaterStarsCastor extends Panel {

    private static final long serialVersionUID = 6704123311798304893L;

    private final HiddenField<String> rater;

    private final AttributeModifier readOnly = new AttributeModifier("readOnly", "readOnly");

    public final static String NAME_FCT_JS_INIT_RATER_CASTOR = "initRaterCastor()";

    public RaterStarsCastor(String id) {
        super(id);
        rater = new HiddenField<>("rater", new Model<String>());
        rater.setMarkupId("raterCastorField");
        this.add(rater);
    }

    public RaterStarsCastor(String id, boolean required) {
        this(id);
        rater.setModel(new Model<String>());
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forScript("function initRaterCastor() { $('.rating').rating({filled: 'fa fa-star',filledSelected: 'fa fa-star', empty: 'fa fa-star-o'});}", "initCastorRater"));
        response.render(JSConstant.fontAwesome);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        //Réinitialisaton du javascript pour que le composant s'affiche en cas de requete ajax
        AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
        if (target != null) {
            target.appendJavaScript(RaterStarsCastor.NAME_FCT_JS_INIT_RATER_CASTOR);
        }
    }
}