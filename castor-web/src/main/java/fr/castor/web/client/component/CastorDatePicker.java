package fr.castor.web.client.component;

import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;

import java.util.Date;

/**
 * Datepicker basé sur bootstrap
 *
 * @author Casaucau Cyril
 */
public class CastorDatePicker extends TextField<Date> {

    private static final long serialVersionUID = -5011785809353752263L;

    private String markupId;

    private StringBuilder initDatePicker;

    private boolean fueluxPresent;

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        initDatePicker = new StringBuilder("$('#");
        initDatePicker.append(markupId).append("').datepicker({format: 'dd/mm/yyyy', weekStart: 1})");
        response.render(OnDomReadyHeaderItem.forScript(initDatePicker));

        if(!fueluxPresent){
            response.render(JavaScriptHeaderItem.forUrl("js/bootstrap-datepicker.js"));
            response.render(CssContentHeaderItem.forUrl("css/datepicker.css"));
        }
    }

    public CastorDatePicker(String id, String markupId, boolean fueluxPresent) {
        super(id);
        this.markupId = markupId;
        this.fueluxPresent = fueluxPresent;

        setMarkupId(markupId);
    }
}